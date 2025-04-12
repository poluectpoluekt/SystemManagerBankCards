package com.ed.sysbankcards.repository;

import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceCard(Card card);

    List<Transaction> findBySourceCard(Card card, Pageable pageable);



}
