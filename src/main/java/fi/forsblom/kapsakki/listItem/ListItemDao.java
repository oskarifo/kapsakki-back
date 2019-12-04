package fi.forsblom.kapsakki.listItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fi.forsblom.kapsakki.exceptions.NotFoundException;

@Repository
public class ListItemDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	public ListItem findByIdAndOwnerId(int id, int ownerId) {
		final String sql =  "select listitem.id, listitem.name, listitem.quantity, listitem.priority, listitem.checked, " +
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from listitem " +
				"inner join user on user.id = listitem.owner_id " +
			 	"where listitem.id = ? and listitem.owner_id = ?;";
		try {
			return jdbcTemplate.queryForObject(
				sql, new Object[]{id, ownerId}, new ListItemRowMapper());	
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("ListItem not found");
		}
	}
	
	public ListItem findById(int id) {
		final String sql =  "select listitem.id, listitem.name, listitem.quantity, listitem.priority, listitem.checked, " +
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from listitem " +
				"inner join user on user.id = listitem.owner_id " +
			 	"where listitem.id = ?;";
		try {
			return jdbcTemplate.queryForObject(
				sql, new Object[]{id}, new ListItemRowMapper());	
		} catch (EmptyResultDataAccessException e) {
			throw new NotFoundException("ListItem not found");
		}
	}
	
	public boolean delete(ListItem listItem) {
		return false;
	}
	
	public List<ListItem> batchUpdate(List<ListItem> listItems) {
		final String sql = "update listitem set name = ?, priority = ?, checked  = ?, quantity = ? where id = ?;";
		this.jdbcTemplate.batchUpdate(
			sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, listItems.get(i).getName());
                ps.setString(2, listItems.get(i).getPriority());
                ps.setBoolean(3, listItems.get(i).isChecked());
                ps.setInt(4, listItems.get(i).getQuantity());
                ps.setInt(5, listItems.get(i).getId());
            }

            public int getBatchSize() {
                return listItems.size();
            }
        });
		return listItems;
	}
	
	public void batchDelete(List<ListItem> listItems) {
		final String sql = "delete from listitem where id = ?;";
		this.jdbcTemplate.batchUpdate(
	        sql, new BatchPreparedStatementSetter() {		
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	                ps.setInt(1, listItems.get(i).getId());
	            }
	
	            public int getBatchSize() {
	                return listItems.size();
	            }
	        });
	}
	
	public List<ListItem> batchInsert(List<ListItem> listItems) {
		final String sql = "insert into listitem (name, priority, checked, quantity, owner_id, list_id) values (?, ?, ?, ?, ?, ?);";

		this.jdbcTemplate.batchUpdate(
			sql, new BatchPreparedStatementSetter() {
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	                ps.setString(1, listItems.get(i).getName());
	                ps.setString(2, listItems.get(i).getPriority());
	                ps.setBoolean(3, false);
	                ps.setInt(4, listItems.get(i).getQuantity());
	                ps.setString(5, listItems.get(i).getOwner().getId());
	                ps.setInt(6, listItems.get(i).getList().getId());
	            }
	
	            public int getBatchSize() {
	                return listItems.size();
	            }
	        });
		return listItems;
	}
	 
	public ListItem update(ListItem listItem) {
        final String sql = "update listitem set name = ?, priority = ?, checked  = ?, quantity = ? where id = ?;";
        jdbcTemplate.update(
        		sql, listItem.getName(), listItem.getPriority(), listItem.isChecked(), listItem.getQuantity(), listItem.getId());			
        return listItem;
	}
	
	public ListItem save(ListItem listItem, int listId) {
		final String sql = "insert into listitem (name, priority, checked, quantity, owner_id, list_id) values (?, ?, ?, ?, ?, ?);";
	    KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, listItem.getName());
            ps.setString(2, listItem.getPriority());
            ps.setBoolean(3, listItem.isChecked());
            ps.setInt(4, listItem.getQuantity());
            ps.setString(5, listItem.getOwner().getId());
            ps.setInt(6, listId);
           
            return ps;
        }, keyHolder);
		int id = keyHolder.getKey().intValue();
		
		listItem.setId(id);
	
		return listItem;
	}
}
