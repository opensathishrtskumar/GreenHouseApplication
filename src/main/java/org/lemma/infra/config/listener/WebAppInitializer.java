package org.lemma.infra.config.listener;

import static org.springframework.web.context.ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.lemma.infra.config.DBConfigPropertySourceInitializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;

/**
 * 
 * Add Servlet initialization parameters
 * 
 * @author RTS Sathish Kumar
 *
 */
@Component
public class WebAppInitializer implements WebApplicationInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.
	 * ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		String initializerClasses = servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);

		String propertySourceInitClassName = DBConfigPropertySourceInitializer.class.getName();

		if (StringUtils.hasText(initializerClasses)) {
			initializerClasses += " " + propertySourceInitClassName;
		} else {
			initializerClasses = propertySourceInitClassName;
		}

		servletContext.setInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM, initializerClasses);
	}
}