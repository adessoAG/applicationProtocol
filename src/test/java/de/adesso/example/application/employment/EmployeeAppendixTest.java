package de.adesso.example.application.employment;

import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class EmployeeAppendixTest {

	@Test
	void testGetContentClass() {
		final Class<EmployeeAppendix> type = EmployeeAppendix.class;
		final ParameterizedType pt = (ParameterizedType) type.getGenericSuperclass();
		Arrays.asList(pt.getActualTypeArguments()).stream()
				.forEach(t -> {
					System.out.println(t.getTypeName());
					System.out.println(t.getClass().getName());
				});
		fail("Not yet implemented");
	}

}
