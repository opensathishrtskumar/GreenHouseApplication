package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.DeviceReportMasterDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceDetailsDAO")
public class DeviceDetailsDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDetailsDAO.class);

	/* Queries */
	public static final String RETRIEVE_EMS_DEVICES = "select * from setup.devicedetails where status != ? and type = ?";

	public static final String INSERT_DEVICES = "INSERT INTO setup.devicedetails(deviceid,devicedesc,baudrate,wordlength,stopbit,parity,"
			+ "port,method,registermapping,encoding,status,type,createdtimestamp,modifiedtimestamp,hashkey) "
			+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String RETRIEVE_EMS_ACTIVE_DEVICES = "select * from setup.devicedetails where status = ? and type = ?";

	public static final String UPDATE_DEVICE = "update setup.devicedetails set status=?,modifiedtimestamp=?,hashkey=?  where id=?";

	public static final String RETRIEVES_DEVICES_4_REPORT = "select d.*,r.id as rid,r.deviceid as rdeviceid,r.type as rtype, "
			+ "r.status as rstatus,r.createdtimestamp as  rcreatedtimestamp  from setup.devicedetails d left join setup.devicereportmaster r"
			+ " on d.id = r.deviceid where d.status = ? and d.type = ?";

	/* All Constants */
	public enum Status {

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
	public enum Type {

		EMS(1), BMS(2);

		int type;

		private Type(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}
	}

	private DeviceMemoryDAO deviceMemoryDAO;

	@Autowired
	public void setDeviceMemoryDAO(DeviceMemoryDAO deviceMemoryDAO) {
		this.deviceMemoryDAO = deviceMemoryDAO;
	}

	public List<DeviceDetailsDTO> fetchEMSActiveDevices() {
		return fetchDeviceDetails(RETRIEVE_EMS_ACTIVE_DEVICES,
				new Object[] { Status.ACTIVE.getStatus(), Type.EMS.getType() });
	}

	/**
	 * Loads all devices except {@link DeviceDetailsDAO.Status.DELETED} All columnns
	 * must be selected from table
	 * 
	 * @param query
	 * @param params
	 * @return
	 */
	public List<DeviceDetailsDTO> fetchDeviceDetails(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<DeviceDetailsDTO>() {

			@Override
			public DeviceDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				DeviceDetailsDTO details = mapDeviceDetail(resultSet);

				// Load Memory Mappings of current device
				List<ExtendedDeviceMemoryDTO> fetchAllMemoryMappings = deviceMemoryDAO.fetchAllMemoryMappings(
						DeviceMemoryDAO.MAPPINGS_BY_DEVICEID,
						new Object[] { details.getUniqueId(), DeviceMemoryDAO.Status.DELETED.getStatus() });
				details.setMemoryMappings(fetchAllMemoryMappings);

				return details;
			}

		}, params);
	}

	
	public List<DeviceDetailsDTO> fetchDevice4ReportMaster() {
		
		final String query = RETRIEVES_DEVICES_4_REPORT;
		final Object[] params = new Object[] {Status.ACTIVE.getStatus(), Type.EMS.getType()};
		
		return this.jdbcTemplate.query(query, new RowMapper<DeviceDetailsDTO>() {

			@Override
			public DeviceDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				DeviceDetailsDTO details = mapDeviceDetail(resultSet);
				details.setReportMaster(mapReportMasterDetails(resultSet));
				return details;
			}

		}, params);
	}
	
	private DeviceReportMasterDTO mapReportMasterDetails(ResultSet resultSet) throws SQLException {
		DeviceReportMasterDTO details = new DeviceReportMasterDTO();
		details.setId(resultSet.getLong("rid"));
		details.setDeviceid(resultSet.getLong("rdeviceid"));
		details.setType(resultSet.getInt("rtype"));
		details.setStatus(resultSet.getInt("rstatus"));
		details.setCreatedtimestamp(resultSet.getLong("rcreatedtimestamp"));
		return details;
	}
	
	private DeviceDetailsDTO mapDeviceDetail(ResultSet resultSet) throws SQLException {
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
		details.setEncoding(resultSet.getString("encoding"));
		details.setStatus(resultSet.getInt("status"));
		details.setType(resultSet.getInt("type"));
		details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));
		details.setModifiedTimeStamp(resultSet.getLong("modifiedtimestamp"));
		details.setHashKey(resultSet.getString("hashkey"));
		return details;
	}

	/**
	 * @param device
	 * @return
	 */
	public long insertDeviceDetails(DeviceDetailsDTO device) {

		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_DEVICES, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, device.getDeviceId());
				ps.setString(2, device.getDeviceName());
				ps.setInt(3, device.getBaudRate());
				ps.setInt(4, device.getWordLength());
				ps.setInt(5, device.getStopbit());
				ps.setString(6, device.getParity());
				ps.setString(7, device.getPort());
				ps.setString(8, device.getMethod());
				ps.setString(9, device.getRegisterMapping());
				ps.setString(10, device.getEncoding());
				ps.setInt(11, device.getStatus());
				ps.setInt(12, device.getType());
				ps.setLong(13, device.getCreatedTimeStamp());
				ps.setLong(14, device.getModifiedTimeStamp());
				ps.setString(15, device.getHashKey());
				return ps;
			}
		};

		long deviceUniqueId = super.createNewRecord(psc, true);

		// insert DeviceMemoryMappings
		for (DeviceMemoryDTO memory : device.getMemoryMappings()) {
			memory.setDeviceId(deviceUniqueId);
			deviceMemoryDAO.insertDeviceMemory(memory);
		}

		return deviceUniqueId;
	}

	public int updateDeviceDetails(DeviceDetailsDTO dto) {
		return super.executeQuery(UPDATE_DEVICE,
				new Object[] { dto.getStatus(), dto.getModifiedTimeStamp(), dto.getHashKey(), dto.getUniqueId() });
	}
}
