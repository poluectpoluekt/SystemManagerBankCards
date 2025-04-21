package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.CustomerRegistrationRequest;
import com.ed.sysbankcards.model.dto.CustomerRegistrationResponse;
import com.ed.sysbankcards.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerRegistrationResponse toCustomerRegistrationResponse(Customer customer);
}
