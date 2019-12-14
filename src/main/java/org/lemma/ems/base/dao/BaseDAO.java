package org.lemma.ems.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * @author RTS Sathish Kumar
 *
 */
public class BaseDAO {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * Inserts into table
	 * 
	 * @param psc
	 * @param autoGenerateKey
	 * @return
	 * 
	 */
	public long createNewRecord(PreparedStatementCreator psc, boolean autoGenerateKey) {

		long returnValue = 0;

		if (autoGenerateKey) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(psc, keyHolder);
			returnValue = (long) keyHolder.getKey();
		} else {
			returnValue = this.jdbcTemplate.update(psc);
		}

		return returnValue;
	}

	/**
	 * @param query
	 * @param params
	 * @return
	 */
	public int executeQuery(final String query, Object[] params) {
		return this.jdbcTemplate.update(query, params);
	}

	/**
	 * @param query
	 */
	public void executeQuery(final String query) {
		jdbcTemplate.execute(query);
	}
}
