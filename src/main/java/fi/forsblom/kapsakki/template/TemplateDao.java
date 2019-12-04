package fi.forsblom.kapsakki.template;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Optional<Template> findByIdAndOwnerId(int templateId, String ownerId) {
		final String sql = "select template.id, template.name, template.owner_id, " +  
				"template_listitem.id, template_listitem.name, template_listitem.quantity, " +
				"template_listitem.priority, template_listitem.owner_id, " + 
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from template " +
				"left join template_listitem on template.id = template_listitem.template_id " +
				"inner join user on user.id = template.owner_id " +
			 	"where template.id = ? and template.owner_id = ?;";
		Template template = jdbcTemplate.query(
			sql, new Object[]{ templateId, ownerId }, new TemplateResultSetExtractor());
		
		return Optional.ofNullable(template);

	}
	
	public Optional<Template> findById(int templateId) {
		final String sql = "select template.id, template.name, template.owner_id, " +  
				"template_listitem.id, template_listitem.name, template_listitem.quantity, " +
				"template_listitem.priority, template_listitem.owner_id, " + 
			    "user.id, user.first_name, user.last_name, user.email, user.picture_url " +
				"from template " +
				"left join template_listitem on template.id = template_listitem.template_id " +
				"inner join user on user.id = template.owner_id " +
			 	"where template.id = ?;";
		Template template = jdbcTemplate.query(
			sql, new Object[]{ templateId }, new TemplateResultSetExtractor());
		
		return Optional.ofNullable(template);
	}

	public List<Template> findAllByOwnerId(String ownerId) {
		final String sql = "select template.id, template.name " +
				"from template " +
				"where template.owner_id = ?;";
		return jdbcTemplate.query(
				sql, new Object[]{ownerId}, new TemplatesRowMapper());	

	}

	public Template save(Template template) {
		final String sql = "insert into template (name, owner_id) values (?, ?);";
	    KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, template.getName());
            ps.setString(2, template.getOwner().getId());
           
            return ps;
        }, keyHolder);
		int id = keyHolder.getKey().intValue();
		template.setId(id);
		return template;
	}

	public Template updateById(Template template) {
		final String sql = "update template set name = ?, where id = ?;";
        jdbcTemplate.update(
        		sql, template.getName(), template.getId());			
        return template;

	}

	public int deleteById(int templateId) {
		final String sql = "delete from template where id = ?;";
		return jdbcTemplate.update(sql, templateId);
	}

}
