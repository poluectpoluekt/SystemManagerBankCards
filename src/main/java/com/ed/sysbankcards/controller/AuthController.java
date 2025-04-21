package com.ed.sysbankcards.controller;

import com.ed.sysbankcards.model.dto.AuthRequest;
import com.ed.sysbankcards.model.dto.AuthResponse;
import com.ed.sysbankcards.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Залогиниться пользователю", description = "В ответе возвращается dto JWT токена.")
    @Tag(name = "auth", description = "Customer")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody final AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
}
