package fi.forsblom.kapsakki.listItem;

import java.util.List;

public interface IListItemService {
	public ListItem addListItem(int listId, ListItem listItem, String ownerId);
	public List<ListItem> addListItems(int listId, List<ListItem> listItems, String ownerId);
	public void deleteListItems(int listId, List<ListItem> listItems, String ownerId);
	public ListItem editListItem(ListItem listItem, String ownerId);
	public List<ListItem> patchListItems(int listId, List<ListItem> listItems, String ownerId);
}
