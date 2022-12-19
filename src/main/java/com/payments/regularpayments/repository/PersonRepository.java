package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {
    boolean existsByInn(Long inn);
    boolean existsByPhoneNumber(String phoneNumber);
}
