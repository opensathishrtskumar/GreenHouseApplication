package org.lemma.ems.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lemma.ems.base.dao.dto.UserDetailsDTO;
import org.lemma.ems.ui.model.ChangePasswordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ems.security.Security;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository
public class UserDetailsDAO extends BaseDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsDAO.class);

	/* ALL STATUS details of UserDetails table */
	public static enum Status {
		ACTIVE(1), INACTIVE(2), DELETED(3);
		int status;

		private Status(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}

	/* Queries related to UserDetails TABLE */
	private static final String AUTHENTICATE_USER = "select ud.id,ud.name,ud.emailid,ud.password,ud.roleid,ud.mobilenumber,ud.status,ud.createdtimestamp,"
			+ "ud.modifiedtimestamp,ud.hashkey, ur.roletype,ur.privileges,ur.createdtimestamp,ur.hashkey "
			+ "from setup.userdetails ud,setup.userroles ur where ud.emailid=? and ud.password=? and ud.status=? and ud.roleid = ur.id";

	private static final String UPDATE_PASSWORD = "update setup.user_credential set credential = ? where id=?";

	public static final String RETRIEVE_USERS = "select * from setup.userdetails";

	public static final String INSERT_USER = "insert into setup.userdetails "
			+ "(name, emailid, password, roleid, mobilenumber,status,createdtimestamp,modifiedtimestamp,hashkey)"
			+ " values(?,?,?,?,?,?,?,?,?)";

	@Autowired
	private Security security;

	public UserDetailsDTO authenticate(final String username, final String password, final int status)
			throws Exception {
		try {
			String encryptedPassword = security.encrypt(password);
			logger.trace("Encrypted password {}", encryptedPassword);

			return jdbcTemplate.queryForObject(AUTHENTICATE_USER, new Object[] { username, encryptedPassword, status },
					new RowMapper<UserDetailsDTO>() {
						@Override
						public UserDetailsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
							logger.trace("row index in authenticate :  {}", rowNum);

							UserDetailsDTO userDetails = new UserDetailsDTO();

							userDetails.setId(rs.getLong("id"));
							userDetails.setName(rs.getString("name"));
							userDetails.setEmailId(rs.getString("emailid"));
							userDetails.setPassword(rs.getString("password"));
							userDetails.setRoleId(rs.getInt("roleid"));
							userDetails.setMobileNumber(rs.getString("mobilenumber"));
							userDetails.setStatus(rs.getInt("status"));
							userDetails.setCreatedTimeStamp(rs.getLong("createdtimestamp"));
							userDetails.setModifiedTimeStamp(rs.getLong("modifiedtimestamp"));
							userDetails.setHashKey(rs.getString("hashkey"));
							/* User Role and Privilege details */
							userDetails.setRoleType(rs.getString("roletype"));
							userDetails.setPrivileges(rs.getLong("privileges"));

							return userDetails;
						}
					});

		} catch (Exception e) {
			logger.error("authenticating exception {}", e);
			throw e;
		}
	}

	/**
	 * @param form
	 * @return
	 */
	public List<UserDetailsDTO> fetchUserDetails(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<UserDetailsDTO>() {

			@Override
			public UserDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				UserDetailsDTO details = new UserDetailsDTO();

				details.setId(resultSet.getLong("id"));
				details.setName(resultSet.getString("name"));
				details.setEmailId(resultSet.getString("emailid"));
				details.setPassword(resultSet.getString("password"));
				details.setRoleId(resultSet.getInt("roleid"));
				details.setMobileNumber(resultSet.getString("mobilenumber"));
				details.setStatus(resultSet.getInt("status"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));
				details.setModifiedTimeStamp(resultSet.getLong("modifiedtimestamp"));
				details.setHashKey(resultSet.getString("hashkey"));
				// Select all columns
				return details;
			}
		}, params);
	}

	public int updatePassword(ChangePasswordForm form) {
		return super.executeQuery(UPDATE_PASSWORD, new Object[] { form.getConfirmPassword(), form.getId() });
	}

	public int insertUserDetails(UserDetailsDTO dto) {
		return super.executeQuery(INSERT_USER,
				new Object[] { dto.getName(), dto.getEmailId(), dto.getPassword(), dto.getRoleId(),
						dto.getMobileNumber(), dto.getStatus(), dto.getCreatedTimeStamp(), dto.getModifiedTimeStamp(),
						dto.getHashKey() });
	}
}
