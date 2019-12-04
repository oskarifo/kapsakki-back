package fi.forsblom.kapsakki.templateListItem;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.forsblom.kapsakki.exceptions.ForbiddenException;
import fi.forsblom.kapsakki.list.ListService;
import fi.forsblom.kapsakki.template.Template;
import fi.forsblom.kapsakki.template.TemplateDao;

@Service
public class TemplateListItemService implements ITemplateListItemService {

	@Autowired
	TemplateListItemDao	templateListItemDao;
	
	@Autowired
	TemplateDao templateDao;
	
    private static Logger LOGGER = LoggerFactory.getLogger(ListService.class);
	
	@Override
	public List<TemplateListItem> addTemplateListItems(int templateId, List<TemplateListItem> templateListItems, String ownerId) {
		Optional<Template> origTemplate = templateDao.findById(templateId);
		
		if (!origTemplate.isPresent()) {
			throw new ForbiddenException("Forbidden to add LTemplateListItem");
		} else if (!origTemplate.get().getOwner().getId().equals(ownerId)){
			throw new ForbiddenException("Forbidden to add TemplateListItem");
		} 
		
		for (TemplateListItem templateListItem:templateListItems) {
			LOGGER.debug("{}", templateListItem);
			templateListItem.setTemplate(origTemplate.get());
			templateListItem.setOwner(origTemplate.get().getOwner());
		}
		return this.templateListItemDao.batchInsert(templateListItems);
	}

}
