package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.customer.CustomerAlreadyRegisteredException;
import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import com.ed.sysbankcards.mapper.CustomerMapper;
import com.ed.sysbankcards.model.dto.CustomerRegistrationRequest;
import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.model.entity.Customer;
import com.ed.sysbankcards.repository.CustomerRepository;
import com.ed.sysbankcards.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RoleRepository roleRepository;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final IdempotencyService idempotencyService;
    private final long TIME_LIFE_RECORD_DB = 3600;

    /**
     * Метод создание пользователя
     * @return dto зарегистрированного пользователя
     */
    @Transactional
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest customerDto, String idempotencyKey) {
        if(customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
            log.error("Customer with email {} already exists", customerDto.getEmail());
            throw new CustomerAlreadyRegisteredException(customerDto.getEmail());
        }

        Customer customer = new Customer();
        customer.setEmail(customerDto.getEmail());
        customer.setPassword(argon2PasswordEncoder.encode(customerDto.getPassword()));
        customer.setName(customerDto.getName());
        customer.setRoles(Collections.singleton(roleRepository.findByName("USER").get()));
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        CustomerRegistrationResponse response = customerMapper
                .toCustomerRegistrationResponse(customerRepository.save(customer));
        idempotencyService.saveIdempotencyKey(idempotencyKey, response, TIME_LIFE_RECORD_DB);
        return response;
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

}
