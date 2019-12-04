package fi.forsblom.kapsakki.listItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class ListItemDto {
	private int id;
	private boolean checked;
	private int quantity;
	private String name;
	private String priority;
}
