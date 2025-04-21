package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.card.CardWithNumberAlreadyExistsException;
import com.ed.sysbankcards.mapper.CardMapper;
import com.ed.sysbankcards.mapper.TransactionMapper;
import com.ed.sysbankcards.model.dto.card.CardResponse;
import com.ed.sysbankcards.model.dto.card.CreateCardRequest;
import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.Customer;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.repository.CardRepository;
import com.ed.sysbankcards.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AdminCardFunctionTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private LimitService limitService;

    @InjectMocks
    private AdminCardFunction adminCardFunction;

    private Card card;
    private Customer customer;
    private CardResponse cardResponse;
    private Transaction transaction;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        card = new Card();
        card.setId(1L);
        card.setCardNumber("1234567890123456");
        card.setCustomer(customer);
        card.setExpiryDate(LocalDate.of(2026, 12, 31));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setCurrency("RUB");

        cardResponse = new CardResponse();
        cardResponse.setCardNumber("**** **** **** 3456");
        cardResponse.setStatus("ACTIVE");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceCard(card);

        transactionResponse = new TransactionResponse();
    }

//    @Test
//    void createCard_Success_ReturnsCardResponse() {
//
//        CreateCardRequest request = new CreateCardRequest();
//        request.setCardNumber("1234567890123456");
//        request.setCardOwner(1L);
//        request.setExpiryDate(LocalDate.of(2026, 12, 31));
//
//        Mockito.when(cardRepository.findByCardNumber(request.getCardNumber())).thenReturn(Optional.empty());
//        Mockito.when(customerService.findCustomerById(1L)).thenReturn(Optional.of(customer));
//        Mockito.when(cardRepository.save(ArgumentMatchers.any(Card.class))).thenReturn(card);
//        Mockito.when(cardMapper.toCardResponse(card)).thenReturn(cardResponse);
//
//
//        CardResponse result = adminCardFunction.createCard(request, );
//
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(cardResponse, result);
//        Mockito.verify(cardRepository).findByCardNumber(request.getCardNumber());
//        Mockito.verify(customerService).findCustomerById(1L);
//        Mockito.verify(cardRepository).save(ArgumentMatchers.any(Card.class));
//        Mockito.verify(cardMapper).toCardResponse(card);
//    }

//    @Test
//    void createCard_CardNumberExists_ThrowsException() {
//
//        CreateCardRequest request = new CreateCardRequest();
//        request.setCardNumber("1234567890123456");
//
//        Mockito.when(cardRepository.findByCardNumber(request.getCardNumber())).thenReturn(Optional.of(card));
//
//        Assertions.assertThrows(CardWithNumberAlreadyExistsException.class, () -> adminCardFunction.createCard(request));
//        Mockito.verify(cardRepository).findByCardNumber(request.getCardNumber());
//        Mockito.verifyNoInteractions(customerService, cardMapper, cardRepository);
//    }
}
