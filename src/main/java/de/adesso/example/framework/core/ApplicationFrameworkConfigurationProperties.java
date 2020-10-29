package de.adesso.example.framework.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "afw")
@Getter
@Setter
public class ApplicationFrameworkConfigurationProperties {

	private String basePackage;
}
