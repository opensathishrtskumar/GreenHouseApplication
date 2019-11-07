package org.lemma.ems.base.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("deviceReportMasterDAO")
public class DeviceReportMasterDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceReportMasterDAO.class);

	/* All Queries constants */
	public static final String INSERT_REPORT_MASTER = "INSERT INTO setup.devicereportmaster(deviceid,type,status,createdtimestamp) VALUES (?,?,?,?)";

	public static final String UPDATE_REPORT_MASTER = "update setup.devicereportmaster set type=?,status=? where deviceid=?";
	
	public static final String SELECT_REPORT_MASTER_BY_DEVICEID = "select * from setup.devicereportmaster where deviceid=?";
	
	
	
	
}