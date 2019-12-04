package fi.forsblom.kapsakki.list;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fi.forsblom.kapsakki.persistance.AbstractDao;

@Repository
public class ListDao implements AbstractDao<List> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    private static Logger LOGGER = LoggerFactory.getLogger(ListService.class);

	@Override
	public Optional<List> findByIdAndOwnerId(int listId, String ownerId) throws DataAccessException {
		LOGGER.debug("event={} listId={} ownerId={}", "find-by-id-and-owner-id", listId, ownerId);
		
		final String sql =  "select list.id, list.name, list.description, list.owner_id, listitem.id, listitem.name, listitem.quantity, listitem.checked, listitem.priority ,listitem.owner_id, " + 
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from list " +
				"left join listitem on list.id = listitem.list_id " +
				"inner join user on user.id = list.owner_id " +
			 	"where list.id = ? and list.owner_id = ?";
		
		List list = jdbcTemplate.query(
			sql, new Object[]{listId, ownerId}, new ListResultSetExtractor());
		
		return Optional.ofNullable(list);
	}
	
	public Optional<List> findById(int listId) throws DataAccessException {
		LOGGER.debug("event={} listId={}", "find-by-id", listId);
		
		final String sql =  "select list.id, list.name, list.description, list.owner_id, listitem.id, listitem.name, listitem.quantity, listitem.checked, listitem.priority ,listitem.owner_id, " + 
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from list " +
				"left join listitem on list.id = listitem.list_id " +
				"inner join user on user.id = list.owner_id " +
			 	"where list.id = ?";
		List list = jdbcTemplate.query(
			sql, new Object[]{listId}, new ListResultSetExtractor());
		
		return Optional.ofNullable(list);
	}

	@Override
	public java.util.List<List> findAllByOwnerId(String ownerId) throws DataAccessException {
		LOGGER.debug("event={} ownerId={}", "find-all-by-owner-id", ownerId);

		final String sql = "select list.id, list.name, list.description " +
				"from list " +
				"where list.owner_id = ?";
		return jdbcTemplate.query(
				sql, new Object[]{ownerId}, new ListsRowMapper());	
	}

	@Override
	public List save(List list) throws DataAccessException {
		LOGGER.debug("event={} listId={}", "save-list", list.getId());

		final String sql = "insert into list (name, description, owner_id) values (?, ?, ?);";
	    KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, list.getName());
            ps.setString(2, list.getDescription());
            ps.setString(3, list.getOwner().getId());
           
            return ps;
        }, keyHolder);
		int id = keyHolder.getKey().intValue();
		list.setId(id);
		return list;
	}

	@Override
	public List updateById(List list) throws DataAccessException {
		LOGGER.debug("event={} listId={}", "update-list-by-id", list.getId());

		final String sql = "update list set name = ?, description = ? where id = ?";
        jdbcTemplate.update(
        		sql, list.getName(), list.getDescription(), list.getId());			
        return list;	
	}

	@Override
	public int deleteById(int listId) throws DataAccessException {
		LOGGER.debug("event={} listId={} ownerId={}", "delete-list-by-id", listId);

		final String sql = "delete from list where id = ?";
		return jdbcTemplate.update(sql, listId);
	}	
}
