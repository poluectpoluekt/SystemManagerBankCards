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

    @Transactional
    public CardResponse createCard(CreateCardRequest createCardDto) {

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

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    public CardResponse updateCard(UpdateCardRequest updateDto) {
        Card card = cardRepository.findByCardNumber(updateDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(updateDto.getCardNumber()));

//        card.setCardNumber(passwordEncoder.encode(updateDto.getNewCardNumber()));
        card.setExpiryDate(LocalDate.parse(updateDto.getNewExpiryDate()));

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Transactional
    public void blockCard(BlockCardRequest blockCardDto) {
        Card card = cardRepository.findByCardNumber(blockCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(blockCardDto.getCardNumber()));
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Transactional
    public void activateCard(ActivateCardRequest activateCardDto) {
        Card card = cardRepository.findByCardNumber(activateCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(activateCardDto.getCardNumber()));
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(DeleteCardRequest deleteCardDto) {
        Card card = cardRepository.findByCardNumber(deleteCardDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(deleteCardDto.getCardNumber()));

        cardRepository.deleteById(card.getId());
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
    public void setCardLimit(SetLimitsForCardRequest limitDto) {
        limitService.setCardLimit(limitDto);
    }

}
