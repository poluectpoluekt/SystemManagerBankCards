package com.ed.sysbankcards.exception.card;

import com.ed.sysbankcards.model.enums.LimitType;

import java.math.BigDecimal;

public class LimitExhaustedException extends RuntimeException{
    public LimitExhaustedException(BigDecimal amountLimit, LimitType type, BigDecimal allowedAmount){
        super(String.format("The limit of %s is exhausted, current limit is - %s. Allowed withdrawal amount - %s",
                type, amountLimit, allowedAmount));

    }
}
