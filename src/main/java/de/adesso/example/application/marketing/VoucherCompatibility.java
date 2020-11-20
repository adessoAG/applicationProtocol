package de.adesso.example.application.marketing;

public enum VoucherCompatibility {
	/** allows not other vouchers within the same basket */
	TOP_DOG,
	/** only one voucher of same type allowed */
	STAND_ALONE_WITHIN_TYPE,
	/** cooperates with other vouchers */
	COOPERATIVE
}
