package com.payments.regularpayments.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "journal_entry")
public class JournalEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;
    @Column(name = "payment_result")
    private Boolean paymentResult;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    public JournalEntryEntity() {
    }

    public JournalEntryEntity(PaymentEntity paymentEntity, Boolean paymentResult) {
        this.paymentEntity = paymentEntity;
        this.paymentResult = paymentResult;
    }

    public Long getId() {
        return id;
    }

    public PaymentEntity getPaymentEntity() {
        return paymentEntity;
    }

    public Boolean getPaymentResult() {
        return paymentResult;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JournalEntryEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("paymentEntity=" + paymentEntity)
                .add("paymentResult=" + paymentResult)
                .add("paymentDate=" + paymentDate)
                .toString();
    }
}