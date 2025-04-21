package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.card.CardWithNumberAlreadyExistsException;
import com.ed.sysbankcards.exception.card.CardWithNumberNoExistsException;
import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import com.ed.sysbankcards.mapper.CardMapper;
import com.ed.sysbankcards.mapper.TransactionMapper;
import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.Limit;
import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.repository.CardRepository;
import com.ed.sysbankcards.repository.LimitRepository;
import com.ed.sysbankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCardFunction {

    private final CardRepository cardRepository;
    private final CustomerService customerService;
    private final CardMapper cardMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final LimitService limitService;
    private final IdempotencyService idempotencyService;
    private final long TIME_LIFE_RECORD_DB = 3600;

    @Transactional
    public CardResponse createCard(CreateCardRequest createCardDto, String idempotencyKey) {

        Card card = new Card();
        if(cardRepository.findByCardNumber(createCardDto.getCardNumber()).isPresent()) {
            throw new CardWithNumberAlreadyExistsException(createCardDto.getCardNumber());
        }
        card.setCardNumber(createCardDto.getCardNumber());

        card.setCustomer(customerService.findCustomerById(createCardDto
                .getCardOwner()).orElseThrow(()-> new CustomerNotFoundException(createCardDto.getCardOwner())));

        card.setExpiryDate(createCardDto.getExpiryDate());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setCurrency("RUB");

        CardResponse response = cardMapper.toCardResponse(cardRepository.save(card));
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);

        return response;
    }

    public CardResponse updateCard(UpdateCardRequest updateDto) {
        Card card = cardRepository.findByCardNumber(updateDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(updateDto.getCardNumber()));

        card.setCardNumber(updateDto.getNewCardNumber());
        card.setExpiryDate(updateDto.getNewExpiryDate());

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Transactional
    public String blockCard(BlockCardRequest blockCardDto, String idempotencyKey) {
        Card card = cardRepository.findByCardNumber(blockCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(blockCardDto.getCardNumber()));
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        String response = "Card blocked";
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;
    }

    @Transactional
    public String activateCard(ActivateCardRequest activateCardDto, String idempotencyKey) {
        Card card = cardRepository.findByCardNumber(activateCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(activateCardDto.getCardNumber()));
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);

        String response = "Card activated successfully";
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;
    }

    @Transactional
    public String deleteCard(DeleteCardRequest deleteCardDto, String idempotencyKey) {
        Card card = cardRepository.findByCardNumber(deleteCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(deleteCardDto.getCardNumber()));

        cardRepository.deleteById(card.getId());
        String response = "Card deleted";
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;
    }

    @Transactional(readOnly = true)
    public List<CardResponse> getAllCards() {
        return cardRepository.findAll().stream().map(cardMapper::toCardResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getCardTransactions(ShowTransactionalByCardRequest cardDto) {
        Card card = cardRepository.findByCardNumber(cardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(cardDto.getCardNumber()));
        return transactionRepository.findBySourceCard(card).stream().map(transactionMapper::toTransactionResponse).toList();
    }

    @Transactional
    public String setCardLimit(SetLimitsForCardRequest limitDto, String idempotencyKey) {

        limitService.setCardLimit(limitDto);
        String resultStringResponse = "The limit has been set";
        idempotencyService.saveIdempotencyKey(idempotencyKey, resultStringResponse, TIME_LIFE_RECORD_DB);
        return resultStringResponse;
    }

}
