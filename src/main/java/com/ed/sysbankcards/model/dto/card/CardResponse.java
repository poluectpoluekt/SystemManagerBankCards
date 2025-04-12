package com.ed.sysbankcards.model.dto.card;

import com.ed.sysbankcards.model.entity.operations.Transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class CardResponse {

    private String cardNumber;
    private String cardHolder;
    private LocalDate expiryDate;
    private String status;
    private BigDecimal balance;
    private String currency;
}
