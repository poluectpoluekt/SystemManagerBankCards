package com.ed.sysbankcards.model.dto.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReplenishmentCardRequest {

    private String cardNumber;
    private BigDecimal amount;
}
