package org.lemma.ems.base.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * @author RTS Sathish Kumar
 *
 */
public class BaseDAO {

	protected JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Inserts into table
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

}
