package fi.forsblom.kapsakki.template;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import fi.forsblom.kapsakki.templateListItem.TemplateListItem;
import fi.forsblom.kapsakki.user.User;

public class TemplateResultSetExtractor implements ResultSetExtractor<Template>  {

	@Override
	public Template extractData(ResultSet rs) throws SQLException, DataAccessException {
		Template template = new Template();
	    while (rs.next()) {
	    	int id = rs.getInt("template.id");
	        if (template.getId() != id) {
	            template.setId(rs.getInt("template.id"));
	            template.setName(rs.getString("template.name"));
	            
	            User user = new User();
	            user.setId(rs.getString("user.id"));
	    		user.setFirstName(rs.getString("user.first_name"));
	    		user.setLastName(rs.getString("user.last_name"));
	    		user.setEmail(rs.getString("user.email"));
	    		user.setProfilePicture(rs.getString("user.picture_url"));
	    		
	    		template.setOwner(user);
	        }
	        
	        if (rs.getString("template_listitem.id") != null) {
	        	TemplateListItem listItem = new TemplateListItem();
	            listItem.setId(rs.getInt("template_listitem.id"));
	            listItem.setName(rs.getString("template_listitem.name"));
	            listItem.setQuantity(rs.getInt("template_listitem.quantity"));
	            listItem.setPriority(rs.getString("template_listitem.priority"));
	            
	            User user = new User();
	            user.setId(rs.getString("user.id"));
	    		user.setFirstName(rs.getString("user.first_name"));
	    		user.setLastName(rs.getString("user.last_name"));
	    		user.setEmail(rs.getString("user.email"));
	    		user.setProfilePicture(rs.getString("user.picture_url"));
	            
	            listItem.setOwner(user);
	            
	            template.addListItem(listItem);
	        }
	    }
	    /*
	     * Spagettia, tämän voisi korjata
	     * palauta lista jos löytyy omistaja, 
	     * muuten palauta null
	     */
	    
	    if (template.getOwner() != null)
	    	return template;
	    else 
	    	return null;
	}
}
