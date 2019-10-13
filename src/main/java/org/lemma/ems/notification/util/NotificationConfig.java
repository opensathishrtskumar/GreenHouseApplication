package org.lemma.ems.notification.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

/**
 * @author RTS Sathish Kumar
 *
 */
@Configuration
public class NotificationConfig {

	private static final Logger logger = LoggerFactory.getLogger(NotificationConfig.class);

	/**
	 * @return
	 * @throws VelocityException
	 * @throws IOException
	 */
	@Bean
	public VelocityEngine createVelocityEngineFactoryBean() throws VelocityException, IOException {
		VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		bean.setVelocityProperties(props);
		bean.afterPropertiesSet();
		return bean.createVelocityEngine();
	}

	@Bean
	@Lazy(true)
	public JavaMailSenderImpl createJavaMailSenderImpl() {
		JavaMailSenderImpl bean = new JavaMailSenderImpl();

		/* actual values are replaced during APP_STARTUP_EVENT */
		String host = "smtp.host.com";
		int port = 587;
		String userName = "username@domain.com";
		String password = "credentials";

		bean.setHost(host);
		bean.setPort(port);
		bean.setUsername(userName);
		bean.setPassword(password);

		Properties javaMailProps = new Properties();

		javaMailProps.put("mail.smtp.host", host);
		javaMailProps.put("mail.smtp.port", port);
		javaMailProps.put("mail.debug", "false");
		javaMailProps.put("mail.smtp.auth", "true");
		javaMailProps.put("mail.smtp.starttls.enable", "true");
		javaMailProps.put("mail.mime.charset", "UTF-8");
		javaMailProps.put("mail.transport.protocol", "smtp");
		javaMailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		bean.setJavaMailProperties(javaMailProps);

		return bean;
	}
}
