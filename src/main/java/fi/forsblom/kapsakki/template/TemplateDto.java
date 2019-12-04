package fi.forsblom.kapsakki.template;

import java.util.ArrayList;

import fi.forsblom.kapsakki.templateListItem.TemplateListItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class TemplateDto {
	private int id;
	private String name;
	private String description;
	private ArrayList<TemplateListItemDto> listItems = new ArrayList<TemplateListItemDto>();
}
