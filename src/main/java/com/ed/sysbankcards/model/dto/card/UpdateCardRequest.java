package com.ed.sysbankcards.model.dto.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
public class UpdateCardRequest {

    @NotNull
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @NotNull
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String newCardNumber;


    @Pattern(regexp = "\\d{16}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\\\d|3[01])$", message = "Date must be in format yyyy-MM-dd")
    private String newExpiryDate;
}
