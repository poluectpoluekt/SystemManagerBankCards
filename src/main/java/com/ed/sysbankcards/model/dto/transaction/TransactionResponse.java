package com.ed.sysbankcards.model.dto.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class TransactionResponse {

    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String statusTransaction;

}
