package de.adesso.example.application.marketing;

public enum VoucherCompatibility {
	/** allows not other vouchers within the same basket */
	TopDog,
	/** only one voucher of same type allowed */
	StandAloneWithinType,
	/** cooperates with other vouchers */
	Cooperative
}
