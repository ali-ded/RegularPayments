package com.payments.regularpayments.model.dto;


import javax.validation.constraints.*;
import java.util.StringJoiner;

public class PersonDto {
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    @NotBlank(message = "Имя обязательно должно быть указано")
    private final String name;
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    @NotBlank(message = "Фамилия обязательно должна быть указана")
    private final String surname;
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    private final String patronymic;
    @Pattern(regexp = "^[+]?\\d{3}?[-\\s]?\\d{2}[-\\s]?\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}$",
            message = "Номер телефона должен быть указан в международном формате. Допустимые разделители: ' ', ','")
    @NotNull(message = "Номер телефона обязательно должен быть указан")
    private final String phoneNumber;
    @Pattern(regexp = "^((?!(0))[0-9]{12})$", message = "ИНН должен содержать 12 цифр и не начинаться с 0")
    @NotNull(message = "ИНН обязательно должен быть указан")
    private final String inn;

    public PersonDto(String name, String surname, String patronymic, String phoneNumber, String inn) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.inn = inn;
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

    public String getInn() {
        return inn;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PersonDto.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("surname='" + surname + "'")
                .add("patronymic='" + patronymic + "'")
                .add("phoneNumber='" + phoneNumber + "'")
                .add("inn=" + inn)
                .toString();
    }
}
