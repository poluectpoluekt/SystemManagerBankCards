package com.ed.sysbankcards.model.dto.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class TransactionResponse {

    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String statusTransaction;

}
