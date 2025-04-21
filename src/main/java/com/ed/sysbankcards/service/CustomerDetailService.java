package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.customer.CustomerNotFoundException;
import com.ed.sysbankcards.model.application_class.CustomerDetails;
import com.ed.sysbankcards.model.entity.Customer;
import com.ed.sysbankcards.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(()-> new CustomerNotFoundException(username));

        return new CustomerDetails(customer);
    }
}
