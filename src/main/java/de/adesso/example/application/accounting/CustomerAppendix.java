package de.adesso.example.application.accounting;

import java.util.UUID;

import de.adesso.example.framework.ApplicationAppendix;
import de.adesso.example.framework.annotation.Appendix;

@Appendix
public class CustomerAppendix extends ApplicationAppendix<Debitor> {

	public CustomerAppendix(final Debitor content) {
		super(content);
	}

	@Override
	public UUID getOwner() {
		return Accounting.id;
	}

}
