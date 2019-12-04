package fi.forsblom.kapsakki.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class TemplateSimpleDto {
	private int id;
	private String name;
	private String description;
}
