package com.ed.sysbankcards.repository;

import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import com.ed.sysbankcards.model.enums.CardStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);

    Page<Card> findByCustomerId(Long customerId, Pageable pageable);

    Page<Card> findByCustomerIdAndStatus(Long customerId, CardStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Card c WHERE c.cardNumber = :cardNumber")
    Optional<Card> findByCardNumberWithLock(String cardNumber);

}
