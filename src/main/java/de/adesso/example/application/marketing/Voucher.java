package de.adesso.example.application.marketing;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Voucher implements Serializable {

	private static final long serialVersionUID = 6119269215970563757L;

	/** basic voucher type */
	private final VoucherType type;
	/** unique identifier of the voucher */
	private final String voucherId;
	/** How often can the voucher be used */
	private int maxApplications = 1;
	/** where is the voucher applicable */
	private final Set<VoucherApplication> applicableAt = new HashSet<>();
	/** used, when the system tries to use the vouchers */
	private transient int tryUse = 0;

	/** voucher compatibility with other vouchers */
	private final VoucherCompatibility compatibility;

	public Voucher(final String voucherId, final VoucherCompatibility compatibility, final VoucherType type) {
		this.voucherId = voucherId;
		this.type = type;
		this.compatibility = compatibility;
	}

	public Voucher(final String voucherId, final VoucherCompatibility compatibility, final VoucherType type,
			final int maxApplications) {
		this.voucherId = voucherId;
		this.maxApplications = maxApplications;
		this.type = type;
		this.compatibility = compatibility;
	}

	public Voucher(final String voucherId, final VoucherCompatibility compatibility, final VoucherType type,
			final VoucherApplication... applicableAt) {
		this.voucherId = voucherId;
		this.applicableAt.addAll(Set.of(applicableAt));
		this.type = type;
		this.compatibility = compatibility;
	}

	public Voucher(final String voucherId, final VoucherCompatibility compatibility, final VoucherType type,
			final int maxApplications,
			final VoucherApplication... applicableAt) {
		this.voucherId = voucherId;
		this.maxApplications = maxApplications;
		this.applicableAt.addAll(Set.of(applicableAt));
		this.type = type;
		this.compatibility = compatibility;
	}

	public void utilize() {
		if (this.maxApplications == 0) {
			throw VoucherNotUtilizableException.notUtilizable(this);
		}
		this.maxApplications--;
	}

	public void tryUtilize() {
		if (this.maxApplications - this.tryUse == 0) {
			throw VoucherNotUtilizableException.notUtilizable(this);
		}
		this.tryUse++;
	}

	public void resetTryUse() {
		this.tryUse = 0;
	}

	public boolean isUtilizable() {
		return this.maxApplications > 0;
	}

	public boolean isTryUtilizable() {
		return this.maxApplications - this.tryUse > 0;
	}
}
