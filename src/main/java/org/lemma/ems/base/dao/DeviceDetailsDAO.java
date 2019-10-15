package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceDetailsDAO")
public class DeviceDetailsDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDetailsDAO.class);
	
	/* Queries  */
	public static final String RETRIEVE_EMS_DEVICES = "select * from setup.devicedetails where status != ? and type = ?";
	
	/* All Constants */
	public static enum Status {

		ACTIVE(1), DISABLED(2), DELETED(9);

		int status;

		private Status(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}

	/**
	 * EMS is read-only devices BMS is read-write devices
	 *
	 */
	public static enum Type {

		EMS(1), BMS(2);

		int type;

		private Type(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}

	@Autowired
	private DeviceMemoryDAO deviceMemoryDAO;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Loads all devices except {@link DeviceDetailsDAO.Status.DELETED} 
	 * All columnns must be selected from table
	 * 
	 * @param query
	 * @param params
	 * @return
	 */
	public List<DeviceDetailsDTO> fetchDeviceDetails(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<DeviceDetailsDTO>() {

			@Override
			public DeviceDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				DeviceDetailsDTO details = new DeviceDetailsDTO();

				details.setUniqueId(resultSet.getLong("id"));
				details.setDeviceId(resultSet.getInt("deviceid"));
				details.setDeviceName(resultSet.getString("devicedesc"));
				details.setBaudRate(resultSet.getInt("baudrate"));
				details.setWordLength(resultSet.getInt("wordlength"));
				details.setStopbit(resultSet.getInt("stopbit"));
				details.setParity(resultSet.getString("parity"));
				details.setPort(resultSet.getString("port"));
				details.setMethod(resultSet.getString("method"));
				details.setRegisterMapping(resultSet.getString("registermapping"));// MSRF/LSRF
				details.setStatus(resultSet.getInt("status"));
				details.setType(resultSet.getInt("type"));
				details.setCreatedTimeStamp(resultSet.getInt("createdtimestamp"));
				details.setModifiedTimeStamp(resultSet.getInt("modifiedtimestamp"));
				details.setHashKey(resultSet.getString("hashkey"));

				// Load Memory Mappings of current device
				List<DeviceMemoryDTO> fetchAllMemoryMappings = deviceMemoryDAO.fetchAllMemoryMappings(
						DeviceMemoryDAO.MAPPINGS_BY_DEVICEID, new Object[] { details.getUniqueId() });
				details.setMemoryMappings(fetchAllMemoryMappings);

				return details;
			}
		}, params);
	}

	public int executeQuery(final String query, final Object[] params) {

		return this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(query);
				logger.trace("StmtCreator for query : {} with params : {}", query, Arrays.toString(params));
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

	public void execute(final String query) {
		jdbcTemplate.execute(query);
	}

	public int execute(final String query, Object[] params) {
		jdbcTemplate.execute(query, new PreparedStatementCallback<Object>() {
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (params != null) {
					int count = 1;
					for (Object param : params) {
						ps.setObject(count++, param);
					}
				}

				return ps;
			}
		});
		return 0;
	}
}