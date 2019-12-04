package fi.forsblom.kapsakki.templateListItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateListItemDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<TemplateListItem> batchInsert(List<TemplateListItem> templateListItems) {
		final String sql = "insert into template_listitem (name, priority, quantity, owner_id, template_id) values (?, ?, ?, ?, ?)";

		this.jdbcTemplate.batchUpdate(
			sql, new BatchPreparedStatementSetter() {
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	                ps.setString(1, templateListItems.get(i).getName());
	                ps.setString(2, templateListItems.get(i).getPriority());
	                ps.setInt(3, templateListItems.get(i).getQuantity());
	                ps.setString(4, templateListItems.get(i).getOwner().getId());
	                ps.setInt(5, templateListItems.get(i).getTemplate().getId());
	            }
	
	            public int getBatchSize() {
	                return templateListItems.size();
	            }
	        });
		return templateListItems;
	}
}
