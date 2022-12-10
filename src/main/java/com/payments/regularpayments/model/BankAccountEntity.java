package com.payments.regularpayments.model;

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
    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    public BankAccountEntity() {
    }

    public BankAccountEntity(BigDecimal sumOfMoney, PersonEntity personEntity) {
        this.sumOfMoney = sumOfMoney;
        this.personEntity = personEntity;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getSumOfMoney() {
        return sumOfMoney;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankAccountEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sumOfMoney=" + sumOfMoney)
                .add("personEntity=" + personEntity)
                .toString();
    }
}
