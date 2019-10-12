package org.lemma.ems.base.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.lemma.ems.UI.dto.SettingsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ems.security.Security;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository
public class SettingsDAO {

	private static final Logger logger = LoggerFactory.getLogger(SettingsDAO.class);
	
	public static enum SettingsGroup{
		EMAIL("EMAIL");
		
		String groupKey;
		
		private SettingsGroup(String groupKey) {
			this.groupKey = groupKey;
		}
	}
	
	public static enum EMAILGroup{
		HOST("smtp.host"),
		PORT("smtp.port"),
		USERNAME("smtp.username"),
		PASSWORD("smtp.password"),
		BCC("smtp.bcc.mail"),
		CC("smtp.cc.mail"),
		TO("smtp.to.mail");
		
		String mailKey;
		
		private EMAILGroup(String mailKey) {
			this.mailKey = mailKey;
		}
	}
	
	private final JdbcTemplate jdbcTemplate;
	/* ALL details of SetttingsDAO table */
	
	

	/* Queries related to SetttingsDAO TABLE */
	private static final String FETCH_SETTINGS = "select * from setup.settings";

	@Autowired
	private Security security;

	@Inject
	public SettingsDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<SettingsDTO> fetchSettings() throws Exception {

		return jdbcTemplate.query(FETCH_SETTINGS, new RowMapper<SettingsDTO>() {
			@Override
			public SettingsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				SettingsDTO settings = new SettingsDTO();
				settings.setId(rs.getLong("id"));
				settings.setGroupName(rs.getString("groupname"));
				settings.setConfigName(rs.getString("configname"));
				settings.setConfigValue(rs.getString("configvalue"));
				settings.setCreatedTimeStamp(rs.getLong("createdtimestamp"));
				settings.setModifiedTimeStamp(rs.getLong("modifiedtimestamp"));
				settings.setHashKey(rs.getString("hashkey"));

				// FIXME: Verify settings hashkey, throw exception if mismatch
				return settings;
			}
		});
	}
	
	public int updateSettings(SettingsDTO settings) throws Exception {

		return jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				CallableStatement prepareCall = con.prepareCall("");
				//TODO
				return prepareCall;
			}
		});
	}

}
