package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toTransactionResponse(Transaction transaction);
}
