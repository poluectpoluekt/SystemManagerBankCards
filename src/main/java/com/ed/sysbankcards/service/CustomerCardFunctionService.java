package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.card.CardBlockedException;
import com.ed.sysbankcards.exception.card.CardWithNumberNoExistsException;
import com.ed.sysbankcards.exception.card.InsufficientFundsException;
import com.ed.sysbankcards.exception.card.LimitExhaustedException;
import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import com.ed.sysbankcards.mapper.CardMapper;
import com.ed.sysbankcards.mapper.TransactionMapper;
import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.Limit;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.model.enums.LimitType;
import com.ed.sysbankcards.model.enums.TransactionStatus;
import com.ed.sysbankcards.model.enums.TransactionType;
import com.ed.sysbankcards.repository.CardRepository;
import com.ed.sysbankcards.repository.TransactionRepository;
import com.ed.sysbankcards.util.CardNumberEncryptorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerCardFunctionService {

    private final CardRepository cardRepository;
    private final CustomerService customerService;
    private final TransactionRepository transactionRepository;
    private final CardMapper cardMapper;
    private final TransactionMapper transactionMapper;
    private final IdempotencyService idempotencyService;
    private final long TIME_LIFE_RECORD_DB = 3600;


    @Transactional(readOnly = true)
    public Page<CardResponse> getCustomerCards(Long customerId, CardStatus status, int page, int size, String idemKey) {

        if(customerService.findCustomerById(customerId).isEmpty()){
            throw new CustomerNotFoundException(customerId);
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,"createdAt"));
        if(status != null){
            return cardRepository.findByCustomerIdAndStatus(customerId, status, pageable)
                    .map(cardMapper::toCardResponse);
        }

        Page<CardResponse> responses = cardRepository.findByCustomerId(customerId, pageable).map(cardMapper::toCardResponse);
        idempotencyService.saveIdempotencyKey(idemKey, responses, TIME_LIFE_RECORD_DB);
        return responses;
    }

    @Transactional(readOnly = true)
    public CardResponse getCustomerCard(String cartNumber, String idempotencyKey) {


        CardResponse response = cardMapper.toCardResponse(cardRepository.findByCardNumber(cartNumber)
                .orElseThrow(()-> new CardWithNumberNoExistsException(cartNumber)));

        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;
    }

    @Transactional
    public String requestCardBlock(BlockCardRequest blockCardDto, String idempotencyKey) {
        Card card = cardRepository.findByCardNumber(blockCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(blockCardDto.getCardNumber()));

        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new RuntimeException("Card is already blocked");
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        String stringResultResponse = "Card has been blocked";
        idempotencyService.saveIdempotencyKey(idempotencyKey, stringResultResponse, TIME_LIFE_RECORD_DB);
        return stringResultResponse;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionalByCard(ShowTransactionalByCardRequest Dto,
                                                            int page, int size, String idempotencyKey) {
        Card card = cardRepository.findByCardNumber(Dto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(Dto.getCardNumber()));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt"));

        List<TransactionResponse> responses = transactionRepository.findBySourceCard(card, pageable)
                .stream().map(transactionMapper::toTransactionResponse).toList();

        idempotencyService.saveIdempotencyKey(idempotencyKey, responses, TIME_LIFE_RECORD_DB);
        return responses;
    }


    @Transactional
    public TransactionResponse transferBetweenCards(TransferFundsBetweenUserCardsRequest transferFundsDto,
                                                    String idempotencyKey) {

        Card cardFrom = cardRepository.findByCardNumberWithLock(transferFundsDto.getFromCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(transferFundsDto.getFromCardNumber()));

        Card cardTo = cardRepository.findByCardNumberWithLock(transferFundsDto.getToCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(transferFundsDto.getFromCardNumber()));

        if (cardFrom.getStatus() != CardStatus.ACTIVE || cardTo.getStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException();
        }

        if (cardFrom.getBalance().compareTo(transferFundsDto.getAmount()) < 0) {
            throw new InsufficientFundsException();
        }

        checkLimits(cardFrom, transferFundsDto.getAmount());

        cardFrom.setBalance((cardFrom.getBalance().subtract(transferFundsDto.getAmount())));
        cardTo.setBalance(cardTo.getBalance().add(transferFundsDto.getAmount()));

        Transaction transferTransaction = new Transaction();
        transferTransaction.setSourceCard(cardFrom);
        transferTransaction.setTargetCard(cardTo);
        transferTransaction.setAmount(transferFundsDto.getAmount());
        transferTransaction.setCurrency(transferFundsDto.getCurrency());
        transferTransaction.setTransactionType(TransactionType.TRANSFER);
        transferTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);
        TransactionResponse response = transactionMapper.toTransactionResponse(transactionRepository.save(transferTransaction));

        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;

    }

    @Transactional
    public TransactionResponse withdrawalFromCard(WithdrawFundsRequest withdrawDto, String idempotencyKey){
        Card cardFrom = cardRepository.findByCardNumberWithLock(withdrawDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(withdrawDto.getCardNumber()));

        BigDecimal amountWithdraw = withdrawDto.getAmount();

        if(cardFrom.getStatus() != CardStatus.ACTIVE){
            throw new CardBlockedException();
        }

        if (cardFrom.getBalance().compareTo(amountWithdraw) < 0) {
            throw new InsufficientFundsException();
        }

        checkLimits(cardFrom, amountWithdraw);

        cardFrom.setBalance(cardFrom.getBalance().subtract(amountWithdraw));

        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setSourceCard(cardFrom);
        withdrawTransaction.setAmount(withdrawDto.getAmount());
        withdrawTransaction.setCurrency(withdrawDto.getCurrency());
        withdrawTransaction.setTransactionType(TransactionType.DEBIT);
        withdrawTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        cardRepository.save(cardFrom);

        TransactionResponse response = transactionMapper.toTransactionResponse(transactionRepository.save(withdrawTransaction));
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);

        return response;

    }

    @Transactional
    public TransactionResponse cardReplenishment(ReplenishmentCardRequest replenishmentCardDto, String idempotencyKey) {

        Card card = cardRepository.findByCardNumberWithLock(replenishmentCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(replenishmentCardDto.getCardNumber()));


        card.setBalance(card.getBalance().add(replenishmentCardDto.getAmount()));

        Transaction replenishTransaction = new Transaction();
        replenishTransaction.setSourceCard(card);
        replenishTransaction.setAmount(replenishmentCardDto.getAmount());
        replenishTransaction.setTransactionType(TransactionType.CREDIT);
        replenishTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        replenishTransaction.setCurrency("RUB");
        TransactionResponse transactionResponse = transactionMapper
                .toTransactionResponse(transactionRepository.save(replenishTransaction));

        cardRepository.save(card);

        idempotencyService.saveIdempotencyKey(idempotencyKey, transactionResponse, TIME_LIFE_RECORD_DB);
        return transactionResponse;
    }

    private void checkLimits(Card card, BigDecimal amount) {
        Limit limit = card.getLimit();
        if (limit == null) {
            return; // Нет лимитов, операция разрешена
        }

        // Проверка дневного лимита
        BigDecimal dailySpent = calculateDailySpent(card);
        if (dailySpent.add(amount).compareTo(limit.getDailyLimit()) > 0) {
            throw new LimitExhaustedException(limit.getDailyLimit(), LimitType.DAILY,
                    limit.getDailyLimit().subtract(dailySpent));
        }

        // Проверка месячного лимита
        BigDecimal monthlySpent = calculateMonthlySpent(card);
        if (monthlySpent.add(amount).compareTo(limit.getMonthlyLimit()) > 0) {
            throw new LimitExhaustedException(limit.getDailyLimit(), LimitType.MONTHLY,
                    limit.getMonthlyLimit().subtract(monthlySpent));
        }
    }

    private BigDecimal calculateDailySpent(Card card) {
        LocalDateTime startOfDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        return card.getHistory().stream()
                .filter(t -> t.getCreatedAt().isAfter(startOfDay))
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(t -> t.getAmount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlySpent(Card card) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        return card.getHistory().stream()
                .filter(t -> t.getCreatedAt().isAfter(startOfMonth))
                .filter(t -> t.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(t -> t.getAmount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
