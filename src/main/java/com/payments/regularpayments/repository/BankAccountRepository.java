package com.payments.regularpayments.repository;

import com.payments.regularpayments.model.BankAccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long> {
}
