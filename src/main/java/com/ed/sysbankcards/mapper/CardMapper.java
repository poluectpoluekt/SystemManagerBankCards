package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.card.CardResponse;
import com.ed.sysbankcards.model.dto.card.CreateCardRequest;
import com.ed.sysbankcards.model.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toCard(CreateCardRequest createCardRequest);

    CardResponse toCardResponse(Card card);
}
