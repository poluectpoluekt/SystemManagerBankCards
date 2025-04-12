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

    @Transactional
    public CustomerRegistrationResponse registerCustomer(CustomerRegistrationRequest customerDto) {
        if(customerRepository.findByEmail(customerDto.getEmail()).isPresent()) {
            throw new CustomerAlreadyRegisteredException(customerDto.getEmail());
        }

        Customer customer = customerMapper.toCustomer(customerDto);
        customer.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER").get()));
        customer.setAccountNonExpired(true);
        customer.setAccountNonLocked(true);
        customer.setCredentialsNonExpired(true);
        customer.setEnabled(true);
        return customerMapper.toCustomerRegistrationResponse(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }


}
