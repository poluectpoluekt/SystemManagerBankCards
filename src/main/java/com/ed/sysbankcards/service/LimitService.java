package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.card.CardWithNumberNoExistsException;
import com.ed.sysbankcards.model.dto.card.SetLimitsForCardRequest;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.entity.Limit;
import com.ed.sysbankcards.repository.CardRepository;
import com.ed.sysbankcards.repository.LimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void setCardLimit(SetLimitsForCardRequest limitDto) {
        Card card = cardRepository.findByCardNumber(limitDto.getCardNumber())
                .orElseThrow(()-> new CardWithNumberNoExistsException(limitDto.getCardNumber()));

        Limit limit = card.getLimit() != null ? card.getLimit() : new Limit();
        limit.setDailyLimit(limitDto.getDailyLimit());
        limit.setMonthlyLimit(limitDto.getMonthlyLimit());
        limitRepository.save(limit);
    }
}
