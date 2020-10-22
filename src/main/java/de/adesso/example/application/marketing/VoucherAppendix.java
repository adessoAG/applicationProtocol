package de.adesso.example.application.marketing;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class VoucherAppendix extends ApplicationAppendix<Voucher> {

	@Autowired
	public VoucherAppendix(final Voucher voucher) {
		super(voucher);
	}

	@Override
	public UUID getOwner() {
		return Marketing.marketingOwner;
	}

	@Override
	public UUID getAppendixId() {
		return Marketing.voucherAppendixId;
	}
}
