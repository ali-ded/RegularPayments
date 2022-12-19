package com.payments.regularpayments.dto;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class JournalEntryDto {
    private Long id;
    private PaymentDto paymentDto;
    private Boolean paymentResult;
    private LocalDateTime paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentDto getPaymentDto() {
        return paymentDto;
    }

    public void setPaymentDto(PaymentDto paymentDto) {
        this.paymentDto = paymentDto;
    }

    public Boolean getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(Boolean paymentResult) {
        this.paymentResult = paymentResult;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JournalEntryDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("paymentDto=" + paymentDto)
                .add("paymentResult=" + paymentResult)
                .add("paymentDate=" + paymentDate)
                .toString();
    }
}