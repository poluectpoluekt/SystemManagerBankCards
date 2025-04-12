package com.ed.sysbankcards.controller;

import com.ed.sysbankcards.model.dto.CustomerRegistrationRequest;
import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;


    @Operation(summary = "Зарегистрировать нового пользователя", description = "В ответе возвращается dto.")
    @Tag(name = "post", description = "Customer API")
    @PostMapping("/registration")
    public CustomerRegistrationResponse customerRegistration(@RequestBody CustomerRegistrationRequest customerReqDto){
        return customerService.registerCustomer(customerReqDto);
    }
}
