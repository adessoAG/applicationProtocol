package de.adesso.example.application;

import java.math.BigDecimal;

import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;

public interface Cashier {

	ApplicationProtocol<BigDecimal> openBill(Customer customer);

	ApplicationProtocol<BigDecimal> addItem(Article article, ApplicationProtocol<BigDecimal> protocol);

	ApplicationProtocol<BigDecimal> encash(ApplicationProtocol<BigDecimal> state);
}
