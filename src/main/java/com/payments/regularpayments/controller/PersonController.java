package com.payments.regularpayments.controller;

import com.payments.regularpayments.exception.FieldErrorException;
import com.payments.regularpayments.exception.PersonInnAlreadyExistsException;
import com.payments.regularpayments.exception.PersonNotFoundException;
import com.payments.regularpayments.exception.PersonPhoneNumberAlreadyExistsException;
import com.payments.regularpayments.model.dto.PersonDto;
import com.payments.regularpayments.model.entity.PersonEntity;
import com.payments.regularpayments.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonController {
    private final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/get-all")
    @Operation(summary = "Получить список всех клиентов банка",
            description = "Возвращает список всех зарегистрированных клиентов банка")
    public ResponseEntity<List<PersonEntity>> getAllPersons() {
        LOGGER.info("GET /get-all");
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/get")
    @Operation(summary = "Получить регистрационные данные о клиенте банка по идентификатору",
            description = "Возвращает информацию о клиенте банка, зарегистрированному по заданному идентификатору")
    public ResponseEntity<PersonEntity> getPerson(@RequestParam("id") long id) throws PersonNotFoundException {
        LOGGER.info("GET /get?id={}", id);
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping("/register")
    @Operation(summary = "Зарегистрировать нового клиента банка",
            description = "В случае успешного сохранения метод возвращает сущность сохраненного в БД клиента")
    public ResponseEntity<PersonEntity> registerPerson(@RequestBody @Valid PersonDto personDto,
                                                       BindingResult bindingResult) throws
            PersonInnAlreadyExistsException, FieldErrorException, PersonPhoneNumberAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            LOGGER.info("POST /register: {}", HttpStatus.BAD_REQUEST);
            throw new FieldErrorException(bindingResult.getFieldErrors(), "Объект имеет невалидные поля");
        }

        PersonEntity personEntitySaved;
        try {
            personEntitySaved = personService.save(personDto);
        } catch (PersonInnAlreadyExistsException | PersonPhoneNumberAlreadyExistsException e) {
            LOGGER.info("POST /register: {}", HttpStatus.CONFLICT);
            throw e;
        }

        LOGGER.info("POST /register: {}", HttpStatus.OK);
        return ResponseEntity.ok(personEntitySaved);
    }

    @PutMapping("/edit")
    @Operation(summary = "Изменить регистрационные данные клиента банка",
            description = "Возвращает сущность клиента банка с измененными данными")
    public ResponseEntity<PersonEntity> editPerson(@RequestBody @Valid PersonDto personDto,
                                                   BindingResult bindingResult,
                                                   @RequestParam("id") @Min(value = 1) long id) throws
            FieldErrorException, PersonNotFoundException {
        if (bindingResult.hasErrors()) {
            LOGGER.info("PUT /edit?id={}: {}", id, HttpStatus.BAD_REQUEST);
            throw new FieldErrorException(bindingResult.getFieldErrors(), "Объект имеет невалидные поля");
        }

        PersonEntity personEntityEdited;
        try {
            personEntityEdited = personService.update(personDto, id);
        } catch (PersonNotFoundException e) {
            LOGGER.info("PUT /edit?id={}: {}", id, HttpStatus.NOT_FOUND);
            throw e;
        }

        LOGGER.info("PUT /edit?id={}: {}", id, HttpStatus.OK);
        return ResponseEntity.ok(personEntityEdited);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удалить данные о клиенте банка по заданному идентификатору",
            description = "В случае успеха ничего не возвращает, HTTP ответ 200 OK. " +
                    "Если по идентификатору данные клиента не найдены - HTTP ответ 404 Not Found")
    public ResponseEntity<Void> deletePerson(@RequestParam("id") @Min(value = 1) long id) throws PersonNotFoundException {
        LOGGER.info("DELETE /delete?id={}", id);
        personService.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
