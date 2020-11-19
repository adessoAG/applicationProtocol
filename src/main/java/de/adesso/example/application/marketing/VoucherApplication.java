package de.adesso.example.application.marketing;

/**
 * The enum specifies the different possibilities a voucher can be applied to.
 *
 * @author Matthias
 *
 */
public enum VoucherApplication {
	/** applicable to the whole shopping cart */
	APPLICABLE_TO_CART,
	/** applicable to a shopping cart entry with arbitrary amount */
	APPLICABLE_TO_ENTRY,
	/** applicable to a single article */
	APPLICABLE_TO_SUB_ENTRY
}
