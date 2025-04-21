package com.ed.sysbankcards.repository;

import com.ed.sysbankcards.model.entity.Limit;
import com.ed.sysbankcards.model.enums.LimitType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LimitRepository extends CrudRepository<Limit, Long> {

    Optional<Limit> findByCardId(Long cardId);

}
