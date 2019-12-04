package fi.forsblom.kapsakki.listItem;

import fi.forsblom.kapsakki.list.List;
import fi.forsblom.kapsakki.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class ListItem {
	private int id;
	private List list;
	private User owner;
	private boolean checked;
	private int quantity;
	private String name;
	private String priority;
	
}
