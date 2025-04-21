package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import com.ed.sysbankcards.model.application_class.CustomerDetails;
import com.ed.sysbankcards.model.dto.AuthRequest;
import com.ed.sysbankcards.model.dto.AuthResponse;
import com.ed.sysbankcards.model.entity.Customer;
import com.ed.sysbankcards.repository.CustomerRepository;
import com.ed.sysbankcards.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomerNotFoundException(request.email()));

        CustomerDetails customerDetails = new CustomerDetails(customer);

        String token = "jwt-token: " + jwtUtil.generateToken(customerDetails);
        return new AuthResponse(token);
    }
}
