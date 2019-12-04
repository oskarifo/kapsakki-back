package fi.forsblom.kapsakki.template;

import java.util.List; 

public interface ITemplateService {
	public Template createTemplate(Template template, String ownerId);
	public List<Template> getTemplates(String ownerId);
	public Template getTemplate(int templateId, String ownerId);
	public int deleteTemplate(int templateId, String ownerId);	
}
