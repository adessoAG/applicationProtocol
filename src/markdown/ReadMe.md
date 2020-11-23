# Readme Application Framework

This `readme` provides information about the motivation to create the framework. It explains the functionality of the framework with the help of examples. After introducing the whole functionality it discusses alternative solutions. 

At the end you should be able to decide whether this framework could help you within your current programming project. 

# Motivation
The main motivation to create the framework was to support clean software. The following list shows a new idea to achieve better code quality.
* Work in small teams. One precondition is, that the tasks are small and concise. 
* Keep the number of dependencies as low as possible.  
* Find ways to minimize degradation of inner quality of a system while working on it. 

## The problem case
Surely there are many other circumstances which should be noted. Let's have a simple example. 
```java
Money price calculatePrice(Article article);
```
This is a rather simple signature. It is required, because articles may depend on daily purchase prices. Therefore there should be some functionality to calculate the price. 

Some time later, you may want to excite your employees and support the commitment to your shop. Thus you provide the personal the possibility to purchase your goods to reduced prices. This requirement changes the signature to:
```java
Money price calculatePrice(Article article, Employee employee);
```
Next the marketing department wants to create some buying incentives for the customers. They are going to introduce vouchers which can be given
to customers. The requirement changes the signature to:
```java
Money price calculatePrice(Article article, Employee employee, Voucher voucher);
```
You can imagine that there will come further request which will again raise the need to extend the price calculation functionality. 

If we have a closer look what is going to happen inside the price calculation and later cash up at checkout. At the moment we provide advantages to employees, we have to consider the taxes. The employee receives an additional income which requires taxes to be paid. This means at the moment the employee checks out the goods, the human resources management need to know the purchase. Also there is a need to book the difference, because if the product is sold below its price, the difference needs to be considered in accounting. 

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
* clear responsibilities

So the idea is, to divide the task in several components:
* `PricingBean` which does the original job
* `EmployeeShoppingBean` which calculates the discount for the employee and is responsible for all the taxes stuff. This component is owned by 
the human resources department.
* `MarketingBean` which calculates the discount based on a voucher the customer provides. This component is owned by the marketing. 

At next we need a small component which combines the different calculators. The idea of the framework is, that all beans used within the calculation chain are provided as independent components by the responsible business department. Than there is one component of the framework, which binds them together. 

## Application Protocol
At first, the interface is adopted to the framework. To be able to transport more information, there is the so called `ApplicationProtocol`. It allows for various appendixes  (specializations of `ApplicationAppendix`) which can provide information to the calculating beans. Since there are also many result values, the protocol collects them together. This leads to a calculation pipeline. 

An Appendix is owned by a business organization, e.g. the marketing. Each appendix owned by marketing carries the owner id of marketing. So the appendixes, marketing is responsible, can easily identified. 

## Implementing the Example 
Applying this changes, the signature would like this:
```java
@Emulated
interface PriceCalculator {

@ImplementationDefinition(
			value = {
					@Implementation(bean = PricingBean.class, method = "buildPrice"),
					@Implementation(bean = EmployeeShoppingBean.class, method = "discountEmployee"),
					@Implementation(bean = MarketingBean.class, method = "discountVoucher")
			})			})
    ApplicationProtocol<Money> price calculatePrice(Article article, ApplicationProtocol<Money> state);
}
```
 The application protocol is input to receive private information for beans involved in the calculation chain. Next it is also output, to be able to forward all information inclusive appendixes of 
 different responsibilities. The annotation `@Emulated` tells the system, that it has to do some magic with this interface. The annotation `@ImplementationDefinition` tells the magic behind the scene which beans which should be used. Each `@Implementation` annotation tells a bean and a method of the bean to build up the calculation chain. The method identifier may be omitted. In this case, the required method must have the same identifier as the emulated method. 
 Java allows to omit the annotation `@ImplementationDefinition` and write only the `@Implementation` annotations. unfortunately this works only, if there are more than one bean involved, thus more than one `@Implementation` annotations are provided. Due to this shortcoming, I tend to write always the complete annotation with the `@ImplementationDefinition`. Otherwise if you not aware of this shortcoming you might wonder why the system does not behave as you thought it should do. 
 
 The signature of the `PricingBean` could be like this:
