package com.payments.regularpayments.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.StringJoiner;

@Entity
@Table(name = "bank_account")
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sum_of_money")
    private BigDecimal sumOfMoney;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonManagedReference
    private PersonEntity personEntity;

    public BankAccountEntity() {
    }

    public void debit(BigDecimal amount) {
        sumOfMoney = sumOfMoney.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        sumOfMoney = sumOfMoney.add(amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(BigDecimal sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankAccountEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sumOfMoney=" + sumOfMoney)
                .toString();
    }
}
