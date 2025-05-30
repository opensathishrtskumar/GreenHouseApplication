package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemma.ems.base.dao.constants.QueryConstants;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.dao.dto.SettingsDTO;
import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("pollingDetailsDAO")
public class PollingDetailsDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(PollingDetailsDAO.class);
	
	/* All Queries constants*/
	public static final String INSERT_POLLING_DETAILS = "insert into setup.pollingdetails"
			+ "(uniqueid, polledon, voltage_bn,voltage_br,voltage_rn,voltage_ry,voltage_yb,voltage_yn,voltage_avg_ll,"
			+ "voltage_avg_ln,r_current,y_current,b_current,current_avg,frequency,power_factor,w1,w2,w3,wh,w_avg,va1,va2,va3,vah,va_avg)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_MONTHLY_POLLING_DETAILS = "insert into monthly.pollingdetails"
			+ "(uniqueid, polledon, voltage_bn,voltage_br,voltage_rn,voltage_ry,voltage_yb,voltage_yn,voltage_avg_ll,"
			+ "voltage_avg_ln,r_current,y_current,b_current,current_avg,frequency,power_factor,w1,w2,w3,wh,w_avg,va1,va2,va3,vah,va_avg)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String FETCH_DAILY_CUMULATIVE_DETAILS = "SELECT DATE_FORMAT(FROM_UNIXTIME(p.polledon/1000),'%d-%k')  timeformat,p.*" + 
			"	FROM setup.pollingdetails p" + 
			"    WHERE  " + 
			"		p.uniqueid in (select uniqueid from setup.devicedetails where status = ? and type = ?) " + 
			"		AND p.polledon BETWEEN ? AND ? " + 
			"	GROUP BY p.uniqueid,timeformat" + 
			"	ORDER BY p.uniqueid,CAST(p.polledon AS UNSIGNED) ASC";	

	public static final String FETCH_RANGE_CUMULATIVE_DETAILS = "SELECT * FROM setup.pollingdetails as sp WHERE sp.polledon BETWEEN ? AND ?" + 
			"	UNION ALL " + 
			"	SELECT * FROM monthly.pollingdetails as mp WHERE mp.polledon BETWEEN ? AND ?";
	
