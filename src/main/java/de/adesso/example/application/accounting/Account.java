package de.adesso.example.application.accounting;

import java.util.UUID;

import org.javamoney.moneta.Money;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class Account {
	private UUID id;
	private String name;
	Money amount;
}
