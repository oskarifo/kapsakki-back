package fi.forsblom.kapsakki.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    	User user = new User();
 
    	user.setId(rs.getString("ID"));
    	user.setFirstName(rs.getString("FIRST_NAME"));
    	user.setLastName(rs.getString("LAST_NAME"));
    	user.setEmail(rs.getString("EMAIL"));
    	user.setProfilePicture(rs.getString("PICTURE_URL"));

        return user;
    }
}