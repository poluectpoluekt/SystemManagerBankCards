package com.ed.sysbankcards.model.entity.operations;

import com.ed.sysbankcards.model.entity.BaseEntity;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.enums.TransactionStatus;
import com.ed.sysbankcards.model.enums.TransactionType;
import com.ed.sysbankcards.model.enums.converter.TransactionStatusConverter;
import com.ed.sysbankcards.model.enums.converter.TransactionTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    private BigDecimal amount;
    private String currency;

    @Column(name = "transaction_status")
    @Convert(converter = TransactionStatusConverter.class)
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_type")
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "sourceCardId", referencedColumnName = "id")
    private Card sourceCard;

    @ManyToOne
    @JoinColumn(name = "targetCardId")
    private Card targetCard;
}
