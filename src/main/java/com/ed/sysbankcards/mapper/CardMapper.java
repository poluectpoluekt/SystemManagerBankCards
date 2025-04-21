package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.card.CardResponse;
import com.ed.sysbankcards.model.dto.card.CreateCardRequest;
import com.ed.sysbankcards.model.entity.Card;
import com.ed.sysbankcards.model.enums.converter.CardStatusConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "cardHolder", source = "card.customer.name")
    @Mapping(target = "status", source = "card.status")
    CardResponse toCardResponse(Card card);
}
