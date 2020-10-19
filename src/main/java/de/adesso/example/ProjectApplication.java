package de.adesso.example;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class ProjectApplication {

	public static void main(final String[] args) {
		new SpringApplicationBuilder(ProjectApplication.class)
				.build()
				.run(args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			final String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (final String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}
}
