package fi.forsblom.kapsakki.listItem;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.forsblom.kapsakki.exceptions.ForbiddenException;
import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.list.List;
import fi.forsblom.kapsakki.list.ListDao;
import fi.forsblom.kapsakki.list.ListService;
import fi.forsblom.kapsakki.user.IUserService;

@Service
public class ListItemService implements IListItemService {

	@Autowired
	ListItemDao listItemDao;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	ListDao listDao;
	
    private static Logger LOGGER = LoggerFactory.getLogger(ListService.class);
	
	@Override
	public ListItem addListItem(int listId, ListItem listItem, String ownerId) {
		Optional<List> list = listDao.findByIdAndOwnerId(listId, ownerId);
		
		if (!list.isPresent()) {
			throw new NotFoundException("List not found");
		} else if (list.get().getOwner().getId().equals(ownerId)) {
			listItem.setOwner(list.get().getOwner());
		} else {
			throw new ForbiddenException("Unauthorized");	
		}		
		
		return listItemDao.save(listItem, listId);
	}

	@Override
	public ListItem editListItem(ListItem listItem, String ownerId) {
		LOGGER.debug("{}", listItem);
		ListItem origListItem = listItemDao.findById(listItem.getId());
		
		if (!ownerId.equals(origListItem.getOwner().getId())) {
			throw new ForbiddenException("Forbidden to edit ListItem");
		}
		
		listItem.setOwner(origListItem.getOwner());
		listItem.setList(origListItem.getList());
		
		LOGGER.debug("{}", listItem);
		
		return listItemDao.update(listItem);
	}

	@Override
	public java.util.List<ListItem> addListItems(int listId, java.util.List<ListItem> listItems, String ownerId) {
		Optional<List> origList = listDao.findById(listId);
		if (!origList.isPresent()) {
			LOGGER.info("event={} user={} error={}", "add-list-items", ownerId, "parent-list-not-found");
			throw new ForbiddenException("Forbidden to add ListItem");
		} else if (!origList.get().getOwner().getId().equals(ownerId)){
			LOGGER.info("event={} user={} error={}", "add-list-items", ownerId, "user-does-not-own-parent-list");
			throw new ForbiddenException("Forbidden to add ListItem");
		} 
		
		for (ListItem listItem:listItems) {
			listItem.setList(origList.get());
			listItem.setOwner(origList.get().getOwner());
		}

		return this.listItemDao.batchInsert(listItems);
	}

	@Override
	public void deleteListItems(int listId, java.util.List<ListItem> listItems, String ownerId) {
		Optional<List> origList = listDao.findById(listId);
		
		if (!origList.isPresent()) {
			throw new ForbiddenException("Forbidden to delete ListItems");
		}
		for (ListItem listItem:listItems) {
			if (!this.containsInArray(origList.get().getListItems(), listItem)) {
				throw new ForbiddenException("Forbidden to patch ListItem");
			}
		}
		listItemDao.batchDelete(listItems);
	}

	@Override
	public java.util.List<ListItem> patchListItems(int listId, java.util.List<ListItem> listItems, String ownerId) {
		Optional<List> origList = listDao.findById(listId);
		
		if (!origList.isPresent()) {
			throw new ForbiddenException("Forbidden to patch ListItem");
		}
			
		for (ListItem listItem:listItems) {
			
			if (!this.containsInArray(origList.get().getListItems(), listItem)) {
				throw new ForbiddenException("Forbidden to patch ListItem");
			}
			
			listItem.setOwner(origList.get().getOwner());
			listItem.setList(origList.get());
		}
		return listItemDao.batchUpdate(listItems);
	}
	
	public boolean containsInArray (java.util.List<ListItem> listItems, ListItem listItem) {
		Optional<ListItem> item = listItems.stream().filter(_listItem -> {
			if (_listItem.getId() == listItem.getId()) {
				return true;
			};
			return false;
		}).findAny();
	
		return item.isPresent();
	}
}
