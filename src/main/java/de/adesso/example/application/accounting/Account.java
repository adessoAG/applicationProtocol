package de.adesso.example.application.accounting;

import java.io.Serializable;
import java.util.UUID;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private final UUID id;
	Money amount;
}
