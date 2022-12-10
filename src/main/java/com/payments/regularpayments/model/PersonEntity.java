package com.payments.regularpayments.model;

import javax.persistence.*;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    @Column(name = "phone_number")
    private String phoneNumber;
    private Long inn;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "personEntity", fetch = FetchType.EAGER)
    private Set<BankAccountEntity> bankAccountEntities;

    public PersonEntity() {
    }

    public PersonEntity(String name, String surname, String patronymic, String phoneNumber, Long inn) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.inn = inn;
    }

    public PersonEntity setName(String name) {
        this.name = name;
        return this;
    }

    public PersonEntity setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public PersonEntity setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public PersonEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonEntity setInn(Long inn) {
        this.inn = inn;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getInn() {
        return inn;
    }

    public Set<BankAccountEntity> getBankAccountEntities() {
        return bankAccountEntities;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PersonEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("surname='" + surname + "'")
                .add("patronymic='" + patronymic + "'")
                .add("phoneNumber='" + phoneNumber + "'")
                .add("inn=" + inn)
                .add("bankAccountEntities=" + bankAccountEntities)
                .toString();
    }
}
