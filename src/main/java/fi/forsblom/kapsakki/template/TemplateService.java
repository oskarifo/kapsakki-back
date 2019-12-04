package fi.forsblom.kapsakki.template;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.templateListItem.ITemplateListItemService;
import fi.forsblom.kapsakki.user.User;
import fi.forsblom.kapsakki.user.UserService;

@Service
public class TemplateService implements ITemplateService {

	@Autowired
	TemplateDao templateDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ITemplateListItemService templateListItemService;
	
	@Override
	@Transactional
	public Template createTemplate(Template template, String ownerId) {
		User user = userService.getUserById(ownerId);
		template.setOwner(user);

		Template createdTemplate = this.templateDao.save(template);
		template.setId(createdTemplate.getId());
		this.templateListItemService.addTemplateListItems(template.getId(), template.getListItems(), ownerId);
		
		return this.templateDao.findByIdAndOwnerId(template.getId(), ownerId).get();
	}

	@Override
	public List<Template> getTemplates(String ownerId) {
		return this.templateDao.findAllByOwnerId(ownerId);
	}

	@Override
	public Template getTemplate(int templateId, String ownerId) {
		Optional<Template> template = this.templateDao.findByIdAndOwnerId(templateId, ownerId);
		
		if(template.isPresent()) {
			return template.get();
		} else { 
			throw new NotFoundException("Template not found");
		}
	}

	@Override
	public int deleteTemplate(int templateId, String ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
