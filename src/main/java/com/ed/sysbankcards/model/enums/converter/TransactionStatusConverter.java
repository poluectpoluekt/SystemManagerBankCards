package com.ed.sysbankcards.model.enums.converter;

import com.ed.sysbankcards.model.enums.TransactionStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, String> {

    @Override
    public String convertToDatabaseColumn(TransactionStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TransactionStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TransactionStatus.fromString(dbData);
    }
}