```java
@Service
public class PricingBean {

	@CallStrategy(strategy = CallingStrategy.EAGER)
	public ApplicationProtocol<BigDecimal> buildPrice(
			@Required final Article article,
			@Required final Customer customer,
			@Required final ApplicationProtocol<BigDecimal> state) {

		// do the task

		return protocol;
	}
}
```
In this example the BasePriceCalculator is a Spring bean. You can also use a POJO, another emulated interface of the interface of a spring bean.  
Here you find other annotations. The `@CallStrategy` tells, whether this bean is 
called conditionally or in every case (Eager). The information `@Required` at the
method parameters, tell, that the call is only possible if the required information is present. 

Since the call `buildPrice` is annotated as `CallingStrategy.EAGER` if the call is not possible, the whole emulation will fail. Thus if one of the parameters annotated as required (`@Required`) is missing, a `BeanCallException` is thrown. This is true for all parameters: article, customer and state.

You may have recognized, that there is the parameter customer. I introduced this parameter, because the beans should provide accounting records within the resulting state. This can enable later on, that the chains result is used during booking. Recalculation becomes obsolete. 

Another aspect of this procedure is, that the calculation result can easily archived, because it is available as a complex object (the protocol). Staying with the example above, the procedure could be:
- Customer informs about possible price for an article.
- Later on at check out the customer wants to purchase the article. 
This implies that all appendixes have to be `Serializable`.

Normally one would repeat the whole calculation which could be require extensive resources. By using a cache it would be possible to reuse the first calculation and save resources. 

Lets have a look at the next bean, the `EmployeeShoppingBean`:
```java
@Service
public class EmployeeShoppingBean {

	@CallStrategy(strategy = CallingStrategy.RequiredParameters)
	public ApplicationProtocol<BigDecimal> discountEmployee(
			@Required final Article article,
			@Required final Employee employee,
			@Required final ApplicationProtocol<BigDecimal> state) {

		// do the task

		return protocol;
	}
}
```
The difference is, that the strategy now is `CallingStrategy.RequiredParameters`. Thus the call will
only happen, if the employee can be found within the appendix. Remember the interface `PriceCalculator`. This bean requires the at the method signature of `discountEmployee` the additional parameter employee. It is annotated as required. In this case the system extracts the employee from the appendix, since there is no employee available within the signature of the emulated interface. If the employee is not found in the appendix, the bean will not be called. 

The third bean within the trio of the beans calculating the chain is the `MarketingBean`. Its declaration looks like:  
```java
@Service
public class MarketingBean {

	@CallStrategy(strategy = CallingStrategy.REQUIRED_PARAMETER)
	public ApplicationProtocol<Money> discountVoucher(
			@Required final Article article,
			@Required final Customer customer,
			@Required final Voucher voucher,
			@Required final ApplicationProtocol<Money> state) {
        // do the job
        }
```
This bean requires several parameters. At first the voucher. If it is not present, no discount calculation is required. This is achieved by annotating the method with `CallingStrategy.REQUIRED_PARAMETER` and the parameter with `@Required`. The additional parameters customer and article are required to provide the appropriate booking records. 

## Behavior of the Queue

If one call of the queue fails with an exception, the whole queue fails. This ensures, that calculation is terminated at the first problem. All steps after this problem might be meaningless. Therefore they are not executed. 

There is an exception to this behavior. If an method is annotated as CallingStrategy.REQUIRED_PARAMETER the system behind the scenes throws a RequiredParameterException which is a specialization of CalculationNotApplicable. Thus if the system cannot provide a required parameter, a RequiredParameterException is thrown. If some kinds of check yield the result, that further calculation of this bean wihin the queue is not necessary, it should throw an exception which is a specialization of CalculationNotApplicable. In this case the call of the bean is terminated, but the calculation of the queue continues. 

## Hot to use the emulated interface. 
The emulated interface becomes a "normal" Spring bean. Thus it can be used as any other Spring bean.
Here are some details of the code of the ClientExample:

```java
class ClientExample{

    private final PriceCalculator priceCalculator;
    
	@Autowired
	public ClientExample(
			final PriceCalculator priceCalculator) {
		this.priceCalculator = priceCalculator;
	}
	
	private void testCallToEmulatedInterface (){
	    // build the call for the example
	    state = priceCalculator.calculatePrice(myArticle, state);
	    // evaluate the result
	}
}
```
The constuctor is marked as `@Autowired`, then Spring will provide the reference to the emulated interface. You can use it than as every other Spring interface. 


