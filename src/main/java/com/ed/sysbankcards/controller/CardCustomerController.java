package com.ed.sysbankcards.controller;


import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.service.CustomerCardFunctionService;
import com.ed.sysbankcards.service.IdempotencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/api/cards")
@RestController
public class CardCustomerController {

    private final CustomerCardFunctionService cardFunctionService;
    private final IdempotencyService idempotencyService;

    /**
     * Запрос получений данных карты
     * @param cardNumber номер карты
     * @param idempotencyKey
     * @return dto данных карты
     */
    @Operation(summary = "Получить данные карты", description = "В ответе возвращается dto.")
    @Tag(name = "get", description = "Card API")
    @GetMapping("/get/{cardNumber}")
    public CardResponse getCard(@PathVariable String cardNumber,
                                @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {
        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (CardResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.getCustomerCard(cardNumber, idempotencyKey);
    }

    /**
     * Запрос на получение всех карт по статусу и пагинацией
     */
    @Operation(summary = "Получить список карт", description = "В ответе возвращается List dto.")
    @Tag(name = "get", description = "Card API")
    @GetMapping()
    public Page<CardResponse> getCards(
            @RequestParam(required = false) CardStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long idCustomer,
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (Page<CardResponse>) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.getCustomerCards(idCustomer, status, page, size, idempotencyKey);
    }

    /**
     * Запрос перевода средств между своими картами
     * @param transferDto dto c параметрами перевода
     * @param idempotencyKey
     * @return dto транзакции
     */
    @Operation(summary = "Выполнить перевод между своими картами", description = "В ответе возвращается dto перевода.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/transfer")
    public TransactionResponse transfer(@Valid @RequestBody TransferFundsBetweenUserCardsRequest transferDto,
                                        @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {
        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {

            return (TransactionResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.transferBetweenCards(transferDto, idempotencyKey);
    }

    /**
     * Запрос вывода средств с карты
     * @param withdrawDto dto c параметрами вывода
     * @param idempotencyKey
     * @return dto транзакции
     */
    @Operation(summary = "Выполнить вывод средств с карты", description = "В ответе возвращается dto транзакции вывода.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@Valid @RequestBody WithdrawFundsRequest withdrawDto,
                                        @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {
        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (TransactionResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.withdrawalFromCard(withdrawDto, idempotencyKey);
    }

    /**
     * Запрос получения всех транзакций по карте
     * @return лист dto траназакций
     */
    @Operation(summary = "Получить список транзакций по карте", description = "В ответе возвращается List dto транзакций.")
    @Tag(name = "get", description = "Card API")
    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, ShowTransactionalByCardRequest historyTransactionsDto,
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (List<TransactionResponse>) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.getTransactionalByCard(historyTransactionsDto, page, size, idempotencyKey);
    }

    /**
     * Запрос блокирования карты
     * @param blockCardDto dto c номером карты
     * @param idempotencyKey
     * @return Строка с ответом
     */
    @Operation(summary = "Заблокировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "put", description = "Card API")
    @PutMapping("/block")
    public String blockCard(@Valid @RequestBody BlockCardRequest blockCardDto,
                            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey){

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {

            return (String) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.requestCardBlock(blockCardDto, idempotencyKey);
    }

    /**
     * Запрос пополнения баланса карты
     * @param replenishmentCardDto dto, включаяет номер карты и сумму
     * @param idempotencyKey
     * @return dto транзакции
     */
    @Operation(summary = "Пополнить баланс карты", description = "В ответе ничего не возвращается.")
    @Tag(name = "put", description = "Card API")
    @PutMapping("/replenishment")
    public TransactionResponse replenishmentCard(@Valid @RequestBody ReplenishmentCardRequest replenishmentCardDto,
                                    @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey){

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {

            return (TransactionResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return cardFunctionService.cardReplenishment(replenishmentCardDto, idempotencyKey);
    }

}
