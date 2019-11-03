package org.lemma.ems.base.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.lemma.ems.base.dao.dto.PageAccessDetailsDTO;
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
public class PageAccessDetailsDAO extends BaseDAO {

	public static final String UPDATE_ROLE = "UPDATE setup.pageaccessdetails SET resourcename=?, resourceurl=?, bitposition=?, createdtimestamp=? WHERE id=?";
	
	private static final Logger logger = LoggerFactory.getLogger(PageAccessDetailsDAO.class);

	@Autowired
	private Security security;

	public List<PageAccessDetailsDTO> fetchPageAccessDetails(String query, Object[] params) {

		return this.jdbcTemplate.query(query, new RowMapper<PageAccessDetailsDTO>() {
			
			@Override
			public PageAccessDetailsDTO mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
				PageAccessDetailsDTO details = new PageAccessDetailsDTO();

				details.setUniqueId(resultSet.getLong("id"));
				details.setResourceName(resultSet.getString("resourcename"));
				details.setResourceURL(resultSet.getString("resourceurl"));
				details.setBitPosition(resultSet.getInt("bitposition"));
				details.setCreatedTimeStamp(resultSet.getLong("createdtimestamp"));
				// Select all columns
				return details;
			}
		}, params);
	}
	public int updatePageAccessDetails(PageAccessDetailsDTO dto) {
		return super.executeQuery(UPDATE_ROLE,
				new Object[] { dto.getResourceName(), dto.getResourceURL(), dto.getBitPosition(), dto.getCreatedTimeStamp(), dto.getUniqueId() });
	}

}
