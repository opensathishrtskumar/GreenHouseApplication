package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.lemma.ems.UI.dto.SchedulesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish  Kumar
 *
 */
@Repository("schedulesDAO")
public class SchedulesDAO {

	private static final Logger logger = LoggerFactory.getLogger(SchedulesDAO.class);
	
	/* All STATUS configuration */
	public static final int ACTIVE = 1;
	public static final int INACTIVE = 2;
	
	
	/* Queries */
	public static final String SELECT_DEVICES =  "select * from setup.schedules where status=?";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<SchedulesDTO> fetchActiveSchedules(){
		return fetchAllSchedules(SELECT_DEVICES, ACTIVE);
	}
	
	/**
	 * @param query
	 * @param status
	 * @return
	 */
	public List<SchedulesDTO> fetchAllSchedules(String query, int status) {

		return this.jdbcTemplate.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement prepareStatement = con.prepareStatement(SELECT_DEVICES);
				prepareStatement.setInt(1, status);
				return prepareStatement;
			}
		}, new RowMapper<SchedulesDTO>() {

			@Override
			public SchedulesDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				SchedulesDTO details = new SchedulesDTO();

				details.setId(resultSet.getLong("id"));
				details.setGroupKey(resultSet.getString("groupkey"));
				details.setJobKey(resultSet.getString("jobkey"));
				details.setDescription(resultSet.getString("description"));
				details.setClassName(resultSet.getString("classname"));
				details.setCronExpression(resultSet.getString("cronexpression"));
				details.setStatus(resultSet.getInt("status"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));
				details.setModifiedTimeStamp(resultSet.getLong("modifiedtimestamp"));
				details.setHashKey(resultSet.getString("hashkey"));
				
				return details;
			}
		});
	}

	/**
	 * @param query
	 * @param params
	 * @return
	 */
	public int executeQuery(final String query, final Object[] params) {

		return this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(query);
				if (params != null) {
					int count = 1;
					for (Object param : params) {
						ps.setObject(count++, param);
					}
				}

				return ps;
			}
		});
	}
}