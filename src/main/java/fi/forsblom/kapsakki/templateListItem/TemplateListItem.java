package fi.forsblom.kapsakki.templateListItem;

import fi.forsblom.kapsakki.template.Template;
import fi.forsblom.kapsakki.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class TemplateListItem {
	private int id;
	private Template template;
	private User owner;
	private int quantity;
	private String name;
	private String priority;
	
}
