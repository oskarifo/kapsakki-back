package fi.forsblom.kapsakki.template;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TemplatesRowMapper implements RowMapper<Template>  {

	@Override
	public Template mapRow(ResultSet rs, int rowNum) throws SQLException {
		Template template = new Template();
		 
		template.setId(rs.getInt("template.id"));
		template.setName(rs.getString("template.name"));
		
        return template;
	}
}
