package de.adesso.example.application.marketing;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationProtocol;
import de.adesso.example.framework.annotation.CallStrategy;
import de.adesso.example.framework.annotation.CallingStrategy;
import de.adesso.example.framework.annotation.Required;

@Service
public class VoucherDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<BigDecimal> calculatePrice(
			@Required final Article article,
			@Required final Voucher voucher,
			final ApplicationProtocol<BigDecimal> state) {
		return null;
	}
}
