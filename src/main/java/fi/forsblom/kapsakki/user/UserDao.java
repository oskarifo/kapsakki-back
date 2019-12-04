package fi.forsblom.kapsakki.user;

import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public User findById(String id) {
		final String sql = "select id, first_name, last_name, email, picture_url from user where id = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{id}, new UserRowMapper());
	}
	
	public User save(User user) {
		final String sql = "insert into user (id, first_name, last_name, email, picture_url) values (?, ?, ?, ?, ?)";

		jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getProfilePicture());
            
            return ps;
        });
		return user;
	}

	public int deleteById(String userId) {
		final String sql = "delete from user where id = ?";
		
		return jdbcTemplate.update(sql, userId);
	}
}
