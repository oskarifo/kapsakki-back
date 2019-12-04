package fi.forsblom.kapsakki.template;

import java.util.ArrayList;

import fi.forsblom.kapsakki.templateListItem.TemplateListItem;
import fi.forsblom.kapsakki.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class Template {
	private int id;
	private String name;
	private User owner;
	private ArrayList<TemplateListItem> listItems = new ArrayList<TemplateListItem>();
	
	public void addListItem(TemplateListItem listItem) {
		this.listItems.add(listItem);
	}
}
