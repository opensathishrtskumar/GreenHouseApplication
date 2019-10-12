package org.lemma.infra.config;

import java.util.Properties;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DBConfigPropertySourceInitializer
		implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextInitializer#initialize(org.
	 * springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) {
		MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
		propertySources.addLast(getPropertySource());
	}

	/**
	 * Returns configured PropertySource
	 * 
	 * @return
	 */
	private PropertySource getPropertySource() {
		Properties properties = new Properties();
		// Add properties if any required
		return new PropertiesPropertySource("INIT_PROPS", properties);
	}
}