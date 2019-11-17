package org.lemma.ems.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceReportMasterDTO;
import org.lemma.ems.base.dao.dto.UserRolesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ems.security.Security;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository
public class UserRolesDAO extends BaseDAO {

	public static final String INSERT_ROLE = "insert into setup.userroles "
			+ "(roletype, privileges,createdtimestamp,modifiedtimestamp,hashkey) values(?,?,?,?,?)";
	
	public static final String UPDATE_ROLE = "UPDATE setup.userroles SET privileges=?, modifiedtimestamp=? WHERE id=?";

	private static final Logger logger = LoggerFactory.getLogger(UserRolesDAO.class);

	@Autowired
	private Security security;

	public List<UserRolesDTO> fetchUserRoles(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<UserRolesDTO>() {

			@Override
			public UserRolesDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				UserRolesDTO details = new UserRolesDTO();

				details.setUniqueId(resultSet.getLong("id"));
				details.setRoleType(resultSet.getString("roletype"));
				details.setPrivileges(resultSet.getInt("privileges"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));
				details.setModifiedTimeStamp(resultSet.getLong("modifiedtimestamp"));
				details.setHashKey(resultSet.getString("hashkey"));
				// Select all columns
				return details;
			}
		}, params);
	}

	public int insertUserRoles(UserRolesDTO dto) {
		return super.executeQuery(INSERT_ROLE, new Object[] { dto.getRoleType(), dto.getPrivileges(),
				dto.getCreatedTimeStamp(), dto.getModifiedTimeStamp(), dto.getHashKey() });
	}
	
	public int updateUserRoles(UserRolesDTO dto) {
		return executeQuery(UPDATE_ROLE, new Object[] {dto.getPrivileges(),dto.getModifiedTimeStamp(),dto.getUniqueId()});
	}
	
}