/*	public static final String FETCH_MONTHLY_CUMULATIVE_DETAILS = 
			"select * from (SELECT DATE_FORMAT(FROM_UNIXTIME(p.polledon/1000),'%Y-%d')  timeformat,p.*" + 
			" FROM setup.pollingdetails p WHERE " + 
			" p.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type) " + 
			" AND p.polledon BETWEEN :startofday AND :endofday "  
			+ " UNION " + 
			" SELECT DATE_FORMAT(FROM_UNIXTIME(mp.polledon/1000),'%Y-%d')  timeformat,mp.*" + 
			" FROM setup.pollingdetails mp WHERE " + 
			" mp.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type) " + 
			" AND mp.polledon BETWEEN :startofday AND :endofday) temp " + 
			" GROUP BY temp.uniqueid,temp.timeformat " + 
			" ORDER BY temp.uniqueid,CAST(temp.polledon AS UNSIGNED) ASC";*/	

	public static final String FETCH_MONTHLY_CUMULATIVE_DETAILS =
			"(SELECT DATE_FORMAT(FROM_UNIXTIME(p.polledon/1000),'%Y-%d')  timeformat,p.*" + 
			"	FROM setup.pollingdetails p WHERE" + 
			"	p.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type)" + 
			"	AND p.polledon BETWEEN :startofday AND :endofday" + 
			"	GROUP BY uniqueid,timeformat" + 
			"	ORDER BY uniqueid,CAST(polledon AS UNSIGNED) ASC)" + 
			"UNION" + 
			"(SELECT DATE_FORMAT(FROM_UNIXTIME(mp.polledon/1000),'%Y-%d')  timeformat,mp.*" + 
			"	FROM monthly.pollingdetails mp WHERE" + 
			"	mp.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type)" + 
			"	AND mp.polledon BETWEEN :startofday AND :endofday" + 
			"	GROUP BY uniqueid,timeformat" + 
			"	ORDER BY uniqueid,CAST(polledon AS UNSIGNED) ASC)";
	
	/*
(SELECT DATE_FORMAT(FROM_UNIXTIME(p.polledon/1000),'%Y-%d')  timeformat,p.*
	FROM setup.pollingdetails p WHERE
	p.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type)
	AND p.polledon BETWEEN :startofday AND :endofday
	GROUP BY uniqueid,timeformat
	ORDER BY uniqueid,CAST(polledon AS UNSIGNED) ASC)
UNION
(SELECT DATE_FORMAT(FROM_UNIXTIME(mp.polledon/1000),'%Y-%d')  timeformat,mp.*
	FROM monthly.pollingdetails mp WHERE
	mp.uniqueid in (select uniqueid from setup.devicedetails where status = :status and type = :type)
	AND mp.polledon BETWEEN :startofday AND :endofday
	GROUP BY uniqueid,timeformat
	ORDER BY uniqueid,CAST(polledon AS UNSIGNED) ASC)

	 * */
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

				details.setUniqueId(resultSet.getLong("uniqueid"));
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

	public int insertPollingDetails(String query, Object[] params) {
		return super.executeQuery(query, params);
	}

	public List<SettingsDTO> fetchSettings() {

		return this.jdbcTemplate.query(QueryConstants.FETCH_SETTINGS, new RowMapper<SettingsDTO>() {
			@Override
			public SettingsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				SettingsDTO dto = new SettingsDTO();
				dto.setId(rs.getLong("id"));
				/*
				 * dto.setConfigName(rs.getString("configname"));
				 * dto.setConfigValue(rs.getString("configvalue"));
				 */

				return dto;
			}

		});
	}

	public String fetchVariable(String query) {
		return this.jdbcTemplate.queryForObject(query, String.class);
	}

	public List<PollingDetailsDTO> fetchMainIncomerDailySummary(String query, Object[] params) {

		List<PollingDetailsDTO> details = this.jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				con.setAutoCommit(false);
				PreparedStatement statement = con.prepareStatement(query);

				int index = 1;
				for (Object obj : params)
					statement.setObject(index++, obj);

				statement.setFetchSize(Integer.MIN_VALUE);
				logger.debug(" prepared statement created {} ", Arrays.toString(params));
				return statement;
			}
		}, new RowMapper<PollingDetailsDTO>() {

			@Override
			public PollingDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				PollingDetailsDTO details = new PollingDetailsDTO();
				details.setUniqueId(rs.getLong("uniqueid"));
				details.setPolledOn(rs.getLong("polledon"));
				// details.setUnitresponse(rs.getString("unitresponse"));
				return details;
			}
		});

		return details;
	}

	public List<String> loadFailedDevicesNames() {

		return this.jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				con.setAutoCommit(false);
				PreparedStatement statement = con.prepareStatement(QueryConstants.FAILED_DEVICES);
				statement.setFetchSize(Integer.MIN_VALUE);
				return statement;
			}
		}, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);// Load only device name to send mail
			}
		});
	}

	/**
	 * @param dtopoll
	 * @return
	 */
	public int insertPollingDetails(PollingDetailsDTO dtopoll) {
		return super.executeQuery(INSERT_POLLING_DETAILS, new Object[] { dtopoll.getUniqueId(),
				dtopoll.getPolledOn(), dtopoll.getVoltage_bn(), dtopoll.getVoltage_br(), dtopoll.getVoltage_rn(),
				dtopoll.getVoltage_ry(), dtopoll.getVoltage_yb(), dtopoll.getVoltage_yn(), dtopoll.getVoltage_avg_ll(),
				dtopoll.getVoltage_avg_ln(), dtopoll.getR_current(), dtopoll.getY_current(), dtopoll.getB_current(),
				dtopoll.getCurrent_avg(), dtopoll.getFrequency(), dtopoll.getPower_factor(), dtopoll.getW1(),
				dtopoll.getW2(), dtopoll.getW3(), dtopoll.getWh(), dtopoll.getW_avg(), dtopoll.getVa1(),
				dtopoll.getVa2(), dtopoll.getVa3(), dtopoll.getVah(), dtopoll.getVa_avg() });
	}

	public int insertMonthlyPollingDetails(PollingDetailsDTO dtopoll) {
		return super.executeQuery(INSERT_MONTHLY_POLLING_DETAILS, new Object[] { dtopoll.getUniqueId(),
				dtopoll.getPolledOn(), dtopoll.getVoltage_bn(), dtopoll.getVoltage_br(), dtopoll.getVoltage_rn(),
				dtopoll.getVoltage_ry(), dtopoll.getVoltage_yb(), dtopoll.getVoltage_yn(), dtopoll.getVoltage_avg_ll(),
				dtopoll.getVoltage_avg_ln(), dtopoll.getR_current(), dtopoll.getY_current(), dtopoll.getB_current(),
				dtopoll.getCurrent_avg(), dtopoll.getFrequency(), dtopoll.getPower_factor(), dtopoll.getW1(),
				dtopoll.getW2(), dtopoll.getW3(), dtopoll.getWh(), dtopoll.getW_avg(), dtopoll.getVa1(),
				dtopoll.getVa2(), dtopoll.getVa3(), dtopoll.getVah(), dtopoll.getVa_avg() });
	}
	
	public List<PollingDetailsDTO> fetchDailyPolledSummary(String query, Object[] params) {

		List<PollingDetailsDTO> details = this.jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				con.setAutoCommit(false);
				PreparedStatement statement = con.prepareStatement(query);

				int index = 1;
				for (Object obj : params) 
					statement.setObject(index++, obj);

				//statement.setFetchSize(Integer.MIN_VALUE);
				logger.debug(" prepared statement created {} ", Arrays.toString(params));
				return statement;
			}
		}, new RowMapper<PollingDetailsDTO>() {

			@Override
			public PollingDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				PollingDetailsDTO details = new PollingDetailsDTO();
				details.setTimeFormat(rs.getString("timeformat"));
				details.setUniqueId(rs.getLong("uniqueid"));
				details.setPolledOn(rs.getLong("polledon"));
				details.setVoltage_br(rs.getLong("voltage_br"));
				return details;
			}
		});

		return details;
	}	

	public List<PollingDetailsDTO> fetchMonthlyPolledSummary(String queryStr, int status, int type, long startOfDay,
			long endOfDay) {

		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("type", type);
		map.put("startofday", startOfDay);
		map.put("endofday", endOfDay);

		return super.namedParameterJdbcTemplate.query(queryStr, map, new RowMapper<PollingDetailsDTO>() {

			@Override
			public PollingDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				PollingDetailsDTO details = new PollingDetailsDTO();
				details.setTimeFormat(rs.getString("timeformat"));
				details.setUniqueId(rs.getLong("uniqueid"));
				details.setPolledOn(rs.getLong("polledon"));
				details.setVoltage_br(rs.getLong("voltage_br"));
				return details;
			}
		});

	}
	
	public List<PollingDetailsDTO> fetchRangePolledSummary(String query, Object[] params) {

		List<PollingDetailsDTO> details = this.jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				con.setAutoCommit(false);
				PreparedStatement statement = con.prepareStatement(query);

				int index = 1;
				for (Object obj : params) 
					statement.setObject(index++, obj);

				//statement.setFetchSize(Integer.MIN_VALUE);
				logger.debug(" prepared statement created {} ", Arrays.toString(params));
				return statement;
			}
		}, new RowMapper<PollingDetailsDTO>() {

			@Override
			public PollingDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				PollingDetailsDTO details = new PollingDetailsDTO();
				details.setTimeFormat(rs.getString("timeformat"));
				details.setUniqueId(rs.getLong("uniqueid"));
				details.setPolledOn(rs.getLong("polledon"));
				details.setVoltage_br(rs.getLong("voltage_br"));
				return details;
			}
		});

		return details;
	}		
	
}