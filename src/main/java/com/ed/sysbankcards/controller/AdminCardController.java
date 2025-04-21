package com.ed.sysbankcards.controller;

import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.service.AdminCardFunction;
import com.ed.sysbankcards.service.IdempotencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/api/admin/cards")
@RestController
public class AdminCardController {

    private final AdminCardFunction adminCardFunction;
    private final IdempotencyService idempotencyService;

    /**
     * Запрос создания карты
     * @param request - dto с параметрами карты
     * @param idempotencyKey
     * @return dto с параметрами созданной карты
     */
    @Operation(summary = "Создать карту", description = "В ответе возвращается dto карты")
    @Tag(name = "admin", description = "Card API")
    @PostMapping("/create")
    public CardResponse createCard(@Valid @RequestBody CreateCardRequest request,
                                   @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {

            return (CardResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return adminCardFunction.createCard(request, idempotencyKey);
    }


    /**
     * Запрос обновления данных карты
     * @param request dto с новыми параметрами карты
     * @param idempotencyKey
     * @return dto с параметрами обновленной карты
     */
    @Operation(summary = "Изменение номера и срока действия карты", description = "В ответе возвращается dto карты")
    @Tag(name = "admin", description = "Card API")
    @PutMapping("/update")
    public CardResponse updateCard(@Valid @RequestBody UpdateCardRequest request,
                                   @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {

            return (CardResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return adminCardFunction.updateCard(request);
    }


    /**
     * Запрос удаления карты
     * @param request dto - с номером карты
     * @param idempotencyKey
     * @return строка с ответом
     */
    @Operation(summary = "Удаление карты", description = "В ответе ничего не возвращается.")
    @Tag(name = "delete", description = "Card API")
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String deleteCard(@Valid @RequestBody DeleteCardRequest request,
                           @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (String) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }

        return adminCardFunction.deleteCard(request, idempotencyKey);
    }


    /**
     * Запрос добавления лимита для карты
     * @param request dto с параметрами лимита
     * @param idempotencyKey
     * @return строка с ответом
     */
    @Operation(summary = "Уставновить лимиты для карты", description = "В ответе ничего не возвращается.")
    @Tag(name = "admin", description = "Card API")
    @PutMapping("/limit")
    public String setCardLimit(@Valid @RequestBody SetLimitsForCardRequest request,
                             @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {
        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (String) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return adminCardFunction.setCardLimit(request, idempotencyKey);
    }

    /**
     * Запрос изменения статуса карты, на активный.
     * @param request dto - с номером карты
     * @param idempotencyKey
     * @return строка с ответом
     */
    @Operation(summary = "Активировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "admin", description = "Card API")
    @PostMapping("/activate")
    public String activateCard(@Valid @RequestBody ActivateCardRequest request,
                             @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (String) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return adminCardFunction.activateCard(request, idempotencyKey);
    }

    /**
     * Запрос изменения статуса карты, на заблокированный.
     * @param request dto - с номером карты
     * @param idempotencyKey
     * @return строка с ответом
     */
    @Operation(summary = "Заблокировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "admin", description = "Card API")
    @PostMapping("/blocked")
    public String blockCard(@Valid @RequestBody BlockCardRequest request,
                          @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey) {

        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (String) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return adminCardFunction.blockCard(request, idempotencyKey);
    }
}
