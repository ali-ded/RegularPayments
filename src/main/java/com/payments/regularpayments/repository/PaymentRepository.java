package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.PaymentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, Long> {
    @Query("select paymentEntity " +
            "from PaymentEntity paymentEntity " +
            "where paymentEntity.creditAccount in " +
            "(select bankAccountEntity.id " +
            "from BankAccountEntity bankAccountEntity " +
            "join PersonEntity personEntity on bankAccountEntity.personEntity.id = personEntity.id " +
            "where personEntity.inn = :inn)")
    List<PaymentEntity> findPaymentEntitiesByPayerInn(@Param("inn") long inn);
}
