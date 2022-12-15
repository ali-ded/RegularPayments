package com.payments.regularpayments.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.StringJoiner;

public class PaymentDto extends PaymentCreateDto {
    @NotNull(message = "идентификатор не можеть быть null")
    @Positive(message = "идентификатор должен быть больше 0")
    private Long id;

    public PaymentDto(Long id, Long creditAccount, Long debitAccount, BigDecimal transactionAmount) {
        super(creditAccount, debitAccount, transactionAmount);
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("creditAccount=" + getCreditAccount())
                .add("debitAccount=" + getDebitAccount())
                .add("transactionAmount=" + getTransactionAmount())
                .toString();
    }
}
