package com.ed.sysbankcards.controller;

import com.ed.sysbankcards.model.dto.card.*;
import com.ed.sysbankcards.service.AdminCardFunction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("api/admin/cards")
@RestController
public class AdminCardController {

    private final AdminCardFunction adminCardFunction;

    @Operation(summary = "Создать карты", description = "В ответе возвращается dto карты")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/create")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CardResponse createCard(@Valid @RequestBody CreateCardRequest request) {
        //Long customerId = getCustomerIdFromAuth(authentication);

        return adminCardFunction.createCard(request);
    }

    @Operation(summary = "Изменение номера и срока действия карты", description = "В ответе возвращается dto карты")
    @Tag(name = "put", description = "Card API")
    @PutMapping("/update")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CardResponse updateCard(@Valid @RequestBody UpdateCardRequest request) {
        //Long customerId = getCustomerIdFromAuth(authentication);
        return adminCardFunction.updateCard(request);
    }

    @Operation(summary = "Удаление карты", description = "В ответе ничего не возвращается.")
    @Tag(name = "delete", description = "Card API")
    @DeleteMapping("/delete")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void deleteCard(DeleteCardRequest request) {
        //Long customerId = getCustomerIdFromAuth(authentication);

        adminCardFunction.deleteCard(request);
    }

    @Operation(summary = "Уставновить лимиты для карты", description = "В ответе ничего не возвращается.")
    @Tag(name = "put", description = "Card API")
    @PutMapping("/limit")
    //@PreAuthorize("hasRole('ADMIN')")
    public void setCardLimit(@Valid @RequestBody SetLimitsForCardRequest request) {
        adminCardFunction.setCardLimit(request);
    }

    @Operation(summary = "Активировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/activate")
    public void activateCard(@Valid @RequestBody ActivateCardRequest request) {
        adminCardFunction.activateCard(request);
    }

    @Operation(summary = "Заблокировать карту", description = "В ответе ничего не возвращается.")
    @Tag(name = "post", description = "Card API")
    @PostMapping("/blocked")
    public void blockCard(@Valid @RequestBody BlockCardRequest request) {
        adminCardFunction.blockCard(request);
    }
}
