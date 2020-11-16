package de.adesso.example.application.marketing;

public class VoucherNotUtilizableException extends RuntimeException {

	private static final long serialVersionUID = -7917746111740547420L;

	private VoucherNotUtilizableException(final String message) {
		super(message);
	}

	public static VoucherNotUtilizableException notUtilizable(final Voucher voucher) {
		final String message = "the voucher is not utilizable: " + voucher.getVoucherId();
		return new VoucherNotUtilizableException(message);
	}

	public static VoucherNotUtilizableException wrongLevelException(final Voucher voucher,
			final VoucherApplication... validApplications) {
		final StringBuilder sb = new StringBuilder()
				.append("voucher: ")
				.append(voucher.getVoucherId())
				.append(": only the VoucherApplications [");
		boolean insertedOne = false;
		for (final VoucherApplication application : validApplications) {
			if (insertedOne) {
				sb.append(", ");
			}
			insertedOne = true;
			sb.append(application);
		}
		sb.append("] are feasable to entries, got: ")
				.append(voucher.getApplicableAt());
		return new VoucherNotUtilizableException(sb.toString());
	}

	public static VoucherNotUtilizableException conflictException(final VoucherApplication level) {
		final String message = "the voucher cannot be used with this article, because there are confliction vouchers";
		return new VoucherNotUtilizableException(message);
	}
}
