package com.ed.sysbankcards.repository;

import com.ed.sysbankcards.model.entity.Limit;
import com.ed.sysbankcards.model.enums.LimitType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends CrudRepository<Limit, Long> {

    Optional<Limit> findByCardIdAndPeriodLimit(Long cardId, LimitType periodLimit);

    Optional<Limit> findByCardId(Long cardId);

//    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.card.id = :cardId AND " +
//            "t.type = 'WITHDRAWAL' AND MONTH(t.timestamp) = MONTH(CURRENT_DATE)")
//    BigDecimal getMonthlyWithdrawalSum(@Param("cardId") Long cardId);
//
//    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.card.id = :cardId AND " +
//            "t.type = 'WITHDRAWAL' AND DATE(t.timestamp) = CURRENT_DATE")
//    BigDecimal getDailyWithdrawalSum(@Param("cardId") Long cardId);
}
