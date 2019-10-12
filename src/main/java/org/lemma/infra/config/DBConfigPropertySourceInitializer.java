package org.lemma.infra.config;

import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class DBConfigPropertySourceInitializer
		implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) {
		MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
		propertySources.addLast(getPropertySource());
		System.out.println("Registered PropertySource");
	}

	private PropertySource getPropertySource() {
		Properties properties = new Properties();
		properties.put("smtp.host", "smtp.gmail.com");

		PropertiesPropertySource src = new PropertiesPropertySource("DB_PROPS", properties);

		System.out.println(src.containsProperty("smtp.host") + " ::: ");

		return src;
	}
}