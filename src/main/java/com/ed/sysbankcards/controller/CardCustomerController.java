package com.ed.sysbankcards.controller;


import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.service.CustomerCardFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/cards")
@RestController
public class CardCustomerController {

    private final CustomerCardFunctionService cardFunctionService;

    @Operation(summary = "Получить данные карты", description = "В ответе возвращается dto.")
    @Tag(name = "get", description = "Card API")
    @GetMapping("/get/{cardNumber}")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CardResponse getCard(@PathVariable String cardNumber) {
        return cardFunctionService.getCustomerCard(cardNumber);
    }

    @Operation(summary = "Получить список карт", description = "В ответе возвращается List dto.")
    @Tag(name = "get", description = "Card API")
    @GetMapping("/cards")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CardResponse> getCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long id) {

        return cardFunctionService.getCustomerCards(id, status, page, size);
    }

    @Operation(summary = "Выполнить перевод между своими картами", description = "В ответе возвращается dto перевода.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/transfer")
    //@PreAuthorize("hasRole('USER')")
    public TransactionResponse transfer(@Valid @RequestBody TransferFundsBetweenUserCardsRequest transferDto) {
        return cardFunctionService.transferBetweenCards(transferDto);
    }

    @Operation(summary = "Выполнить вывод средств с карты", description = "В ответе возвращается dto транзакции вывода.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/withdraw")
    //@PreAuthorize("hasRole('USER')")
    public TransactionResponse withdraw(@Valid @RequestBody WithdrawFundsRequest withdrawDto) {
        return cardFunctionService.withdrawalFromCard(withdrawDto);
    }

    @Operation(summary = "Получить список транзакций по карте", description = "В ответе возвращается List dto транзакций.")
    @Tag(name = "get", description = "Card API")
    @GetMapping("/transactions")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<TransactionResponse> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, ShowTransactionalByCardRequest historyTransactionsDto) {
        return cardFunctionService.getTransactionalByCard(historyTransactionsDto, page, size);
    }

    @Operation(summary = "Заблокировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "put", description = "Card API")
    @PutMapping("/block")
    public void blockCard(@Valid @RequestBody BlockCardRequest blockCardDto){
        cardFunctionService.requestCardBlock(blockCardDto);
    }

}
