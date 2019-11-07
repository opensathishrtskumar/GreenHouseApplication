package org.lemma.ems.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("deviceReportMasterDAO")
public class DeviceReportMasterDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceReportMasterDAO.class);

	/* All Queries constants */
	public static final String INSERT_REPORT_MASTER = "INSERT INTO setup.devicereportmaster(deviceid,type,status,createdtimestamp) VALUES (?,?,?,?)";

	public static final String UPDATE_REPORT_MASTER = "update setup.devicereportmaster set type=?,status=? where deviceid=?";
			
	/**
	 * @param query
	 * @param params
	 * @return
	 */
	public List<DeviceDetailsDTO> fetchAllDeviceDetails(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<DeviceDetailsDTO>() {

			@Override
			public DeviceDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				DeviceDetailsDTO details = new DeviceDetailsDTO();

				details.setUniqueId(resultSet.getLong("deviceuniqueid"));
				details.setDeviceId(resultSet.getInt("unitid"));
				details.setDeviceName(resultSet.getString("devicealiasname"));
				details.setBaudRate(resultSet.getInt("baudrate"));
				details.setWordLength(resultSet.getInt("wordlength"));
				details.setStopbit(resultSet.getInt("stopbit"));
				details.setParity(resultSet.getString("parity"));
				details.setRegisterMapping(resultSet.getString("registermapping"));// MSRF/LSRF
				details.setPort(resultSet.getString("port"));
				details.setMethod(resultSet.getString("method"));

				// Load Memory Mappings and its revers into Tree Map

				return details;
			}
		}, params);
	}
}