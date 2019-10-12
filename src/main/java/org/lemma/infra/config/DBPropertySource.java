package org.lemma.infra.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * Loads Properties from DB and sets in Environment
 * 
 * 
 * @author RTS Sathish Kumar
 *
 */
@Configuration
public class DBPropertySource implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(DBPropertySource.class);

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return
	 */

	@Bean
	@Lazy(false)
	public Properties createDBPropertySource() {
		logger.debug("Loading Settings Properties from CACHE(DB)");
		ConfigurableEnvironment configEnv = applicationContext.getBean(ConfigurableEnvironment.class);

		MutablePropertySources propertySources = configEnv.getPropertySources();
		Properties dbSettingsProperties = new Properties();

		propertySources.addLast(new PropertiesPropertySource("DB_SETTINGS", dbSettingsProperties));

		logger.debug("Settings Properties from DB Loaded successfully");

		return dbSettingsProperties;
	}
}
