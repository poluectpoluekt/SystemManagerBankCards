package com.ed.sysbankcards.controller;

import com.ed.sysbankcards.model.dto.AuthRequest;
import com.ed.sysbankcards.model.dto.AuthResponse;
import com.ed.sysbankcards.model.dto.CustomerRegistrationRequest;
import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.service.AuthService;
import com.ed.sysbankcards.service.CustomerService;
import com.ed.sysbankcards.service.IdempotencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RequestMapping("/api/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final AuthService authService;
    private final IdempotencyService idempotencyService;

    /**
     * Запрос регистрации нового пользователя
     * @param customerReqDto dto с параметрами нового пользователя
     * @param idempotencyKey
     * @return dto с параметрами созданного пользователя
     */
    @Operation(summary = "Зарегистрировать нового пользователя", description = "В ответе возвращается dto.")
    @Tag(name = "sign up", description = "Customer")
    @PostMapping("/registration")
    public CustomerRegistrationResponse customerRegistration(@Valid @RequestBody CustomerRegistrationRequest customerReqDto,
                                                             @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey){
        if (idempotencyService.idempotencyKeyCheck(idempotencyKey)) {
            return (CustomerRegistrationResponse) idempotencyService.getResultByIdempotencyKey(idempotencyKey);
        }
        return customerService.registerCustomer(customerReqDto, idempotencyKey);
    }

}
