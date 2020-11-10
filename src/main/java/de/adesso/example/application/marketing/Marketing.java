package de.adesso.example.application.marketing;

import java.util.UUID;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import de.adesso.example.application.Standard;
import de.adesso.example.application.accounting.Creditor;
import de.adesso.example.framework.ApplicationOwner;

@Service
public class Marketing extends ApplicationOwner {

	private static final UUID marketingOwner = UUID.randomUUID();

	private static final Creditor marketingVoucherAccount = new Creditor(UUID.randomUUID());

	@Override
	protected UUID getOwnerId() {
		return marketingOwner;
	}

	public static Creditor getMarketingVoucherAccount() {
		return marketingVoucherAccount;
	}

	public static UUID getMarketingOwner() {
		return marketingOwner;
	}

	public Voucher createTenEuroDiscount() {
		return new AbsoluteVoucher("10EuroDiscount", Money.of(10.00, Standard.EUROS));
	}
}
