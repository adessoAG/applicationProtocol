/**
 * The MIT License (MIT)
 *
 * Copyright © 2020 Matthias Brenner and Adesso SE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package de.adesso.example;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import de.adesso.example.application.PriceCalculator;
import de.adesso.example.application.accounting.AccountingBean;
import de.adesso.example.application.accounting.Customer;
import de.adesso.example.application.accounting.CustomerAppendix;
import de.adesso.example.application.employment.Employee;
import de.adesso.example.application.employment.EmployeeAppendix;
import de.adesso.example.application.employment.EmployeeShoppingBean;
import de.adesso.example.application.marketing.MarketingBean;
import de.adesso.example.application.marketing.Voucher;
import de.adesso.example.application.marketing.VoucherAppendix;
import de.adesso.example.application.stock.Article;
import de.adesso.example.application.stock.PricingBean;
import de.adesso.example.framework.core.ArgumentApplicationProtocol;
import de.adesso.example.framework.core.ArgumentFromAppendix;
import de.adesso.example.framework.core.ArgumentFromMethod;
import de.adesso.example.framework.core.BeanOperation;
import de.adesso.example.framework.core.DaisyChainDispatcherFactory;
import de.adesso.example.framework.core.MethodImplementation;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableAsync
@Log4j2
public class ApplicationConfig {

	public ApplicationConfig() {
		log.atDebug().log("intatiated the configuration");
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix(
				"ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}

	@Bean
	@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
	PriceCalculator priceCalculator(
			final ApplicationContext context,
			final AccountingBean accountingBean,
			final PricingBean basePriceCalculator,
			final EmployeeShoppingBean employeeShopping,
			final MarketingBean voucherDiscountCalculator) {
		log.atDebug().log("start with initilization of PriceCalculator");

		final PriceCalculator priceCalculator = new DaisyChainDispatcherFactory(context)
				.emulationInterface(PriceCalculator.class)
				.implementation(MethodImplementation.builder()
						.methodIdentifier("calculatePrice")
						// check employee and set customer if found
						.beanOperation(BeanOperation.builder()
								.implementation(employeeShopping)
								.methodIdentifier("setEmployeeCustomer")
								.argument(new ArgumentFromAppendix(Employee.class, EmployeeAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						// set the customer information
						.beanOperation(BeanOperation.builder()
								.implementation(accountingBean)
								.methodIdentifier("checkOrAddCustomer")
								.argument(new ArgumentApplicationProtocol())
								.build())
						// first call BasePriceCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(basePriceCalculator)
								.methodIdentifier("buildPrice")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentFromAppendix(Customer.class, CustomerAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						// second call EmployeeDiscountCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(employeeShopping)
								.methodIdentifier("discountEmployee")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentFromAppendix(Customer.class, CustomerAppendix.class))
								.argument(new ArgumentFromAppendix(Employee.class, EmployeeAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						// third call VoucherDiscountCalculator
						.beanOperation(BeanOperation.builder()
								.implementation(voucherDiscountCalculator)
								.methodIdentifier("discountVoucher")
								.argument(new ArgumentFromMethod(Article.class, 0))
								.argument(new ArgumentFromAppendix(Customer.class, CustomerAppendix.class))
								.argument(new ArgumentFromAppendix(Voucher.class, VoucherAppendix.class))
								.argument(new ArgumentApplicationProtocol())
								.build())
						.build())
				.build();
		log.atDebug().log("done with initialization of PriceCalculator");

		return priceCalculator;
	}
}
