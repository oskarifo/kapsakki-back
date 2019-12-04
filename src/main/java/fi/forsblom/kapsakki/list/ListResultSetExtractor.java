package fi.forsblom.kapsakki.list;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import fi.forsblom.kapsakki.listItem.ListItem;
import fi.forsblom.kapsakki.user.User;

public class ListResultSetExtractor implements ResultSetExtractor<List> {

    public List extractData(ResultSet rs) throws SQLException, DataAccessException {
		List list = new List();
		
	    while (rs.next()) {
	        
	    	int id = rs.getInt("LIST.ID");
	        if (list.getId() != id) {
	            list.setId(rs.getInt("LIST.ID"));
	            list.setName(rs.getString("LIST.NAME"));
	            list.setDescription(rs.getString("LIST.DESCRIPTION"));
	            
	            User user = new User();
	    		
	            user.setId(rs.getString("USER.ID"));
	    		user.setFirstName(rs.getString("USER.FIRST_NAME"));
	    		user.setLastName(rs.getString("USER.LAST_NAME"));
	    		user.setEmail(rs.getString("USER.EMAIL"));
	    		user.setProfilePicture(rs.getString("USER.PICTURE_URL"));
	    		
	    		list.setOwner(user);
	        }
	        
	        if (rs.getString("LISTITEM.ID") != null) {
	        	ListItem listItem = new ListItem();
	            listItem.setId(rs.getInt("LISTITEM.ID"));
	            listItem.setName(rs.getString("LISTITEM.NAME"));
	            listItem.setQuantity(rs.getInt("LISTITEM.QUANTITY"));
	            listItem.setChecked(rs.getBoolean("LISTITEM.CHECKED"));
	            listItem.setPriority(rs.getString("LISTITEM.PRIORITY"));
	            
	            User user = new User();
	    		
	            user.setId(rs.getString("USER.ID"));
	    		user.setFirstName(rs.getString("USER.FIRST_NAME"));
	    		user.setLastName(rs.getString("USER.LAST_NAME"));
	    		user.setEmail(rs.getString("USER.EMAIL"));
	    		user.setProfilePicture(rs.getString("USER.PICTURE_URL"));
	            
	            listItem.setOwner(user);
	            
	            list.addListItem(listItem);
	        }
	    }
	    /*
	     * Spagettia, tämän voisi korjata
	     * palauta lista jos löytyy omistaja, 
	     * muuten palauta null
	     */
	    
	    if (list.getOwner() != null)
	    	return list;
	    else 
	    	return null;
    }
}