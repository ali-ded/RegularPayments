package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.JournalEntryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends CrudRepository<JournalEntryEntity, Long> {
}
