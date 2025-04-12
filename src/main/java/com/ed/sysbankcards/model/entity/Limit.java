package com.ed.sysbankcards.model.entity;

import com.ed.sysbankcards.model.enums.LimitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class Limit extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_limit")
    @SequenceGenerator(name = "sequence_limit", sequenceName = "limit_main_sequence", allocationSize = 1)
    private Long id;

    private BigDecimal dailyLimit;
    private BigDecimal monthlyLimit;

    @OneToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
}
