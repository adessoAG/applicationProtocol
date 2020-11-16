package de.adesso.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import de.adesso.example.application.PriceCalculator;
import de.adesso.example.application.employment.Employee;
import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.application.employment.EmployeeDiscountCalculator;
import de.adesso.example.application.marketing.VoucherDiscount;
import de.adesso.example.application.marketing.VoucherAppendix;
import de.adesso.example.application.marketing.VoucherDiscountCalculator;
import de.adesso.example.application.stock.Article;
import de.adesso.example.application.stock.BasePriceCalculator;
import de.adesso.example.framework.core.ArgumentApplicationProtocol;
import de.adesso.example.framework.core.ArgumentFromAppendix;
import de.adesso.example.framework.core.ArgumentFromMethod;
import de.adesso.example.framework.core.BeanOperation;
import de.adesso.example.framework.core.DaisyChainDispatcherFactory;
import de.adesso.example.framework.core.MethodImplementation;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class ApplicationConfig {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private BasePriceCalculator basePriceCalculator;

	@Autowired
	private EmployeeDiscountCalculator employeeDiscountCalculator;

	@Autowired
	private VoucherDiscountCalculator voucherDiscountCalculator;

	public ApplicationConfig() {
		log.atDebug().log("intatiated the configuration");
	}

	@Bean
	@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
	PriceCalculator priceCalculator() {
		log.atDebug().log("start with initilization of PriceCalculator");

		final PriceCalculator priceCalculator = new DaisyChainDispatcherFactory(this.context)
				.emulationInterface(PriceCalculator.class)
				.implementation(MethodImplementation.builder()
						.methodIdentifier("calculatePrice")
						// first call BasePriceCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(this.basePriceCalculator)
								.methodIdentifier("calculatePrice")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentApplicationProtocol())
								.build())
						// second call EmployeeDiscountCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(this.employeeDiscountCalculator)
								.methodIdentifier("calculatePrice")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentFromAppendix(Employee.class, EmployeeAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						// third call VoucherDiscountCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(this.voucherDiscountCalculator)
								.methodIdentifier("calculatePrice")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentFromAppendix(VoucherDiscount.class, VoucherAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						.build())
				.build();
		log.atDebug().log("done with initialization of PriceCalculator");

		return priceCalculator;
	}
}
