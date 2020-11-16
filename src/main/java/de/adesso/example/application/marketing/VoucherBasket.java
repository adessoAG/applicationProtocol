package de.adesso.example.application.marketing;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Each type which can carry an voucher should have an attribute of type
 * VoucherBasket. It ensures, that only compatible vouchers are assigned.
 *
 * @author Matthias
 *
 */
@Getter
@Log4j2
public class VoucherBasket {

	private final VoucherApplication level;
	private final Set<Voucher> vouchers = new HashSet<>();

	public VoucherBasket(final VoucherApplication level) {
		this.level = level;
	}

	/**
	 * Assign an voucher to the basket while checking all constraints of voucher
	 * assignment.
	 *
	 * @param voucher the voucher to be added to the basket
	 */
	public void addVoucher(final Voucher voucher) {
		this.checkAssignable(voucher);

		// all is fine
		this.vouchers.add(voucher);
		voucher.tryUtilize(); // final utilization during booking operation
	}

	/**
	 * Check if the voucher can be assigned to this basket.
	 *
	 * @param voucher the voucher to be tested
	 */
	public boolean isAssignable(final Voucher voucher) throws VoucherNotApplicableException {
		if (!voucher.isTryUtilizable()) {
			return false;
		}

		if (!voucher.getApplicableAt().contains(this.level)) {
			return false;
		}

		// check compatibility with existing vouchers

		// have a TopDog voucher
		Optional<Voucher> optional = this
				.hasVoucherOfCompatibilityAndType(VoucherCompatibility.StandAloneWithinType, voucher.getType());
		if (!optional.isEmpty()) {
			return false;
		}

		// there is a voucher which is not compatible
		optional = this.hasVoucherOfCompatibility(VoucherCompatibility.TopDog);
		if (!optional.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Check if the voucher can be assigned to this basket.
	 *
	 * @param voucher the voucher to be tested
	 */
	private boolean checkAssignable(final Voucher voucher) throws VoucherNotUtilizableException {
		if (!voucher.isTryUtilizable()) {
			// voucher is not utilizable
			this.throwAndLog(VoucherNotUtilizableException.notUtilizable(voucher));
		}

		if (!voucher.getApplicableAt().contains(this.level)) {
			// voucher not applicable on this level
			this.throwAndLog(VoucherNotUtilizableException.wrongLevelException(voucher, this.level));
		}

		// check compatibility with existing vouchers

		// have a TopDog voucher
		Optional<Voucher> optional = this
				.hasVoucherOfCompatibilityAndType(VoucherCompatibility.StandAloneWithinType, voucher.getType());
		if (!optional.isEmpty()) {
			this.throwAndLog(VoucherNotUtilizableException.conflictException(this.level));
		}

		// there is a voucher which is not compatible
		optional = this.hasVoucherOfCompatibility(VoucherCompatibility.TopDog);
		if (!optional.isEmpty()) {
			this.throwAndLog(VoucherNotUtilizableException.conflictException(this.level));
		}

		return true;
	}

	/**
	 * Remove a voucher from the basket.
	 *
	 * @param voucher the voucher to be removed
	 */
	public void removerVoucher(final Voucher voucher) {
		this.vouchers.remove(voucher);
	}

	public boolean containsVoucherOfType(final VoucherType type) {
		return !this.vouchers.stream()
				.filter(v -> v.getType() == type)
				.findFirst().isEmpty();
	}

	// helper functions

	/**
	 * select vouchers by criteria
	 *
	 * @param vouchers      the list of vouchers to inspect
	 * @param applicability requested application
	 * @return the selected vouchers from the list
	 */
	public static List<Voucher> extractVouchersByApplicability(final List<Voucher> vouchers,
			final VoucherApplication applicability) {
		return vouchers.stream()
				.filter(v -> v.isUtilizable()
						&& v.getApplicableAt().contains(applicability))
				.collect(Collectors.toList());
	}

	private Optional<Voucher> hasVoucherOfCompatibility(final VoucherCompatibility compatibility) {
		return this.vouchers.stream()
				.filter(v -> v.getCompatibility() == compatibility)
				.findFirst();
	}

	private Optional<Voucher> hasVoucherOfCompatibilityAndType(final VoucherCompatibility compatibility,
			final VoucherType type) {
		return this.vouchers.stream()
				.filter(v -> v.getCompatibility() == compatibility && v.getType() == type)
				.findFirst();
	}

	private void throwAndLog(final RuntimeException e) throws RuntimeException {
		log.atInfo().log(e.getMessage());
		throw e;
	}
}
