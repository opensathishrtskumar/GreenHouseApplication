
package org.lemma.infra.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataBaseConfig {

	/**
	 * Allows repositories to access RDBMS data using the JDBC API.
	 */
	@Bean
	@Primary
	@Autowired
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * Allows repositories to access RDBMS data using the JDBC API.
	 */
	@Bean(name = { "namedParameterJdbcTemplate" })
	@Autowired
	public NamedParameterJdbcTemplate jdbcNamedTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
