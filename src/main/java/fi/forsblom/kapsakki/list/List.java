package fi.forsblom.kapsakki.list;

import java.util.ArrayList;

import fi.forsblom.kapsakki.listItem.ListItem;
import fi.forsblom.kapsakki.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class List {
	private int id;
	private User owner;
	private String name;
	private String description;
	private ArrayList<ListItem> listItems = new ArrayList<ListItem>();
	
	public void addListItem(ListItem listItem) {
		this.listItems.add(listItem);
	}
	
}
