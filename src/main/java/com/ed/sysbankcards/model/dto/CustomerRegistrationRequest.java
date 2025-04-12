package com.ed.sysbankcards.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CustomerRegistrationRequest {

    @NotNull(message = "Name should not be empty.")
    @Size(min = 6, max = 50, message = "FirstName should between 6 and 50 characters.")
    private String Name;

    @Email
    @NotNull(message = "Email should not be empty.")
    @Size(min = 6, max = 50, message = "Email should between 6 and 50 characters.")
    private String email;

    @NotNull(message = "Password should not be empty.")
    @Size(min = 6, max = 50, message = "Email should between 6 and 50 characters.")
    private String password;
}
