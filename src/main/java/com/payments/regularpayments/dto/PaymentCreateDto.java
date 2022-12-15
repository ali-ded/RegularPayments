package com.payments.regularpayments.dto;

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

    private WriteOffPeriod writeOffPeriod;

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

    public void setWriteOffPeriod(WriteOffPeriod writeOffPeriod) {
        this.writeOffPeriod = writeOffPeriod;
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

    public WriteOffPeriod getWriteOffPeriod() {
        return writeOffPeriod;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentCreateDto.class.getSimpleName() + "[", "]")
                .add("creditAccount=" + creditAccount)
                .add("debitAccount=" + debitAccount)
                .add("transactionAmount=" + transactionAmount)
                .toString();
    }

    public static class WriteOffPeriod {
        private Long days = 0L;
        private Long hours = 0L;
        private Long minutes = 0L;

        public WriteOffPeriod(Long days, Long hours, Long minutes) {
            if (days != null) {
                this.days = days;
            }
            if (hours != null) {
                this.hours = hours;
            }
            if (minutes != null) {
                this.minutes = minutes;
            }
        }

        public Long getDays() {
            return days;
        }

        public void setDays(Long days) {
            this.days = days;
        }

        public Long getHours() {
            return hours;
        }

        public void setHours(Long hours) {
            this.hours = hours;
        }

        public Long getMinutes() {
            return minutes;
        }

        public void setMinutes(Long minutes) {
            this.minutes = minutes;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", WriteOffPeriod.class.getSimpleName() + "[", "]")
                    .add("days=" + days)
                    .add("hours=" + hours)
                    .add("minutes=" + minutes)
                    .toString();
        }
    }
}
