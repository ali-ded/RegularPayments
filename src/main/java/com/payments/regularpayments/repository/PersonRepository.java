package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Long> {
    public boolean existsByInn(Long inn);
    public boolean existsByPhoneNumber(String phoneNumber);
}
