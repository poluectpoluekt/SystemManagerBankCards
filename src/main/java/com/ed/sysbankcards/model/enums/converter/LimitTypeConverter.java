package com.ed.sysbankcards.model.enums.converter;

import com.ed.sysbankcards.model.enums.CardStatus;
import com.ed.sysbankcards.model.enums.LimitType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LimitTypeConverter implements AttributeConverter<LimitType, String> {
    @Override
    public String convertToDatabaseColumn(LimitType attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public LimitType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LimitType.fromString(dbData);
    }
}
