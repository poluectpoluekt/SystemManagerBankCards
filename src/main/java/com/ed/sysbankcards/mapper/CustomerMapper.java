package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.CustomerRegistrationRequest;
import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.model.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerRegistrationRequest customerRegistrationRequest);

    CustomerRegistrationResponse toCustomerRegistrationResponse(Customer customer);
}
