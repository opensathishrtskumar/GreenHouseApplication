package org.lemma.ems.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceMemoryDAO")
public class DeviceMemoryDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceMemoryDAO.class);

	/* Queries */
	public static final String MAPPINGS_BY_DEVICEID = "select * from setup.devicememory where deviceid = ?";

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
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
				DeviceMemoryDTO details = new DeviceMemoryDTO();
				details.setUniqueId(resultSet.getLong("id"));
				details.setDeviceId(resultSet.getLong("deviceid"));
				details.setMemoryMapping(resultSet.getString("memorymapping"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));

				return details;
			}
		}, params);
	}

	public void execute(final String query) {
		jdbcTemplate.execute(query);
	}
}
