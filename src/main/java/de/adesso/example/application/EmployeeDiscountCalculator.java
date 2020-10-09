package de.adesso.example.application;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import de.adesso.example.application.employment.Employee;
import de.adesso.example.framework.AppendixOwner;
import de.adesso.example.framework.ApplicationFrameworkInvokable;
import de.adesso.example.framework.ApplicationProtocol;

@Service
public class EmployeeDiscountCalculator 
	extends AppendixOwner
	implements ApplicationFrameworkInvokable{

	public EmployeeDiscountCalculator() {
		super(retrieveOwnUuid());
	}
	
	public ApplicationProtocol<BigDecimal> calculatePrice(Article article, Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	private static UUID ownUuid = UUID.randomUUID();
	private static UUID retrieveOwnUuid() {
		return ownUuid;
	}
}
