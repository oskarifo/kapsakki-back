package fi.forsblom.kapsakki.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ListsRowMapper implements RowMapper<List> {
	@Override
	public List mapRow(ResultSet rs, int rowNum) throws SQLException {
		List list = new List();
		 
    	list.setId(rs.getInt("LIST.ID"));
		list.setName(rs.getString("LIST.NAME"));
		list.setDescription(rs.getString("LIST.DESCRIPTION"));
		
        return list;
	}
}
