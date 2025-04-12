package com.ed.sysbankcards.model.dto.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class SetLimitsForCardRequest {

    @NotNull
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal dailyLimit;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monthlyLimit;

}
