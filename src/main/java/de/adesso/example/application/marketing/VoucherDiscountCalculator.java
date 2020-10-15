package de.adesso.example.application.marketing;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.adesso.example.application.stock.Article;
import de.adesso.example.framework.AppendixOwner;
import de.adesso.example.framework.ApplicationFrameworkInvokable;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class VoucherDiscountCalculator 
	extends AppendixOwner
	implements ApplicationFrameworkInvokable {

	public VoucherDiscountCalculator() {
		super(retrieveOwnUuid());
	}

	private static UUID ownUuid = UUID.randomUUID();
	private static UUID retrieveOwnUuid() {
		return ownUuid;
	}

	public ApplicationProtocol<BigDecimal> calculatePrice(Article article, Voucher voucher) {
		return null;
	}
}