# The Programming Example
The source code is divided into two parts. The package `de.adesso.example.application` contains the example application. Let's have a look in the application configuration, found in class ApplicationConfig. It creates the bean "priceCalculator" programmatically. This construction provides a view of the tasks the framework has to accomplish, if running annotation based. 
There is an interface `PriceCalculator` which has to define at least one method. The interface is not implemented in the application. The implementation is generated by the calls found in the configuration. In this case, the configuration is the only part of the program which references the various calculators. At best, all calculator should be in each case part of an own project and independent deliverable. In this case, no calculator would be able to implement wrong dependencies. 

There is an additional interface, `PriceCalculatorAnnotated`. It provides the same functionality only by using annotations. The preconditions to make it work are the following:
- The interface is annotated with `@Emulated`. There is no implementation available for that interface. 
- Methods which should be emulated are annotated with `@Implementation`. This annotation lists the beans which should be incorporated into the implementation which is generated. 
- Parameters of the emulated interface annotated with `@RequiredParameter` are mandatory and may not be null. Hint, this is the annotation to be used only for the emulated interface. The implementing beans use the annotation `@Required`.
- Each implementing bean is required to provide a method with the same identifier as within the interface. Overloaded methods are not supported. 
- The methods of the implementing beans may be annotated with an annotation `@CallStrategy`. It provides a clue how to handle the call. If the strategy is `EAGER`, the method has to be called in any way. If the strategy is RequiredParameters, the method is only called if the required parameters are available. Parameter which are required are marked with the annotation `@Required`. If one parameter has to be extract from the appendix and is not present, no call will happen. 

You can surf through the client application. It creates the example introduced at the beginning of this documentation. 

# The Implementation of the Framework
## Overview
The central class of the implementation is the `DaisyChainDispatcher`. It is the InvocationHandler of the generated proxy. The class `DaisyChainDispatcherFactory` is the class to be used to provide instances of the dispatcher. The first example of the manual construction of the `PriceCalculator` interface used the `DaisyChainDispatcherFactory` explicitly. 

The other solution with the annotated interface `PriceCalculatorAnnotated` the class `ApplicationBeanDefinitionRegistryPostProcessor` is the game changer. It is a `BeanDefinitionRegistryPostProcessor` which scans the classes and looks for the `@Emulated` annotation. It does the job to prepare the interfaces. The bean factories to create the emulation is implemented in the class `ApplicationProxyFactory` which is entered into the bean registry as factory for the interfaces. Spring then uses this factory to create the emulated interfaces. It was a little bit tricky to get the implementation running, because a lot of knowledge about internal information of Spring was necessary. 

`MethodImplementation` is the helper class which represents an emulated method of the emulated interface. BeanOperation is the representation of a call to a bean implementing the emulated method. Arguments are the representation of parameters. There is a lot of checking during construction of the emulation. Later on the state represents how to extract the parameters. 

## Further dispatchers
At the moment there is only a DaisyChainDispatcher which provides calculations like pipelines. The only extension to pipelines is, that calculations within the pipe can be made optional. 

Another dispatcher idea could be a splitting and a joining dispatcher. For example if your business task is to provide offers from various suppliers, than you might collect offers in parallel to minimize the elapsed time during calculation. The procedure could be, select the suppliers, prepare the requests and then split the calculation. Each request itself could be processed again as a daisy chain. Later on, the join collects all results in a common format. 


# Summary
The system introduces new Annotations to be used. Dependencies between business departments cooperating to implement price calculation are removed. By this the 
development team should have an easier task to keep the system tidy. 

This is a proposal. It is the first draft of an idea. It can grow to useful tool if many ideas can be incorporated. Please don't hesitate, I appreciate any comment on it, even if you don't like it. 

There are still ideas which are not implemented yet.

### Distinction
This idea is in competition with other possibilities to implement this problem. At first I want to note BPMN driven engines. The process is designed with help of a standardized notation, which can be processed by various different engines. Chaining of tasks, split and join are already available. 

The BPMN engine introduces a lot of new infrastructure. If your shop already works with BPMN, that's fine and a very good choice. In this case this framework could probably support implementation of tasks of a fine grained level. But it will never replace the BPMN solution.