package de.adesso.example.application.accounting.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class AccountingRecordEntity {

	@Id
	private String id;

}
