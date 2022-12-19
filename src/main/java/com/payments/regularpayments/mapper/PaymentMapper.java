package com.payments.regularpayments.mapper;

import com.payments.regularpayments.dto.PaymentCreateDto;
import com.payments.regularpayments.dto.PaymentDto;
import com.payments.regularpayments.model.PaymentEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Duration;

@Mapper(componentModel = "spring", imports = {Duration.class, PaymentCreateDto.class})
public interface PaymentMapper {
    @Mapping(source = "paymentCreateDto.creditAccount", target = "creditAccount.id")
    @Mapping(source = "paymentCreateDto.debitAccount", target = "debitAccount.id")
    @Mapping(target = "writeOffPeriod",
            expression = "java(writeOffPeriodToLongMinutes(paymentCreateDto.getWriteOffPeriod()))")
    PaymentEntity paymentCreateDtoToPaymentEntity(PaymentCreateDto paymentCreateDto);

    @Mapping(source = "paymentEntity.creditAccount.id", target = "creditAccount")
    @Mapping(source = "paymentEntity.debitAccount.id", target = "debitAccount")
    @Mapping(target = "writeOffPeriod",
    expression = "java(longMinutesToWriteOffPeriod(paymentEntity.getWriteOffPeriod()))")
    @Named("paymentEntityToPaymentDto")
    PaymentDto paymentEntityToPaymentDto(PaymentEntity paymentEntity);

    @InheritInverseConfiguration
    @Mapping(target = "writeOffPeriod",
            expression = "java(writeOffPeriodToLongMinutes(paymentDto.getWriteOffPeriod()))")
    PaymentEntity paymentDtoToPaymentEntity(PaymentDto paymentDto);

    default Long writeOffPeriodToLongMinutes(PaymentCreateDto.WriteOffPeriod writeOffPeriod) {
        return Duration
                .ofDays(writeOffPeriod.getDays())
                .plusHours(writeOffPeriod.getHours())
                .plusMinutes(writeOffPeriod.getMinutes())
                .toMinutes();
    }
    default PaymentCreateDto.WriteOffPeriod longMinutesToWriteOffPeriod(Long minutes) {
        Duration duration = Duration.ofMinutes(minutes);
        return new PaymentCreateDto.WriteOffPeriod(
                duration.toDaysPart(),
                (long) duration.toHoursPart(),
                (long) duration.toMinutesPart());
    }
}
