package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.lemma.ems.base.dao.dto.DeviceReportMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

@Repository("deviceReportMasterDAO")
public class DeviceReportMasterDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceReportMasterDAO.class);

	/* All Queries constants */
	public static final String INSERT_REPORT_MASTER = "INSERT INTO setup.devicereportmaster(deviceid,type,status,createdtimestamp) VALUES (?,?,?,?)";

	public static final String UPDATE_REPORT_MASTER = "update setup.devicereportmaster set type=?,status=? where deviceid=?";

	public static final String SELECT_REPORT_MASTER_BY_DEVICEID = "select * from setup.devicereportmaster where deviceid=?";

	public long insertDeviceReport(DeviceReportMasterDTO dto) {
		return super.createNewRecord(createPs(dto), true);
	}

	/**
	 * @param dto
	 * @return
	 */
	public PreparedStatementCreator createPs(DeviceReportMasterDTO dto) {

		Object[] params = new Object[] { dto.getDeviceid(), dto.getType(), dto.getStatus(), dto.getCreatedtimestamp() };

		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_REPORT_MASTER, Statement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < params.length; i++) {
					ps.setObject(i + 1, params[i]);
				}
				
				return ps;
			}
		};
	}
	
	/**
	 * @param dto
	 * @return
	 */
	public int updateDeviceReport(DeviceReportMasterDTO dto) {
		return executeQuery(UPDATE_REPORT_MASTER, new Object[] {dto.getType(),dto.getStatus(),dto.getDeviceid()});
	}
	
}