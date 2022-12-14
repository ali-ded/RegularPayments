package com.payments.regularpayments.model.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.StringJoiner;

public class PaymentCreateDto {
    @Min(value = 100000000000L, message = "номер счета плательщика должен содержать 12 цифр")
    @Max(value = 999999999999L, message = "номер счета плательщика должен содержать 12 цифр")
    @NotNull(message = "номер счет плательщика обязательно должен быть указан")
    private Long creditAccount;
    @Min(value = 100000000000L, message = "номер счета получателя должен содержать 12 цифр")
    @Max(value = 999999999999L, message = "номер счета получателя должен содержать 12 цифр")
    @NotNull(message = "номер счет получателя обязательно должен быть указан")
    private Long debitAccount;
    @Digits(integer = 12, fraction = 2, message = "должно быть не больше 2 десятичных знаков")
    @Positive(message = "сумма транзакции должна быть больше 0")
    @NotNull(message = "сумма транзакции обязательно должна быть указана")
    private BigDecimal transactionAmount;

    public PaymentCreateDto(Long creditAccount, Long debitAccount, BigDecimal transactionAmount) {
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.transactionAmount = transactionAmount;
    }

    public void setCreditAccount(Long creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(Long debitAccount) {
        this.debitAccount = debitAccount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Long getCreditAccount() {
        return creditAccount;
    }

    public Long getDebitAccount() {
        return debitAccount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentCreateDto.class.getSimpleName() + "[", "]")
                .add("creditAccount=" + creditAccount)
                .add("debitAccount=" + debitAccount)
                .add("transactionAmount=" + transactionAmount)
                .toString();
    }
}
