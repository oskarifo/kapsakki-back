package fi.forsblom.kapsakki.templateListItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class TemplateListItemDto {
	private int id;
	private int quantity;
	private String name;
	private String priority;
}
