package de.adesso.example.application.accounting;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.RequiredParameter;

@Service
public class PointOfSale {

	public ApplicationProtocol<BigDecimal> encash(@RequiredParameter final ShoppingCart cart,
			final ApplicationProtocol<BigDecimal> state) {
		return null;
	}

}
