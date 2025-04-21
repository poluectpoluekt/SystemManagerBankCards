package com.ed.sysbankcards.mapper;

import com.ed.sysbankcards.model.dto.transaction.TransactionResponse;
import com.ed.sysbankcards.model.entity.operations.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "statusTransaction", source = "transaction.transactionStatus")
    TransactionResponse toTransactionResponse(Transaction transaction);
}
