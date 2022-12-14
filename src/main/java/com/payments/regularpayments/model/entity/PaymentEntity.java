package com.payments.regularpayments.model.entity;

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
    @ManyToOne
    @JoinColumn(name = "credit_account")
    @JsonManagedReference
    private BankAccountEntity creditAccount;
    @ManyToOne
    @JoinColumn(name = "debit_account")
    @JsonManagedReference
    private BankAccountEntity debitAccount;
    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "paymentEntity", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<JournalEntryEntity> journalEntryEntities;

    public PaymentEntity() {
    }

    public PaymentEntity(BankAccountEntity creditAccount, BankAccountEntity debitAccount, BigDecimal transactionAmount) {
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
        this.transactionAmount = transactionAmount;
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

    public void setJournalEntryEntities(Set<JournalEntryEntity> journalEntryEntities) {
        this.journalEntryEntities = journalEntryEntities;
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

    public Set<JournalEntryEntity> getJournalEntryEntities() {
        return journalEntryEntities;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaymentEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("creditAccount=" + creditAccount)
                .add("debitAccount=" + debitAccount)
                .add("transactionAmount=" + transactionAmount)
                .add("journalEntryEntities=" + journalEntryEntities)
                .toString();
    }
}
