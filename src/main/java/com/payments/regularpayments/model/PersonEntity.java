package com.payments.regularpayments.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setInn(Long inn) {
        this.inn = inn;
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