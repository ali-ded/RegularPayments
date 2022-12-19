package com.payments.regularpayments.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account")
    @JsonManagedReference
    private BankAccountEntity creditAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account")
    @JsonManagedReference
    private BankAccountEntity debitAccount;
    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;
    @Column(name = "write_off_period")
    private Long writeOffPeriod; // Период списания в минутах
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "paymentEntity", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<JournalEntryEntity> journalEntryEntities;

    public PaymentEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreditAccount(BankAccountEntity creditAccount) {
        this.creditAccount = creditAccount;
    }

    public void setDebitAccount(BankAccountEntity debitAccount) {
        this.debitAccount = debitAccount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setWriteOffPeriod(Long writeOffPeriod) {
        this.writeOffPeriod = writeOffPeriod;
    }

    public Long getId() {
        return id;
    }

    public BankAccountEntity getCreditAccount() {
        return creditAccount;
    }

    public BankAccountEntity getDebitAccount() {
        return debitAccount;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Long getWriteOffPeriod() {
        return writeOffPeriod;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("creditAccount=" + creditAccount)
                .add("debitAccount=" + debitAccount)
                .add("transactionAmount=" + transactionAmount)
                .add("writeOffPeriod=" + writeOffPeriod)
                .add("journalEntryEntities=" + journalEntryEntities)
                .toString();
    }
}
