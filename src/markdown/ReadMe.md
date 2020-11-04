# Readme Application Framework
## Motivation
The main motivation to create the framework was to support clean software. The following list shows some 
procedure to achieve better code quality.
* Work in small teams. One precondition is, that the tasks are small and concise. 
* Keep the number of dependencies as low as possible.  
* Surely there are many other circumstances which should be noted.
Let's have a simple example. 
```java
Money price calculatePrice(Article article);
```
This is a rather simple signature. It is required, because articles may depend on daily purchase prices.
Therefore there should be some functionality to calculate the price. 

The next requirement is, that you want to offer your employees reduced prices for personal need. This requirement changes the signature to:
```java
Money price calculatePrice(Article article, Employee employee);
```
Now the marketing department wants to create some buying incentives for the customers. They are going to introduce vouchers which can be given
to customers. The requirement changes the signature to:
```java
Money price calculatePrice(Article article, Employee employee, Voucher voucher);
```
You can imagine that there will come further request which will again raise the need to extend the price calculation functionality. 

If we have a closer look what is going to happen inside the price calculation and later cash up at checkout. At the moment we provide advantages to 
employees, we have to consider the taxes. The employee receives an additional income which requires taxes to be paid. This means at the moment the 
employee checks out the goods, the human resources management need to know the purchase. Also there is a need to book the difference, because if the 
product is sold below its price, the difference needs to be considered in accounting. 

This look opens the eyes, the simple procedure calculatePrice consists of various different data flows which are interwoven and not reflected by the 
programming flow. So the simple example of calculatePrice shows several risks:
* high complexity
* numerous dependencies
* danger of improper use of information, because all is available by the signature
* I'm sure, I could find several further problems if I go deeper

What the heck if I could stay at the simple call of the first implementation. All would be fine. 
* small and clear task
* minimum of dependencies
* no improper use of other internal information
* no danger of wrong dependencies

So the idea is, to divide the task in several components:
* BasePriceCalculator which does the original job
* EmployeeDiscountCalculator which calculates the discount for the employee and is responsible for all the taxes stuff. This component is owned by 
the human resources department.
* VoucherDiscountCalculator which calculates the discount based on a voucher the customer provides. This component is owned by the marketing. 

At next we need a small component which combines the different calculators. The idea of the framework is, that all the calculators are provided 
as independent components. The component which binds them together is a standard component which binds Spring to achieve the result. To make this 
happen, it is necessary to adopt the signature. Since we want to transport more than one result, I created the so called ApplicationProtocoll. 
It carries the original calculation result and all further information as so called appendixes (ApplicationAppendix). 

An Appendix is owned by a business organization, e.g. the marketing. Each appendix owned by marketing carries the id of marketing. So the 
appendixes marketing is responsible can easily identified. Applying this changes, the signature would like this:
 ```java
 @Emulated
interface PriceCalculator {
	@Implementation(
			implementations = {
					BasePriceCalculator.class,
					EmployeeDiscountCalculator.class,
					VoucherDiscountCalculator.class
			})
    ApplicationProtocol<Money> price calculatePrice(Article article, ApplicationProtocol<Money> state);
}
```
 The application protocol is input to receive private information and it is output, to be able to forward all information inclusive appendixes of 
 different responsibilities. The annotation @Emulated tells the system, that it has to do some magic with this interface. The annotation @Implementation tells the beans which should be used. 
 
 The signature of the BasePriceCalculator could be like this:
  ```java
 public class BasePriceCalculator {

	@CallStrategy(strategy = CallingStrategy.Eager)
	public ApplicationProtocol<BigDecimal> calculatePrice(
			@Required final Article article,
			@Required final ApplicationProtocol<BigDecimal> state) {

		// do the task

		return protocol;
	}
}
```
In this example the BasePriceCalculator is a POJ. Instead also a Spring bean can be used, if the interface of the bean is used at the introduction of the PriceCalculator. 
Here you find other annotations. The @CallStrategy tells, whether this bean is 
called conditionally or in every case (Eager). The information @Required at the
method parameters, tell, that the call is only possible if the required information is present. 

Lets have a look at the next bean, the EmployeeDiscountCalculator:
 ```java
 public class EmployeeDiscountCalculator {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<BigDecimal> calculatePrice(
			@Required final Article article,
			@Required final Employee employee,
			@Required final ApplicationProtocol<BigDecimal> state) {

		// do the task

		return protocol;
	}
}
```
The difference is, that the strategy now is RequiredParameters. Thus the call will
only happen, if the employee can be found within the appendix. Remember the interface PriceCalculator, at the method calculatePrice now employee is given in the signature. In this case the system extracts the employee from the appendix. If it is not present, this bean will not be called. 

Another aspect of this procedure is, that the calculation result can easily archived. Staying with the example above, the procedure could be:
- Customer informs about possible price for an article.
- Later on at check out the customer wants to purchase the article. 

Normally one would repeat the whole calculation which could be require extensive resources. By using a cache it would be possible to reuse the first calculation and save resources. 

## Summary
The system introduces new Annotations to be used. Dependencies between business departments cooperating to implement price calculation are removed. By this the 
development team should have an easier task to keep the system tidy. 