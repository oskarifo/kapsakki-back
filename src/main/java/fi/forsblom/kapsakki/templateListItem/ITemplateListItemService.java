package fi.forsblom.kapsakki.templateListItem;

import java.util.List;

public interface ITemplateListItemService {
	public List<TemplateListItem> addTemplateListItems(int templateId, List<TemplateListItem> templateListItems, String ownerId);
}
