package de.adesso.example.application.marketing;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import de.adesso.example.framework.TApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class VoucherAppendix extends TApplicationAppendix<Voucher> {

	public static final UUID voucherAppendixId = UUID.randomUUID();

	@Autowired
	public VoucherAppendix(final UUID owner, final Voucher voucher) {
		super(voucherAppendixId, owner, voucher);
	}
}
