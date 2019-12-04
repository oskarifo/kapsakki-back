package fi.forsblom.kapsakki.listItem;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fi.forsblom.kapsakki.user.User;

public class ListItemRowMapper implements RowMapper<ListItem> {
	@Override
	public ListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		ListItem listItem = new ListItem();
		 
		listItem.setId(rs.getInt("LISTITEM.ID"));
		listItem.setName(rs.getString("LISTITEM.NAME"));
		listItem.setChecked(rs.getBoolean("LISTITEM.CHECKED"));
		listItem.setPriority(rs.getString("LISTITEM.PRIORITY"));
		listItem.setQuantity(rs.getInt("LISTITEM.QUANTITY"));
		
		User user = new User();
		user.setId(rs.getString("USER.ID"));
		user.setFirstName(rs.getString("USER.FIRST_NAME"));
		user.setLastName(rs.getString("USER.LAST_NAME"));
		user.setEmail(rs.getString("USER.EMAIL"));
		user.setProfilePicture(rs.getString("USER.PICTURE_URL"));
		
		listItem.setOwner(user);
		
        return listItem;
	}
}
