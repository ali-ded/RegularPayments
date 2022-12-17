package com.payments.regularpayments.mapper;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.model.JournalEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PaymentMapper.class)
public interface JournalEntryMapper {
    @Mapping(source = "paymentEntity", target = "paymentDto", qualifiedByName = "paymentEntityToPaymentDto")
    JournalEntryDto journalEntryEntityToJournalEntryDto(JournalEntryEntity journalEntryEntity);
}
