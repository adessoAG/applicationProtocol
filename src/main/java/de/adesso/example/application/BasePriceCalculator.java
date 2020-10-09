package de.adesso.example.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.adesso.example.framework.AppendixOwner;
import de.adesso.example.framework.ApplicationFrameworkInvokable;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class BasePriceCalculator extends AppendixOwner implements ApplicationFrameworkInvokable{
	
	public BasePriceCalculator() {
		super(retrieveOwnUuid());
	}

	public ApplicationProtocol<BigDecimal> calculatePrice(Article article) {
		
		ApplicationProtocol<BigDecimal> protocol = new ApplicationProtocol<BigDecimal>();
		
		return protocol.setResult(new BigDecimal("7.85"));
	}

	private static UUID ownUuid = UUID.randomUUID();
	private static UUID retrieveOwnUuid() {
		return ownUuid;
	}
}
