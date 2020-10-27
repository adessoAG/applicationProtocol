package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.application.accounting.PointOfSale;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;

@Emulated
public interface Cashier {

	@Implementation(implementations = { PointOfSale.class })
	ApplicationProtocol<BigDecimal> encash(ShoppingCart cart, ApplicationProtocol<BigDecimal> state);
}
