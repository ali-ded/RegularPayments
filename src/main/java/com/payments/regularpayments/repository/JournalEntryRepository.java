package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.JournalEntryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalEntryRepository extends CrudRepository<JournalEntryEntity, Long> {
    @Modifying
    @Transactional
    @Query("update JournalEntryEntity " +
            "set paymentResult = false, paymentDate = :dateOfStorno " +
            "where id = :id")
    int storno(@Param("id") long id, @Param("dateOfStorno") LocalDateTime dateOfStorno);

    @Query("select journalEntry " +
            "from JournalEntryEntity journalEntry " +
            "where journalEntry.paymentEntity.id = :id " +
            "order by journalEntry.paymentDate asc")
    List<JournalEntryEntity> findJournalEntryEntitiesByPaymentId(@Param("id") long paymentId);

    @Query("select journalEntry " +
            "from JournalEntryEntity journalEntry " +
            "where journalEntry.paymentEntity.id = :id and journalEntry.paymentResult = true " +
            "order by journalEntry.paymentDate desc")
    Page<JournalEntryEntity> getLastWriteOff(@Param("id") long paymentId, Pageable pageable);
}
