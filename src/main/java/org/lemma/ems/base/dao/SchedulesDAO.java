package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lemma.ems.base.dao.dto.SchedulesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("schedulesDAO")
public class SchedulesDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(SchedulesDAO.class);

	/* All STATUS configuration */
	
	public enum Status{
		ACTIVE(1),INACTIVE(2),ONETIMEJOB(3);
		
		int status;
		
		private Status(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}
	

	/* Queries */
	public static final String SELECT_SCHEDULES_BY_STATUS = "select * from setup.schedules where status=?";
	public static final String SELECT_ALL_SCHEDULES = "select * from setup.schedules";

	public List<SchedulesDTO> fetchAllSchedulesByStatus() {
		return fetchAllSchedulesByStatus(SELECT_SCHEDULES_BY_STATUS, Status.ACTIVE.getStatus());
	}

	public List<SchedulesDTO> fetchAllSchedules() {
		return fetchAllSchedules(SELECT_ALL_SCHEDULES);
	}

	/**
	 * @param query
	 * @param status
	 * @return
	 */
	private List<SchedulesDTO> fetchAllSchedulesByStatus(String query, int status) {

		return this.jdbcTemplate.query(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement prepareStatement = con.prepareStatement(query);
				prepareStatement.setInt(1, status);
				return prepareStatement;
			}
		}, new ScheduledDTOMapper());
	}

	/**
	 * @param query
	 * @param status
	 * @return
	 */
	private List<SchedulesDTO> fetchAllSchedules(String query) {
		return this.jdbcTemplate.query(query, new ScheduledDTOMapper());
	}

	class ScheduledDTOMapper implements RowMapper<SchedulesDTO> {

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
	}

	/**
	 * @param query
	 * @param params
	 * @return
	 */
	public int executeQuery(final String query, final Object[] params) {
		return super.executeQuery(query, params);
	}
}