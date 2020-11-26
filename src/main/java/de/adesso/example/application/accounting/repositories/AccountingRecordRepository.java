package de.adesso.example.application.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.adesso.example.application.accounting.entities.AccountingRecordEntity;

@Repository
public interface AccountingRecordRepository extends JpaRepository<AccountingRecordEntity, String> {

}
