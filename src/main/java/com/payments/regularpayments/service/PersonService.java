package com.payments.regularpayments.service;

import com.payments.regularpayments.exception.PersonInnAlreadyExistsException;
import com.payments.regularpayments.exception.PersonNotFoundException;
import com.payments.regularpayments.exception.PersonPhoneNumberAlreadyExistsException;
import com.payments.regularpayments.mapper.PersonMapper;
import com.payments.regularpayments.dto.PersonDto;
import com.payments.regularpayments.model.PersonEntity;
import com.payments.regularpayments.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonEntity save(PersonDto personDto) throws PersonInnAlreadyExistsException, PersonPhoneNumberAlreadyExistsException {
        LOGGER.info("New person registration");
        if (personRepository.existsByInn(Long.valueOf(personDto.getInn()))) {
            throw new PersonInnAlreadyExistsException("Клиент банка с таким ИНН уже зарегистрирован");
        }
        if (personRepository.existsByPhoneNumber(personDto.getPhoneNumber())) {
            throw new PersonPhoneNumberAlreadyExistsException("Клиент банка с таким номером телефона уже зарегистрирован");
        }
        return personRepository.save(PersonMapper.INSTANCE.personDtoToPersonEntity(personDto));
    }

    public PersonEntity update(PersonDto personDto, Long id) throws PersonNotFoundException {
        LOGGER.info("Update person data by ID {}", id);
        if (personRepository.existsById(id)) {
            PersonEntity personEntity = PersonMapper.INSTANCE.personDtoToPersonEntity(personDto);
            personEntity.setId(id);
            return personRepository.save(personEntity);
        } else {
            throw new PersonNotFoundException("Невозможно обновить данные. По указанному идентификатору клиент банка не найден");
        }
    }

    public PersonEntity findById(Long id) throws PersonNotFoundException {
        LOGGER.info("Person search by ID {}", id);
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("По заданному идентификатору клиент банка не найден"));
    }

    public List<PersonEntity> findAll() {
        LOGGER.info("Search all persons");
        return (List<PersonEntity>) personRepository.findAll();
    }

    public void deleteById(Long id) throws PersonNotFoundException {
        LOGGER.info("Deleting a person by ID {}", id);
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        } else {
            throw new PersonNotFoundException("Невозможно удалить данные. По указанному идентификатору клиент банка не найден");
        }
    }
}
