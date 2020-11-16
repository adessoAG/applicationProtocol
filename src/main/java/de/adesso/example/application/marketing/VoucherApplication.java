package de.adesso.example.application.marketing;

/**
 * The enum specifies the different possibilities a voucher can be applied to.
 *
 * @author Matthias
 *
 */
public enum VoucherApplication {
	/** applicable to the whole shopping cart */
	ApplicableToCart,
	/** applicable to a shopping cart entry with arbitrary amount */
	ApplicableToEntry,
	/** applicable to a single article */
	ApplicableToSubEntry
}
