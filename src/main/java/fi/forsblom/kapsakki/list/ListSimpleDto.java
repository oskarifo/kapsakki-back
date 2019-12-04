package fi.forsblom.kapsakki.list;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class ListSimpleDto {
	private int id;
	private String name;
	private String description;
}
