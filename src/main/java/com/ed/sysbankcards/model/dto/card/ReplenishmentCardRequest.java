package com.ed.sysbankcards.model.dto.card;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
public class ReplenishmentCardRequest {

    private String cardNumber;
    private BigDecimal amount;
}
