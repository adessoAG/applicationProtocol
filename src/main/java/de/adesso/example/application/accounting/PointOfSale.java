package de.adesso.example.application.accounting;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.RequiredParameter;

@Service
public class PointOfSale {

	public ApplicationProtocol<Money> encash(@RequiredParameter final ShoppingCart cart,
			final ApplicationProtocol<Money> state) {
		return null;
	}

}