package com.ed.sysbankcards.model.entity.operations;

import com.ed.sysbankcards.model.entity.BaseEntity;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.enums.TransactionStatus;
import com.ed.sysbankcards.model.enums.TransactionType;
import com.ed.sysbankcards.model.enums.converter.TransactionStatusConverter;
import com.ed.sysbankcards.model.enums.converter.TransactionTypeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_transaction")
    @SequenceGenerator(name = "sequence_transaction", sequenceName = "transaction_main_sequence", allocationSize = 1)
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
    @JoinColumn(name = "source_card_id", referencedColumnName = "id")
    private Card sourceCard;

    @ManyToOne
    @JoinColumn(name = "target_card_id")
    private Card targetCard;
}
