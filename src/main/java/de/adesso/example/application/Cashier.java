package de.adesso.example.application;

import org.javamoney.moneta.Money;

import de.adesso.example.application.accounting.PointOfSale;
import de.adesso.example.application.shopping.ShoppingCart;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.Emulated;
import de.adesso.example.framework.annotation.Implementation;
import de.adesso.example.framework.annotation.ImplementationDefinition;

@Emulated
public interface Cashier {

	@ImplementationDefinition(value = @Implementation(bean = PointOfSale.class))
	ApplicationProtocol<Money> encash(ShoppingCart cart, ApplicationProtocol<Money> state);
}
