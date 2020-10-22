package de.adesso.example.application.marketing;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.ApplicationFrameworkInvokable;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class VoucherDiscountCalculator
		implements ApplicationFrameworkInvokable {

	public ApplicationProtocol<BigDecimal> calculatePrice(final Article article, final Voucher voucher) {
		return null;
	}
}
