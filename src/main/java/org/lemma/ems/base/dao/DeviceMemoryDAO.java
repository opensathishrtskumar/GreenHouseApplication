package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceMemoryDAO")
public class DeviceMemoryDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceMemoryDAO.class);

	/* Queries */
	public static final String MAPPINGS_BY_DEVICEID = "select * from setup.devicememory where deviceid = ? and status != ?";

	public static final String INSERRT_MEMORY_MAPPINGS = "INSERT INTO setup.devicememory(deviceid,memorymapping,status,createdtimestamp) VALUES(?,?,?,?)";

	/* All Constants */
	public static enum Status {

		ACTIVE(1), DELETED(9);

		int status;

		private Status(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}

	/**
	 * @param query
	 * @param params
	 * @return
	 */
	public List<DeviceMemoryDTO> fetchAllMemoryMappings(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<DeviceMemoryDTO>() {

			@Override
			public DeviceMemoryDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				ExtendedDeviceMemoryDTO details = new ExtendedDeviceMemoryDTO();
				details.setUniqueId(resultSet.getLong("id"));
				details.setDeviceId(resultSet.getLong("deviceid"));
				details.setMemoryMapping(resultSet.getString("memorymapping"));
				details.setStatus(resultSet.getInt("status"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));

				return details;
			}
		}, params);
	}

	/**
	 * @param dto
	 * @return
	 */
	public long insertDeviceMemory(DeviceMemoryDTO dto) {

		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERRT_MEMORY_MAPPINGS, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, dto.getDeviceId());
				ps.setString(2, dto.getMemoryMapping());
				ps.setInt(3, dto.getStatus());
				ps.setLong(4, dto.getCreatedTimeStamp());
				return ps;
			}
		};

		return super.createNewRecord(psc, true);
	}
}
