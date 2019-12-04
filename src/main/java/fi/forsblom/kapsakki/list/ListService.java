package fi.forsblom.kapsakki.list;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.forsblom.kapsakki.exceptions.DatabaseException;
import fi.forsblom.kapsakki.exceptions.ForbiddenException;
import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.listItem.IListItemService;
import fi.forsblom.kapsakki.user.IUserService;
import fi.forsblom.kapsakki.user.User;

@Service
public class ListService implements IListService {

	@Autowired
	ListDao listDao;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IListItemService listItemService;
	
    private static Logger LOGGER = LoggerFactory.getLogger(ListService.class);
	
	@Override
	@Transactional
	public List createList(List list, String ownerId) throws DatabaseException {
		User user = userService.getUserById(ownerId);
		list.setOwner(user);
		List createdList;
		try {
			createdList = listDao.save(list);
			LOGGER.debug("event={} user={}", "new-list-creation", user.getId());

		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} e={}", "new-list-creation-failure", user.getId(), ex.getLocalizedMessage());
			throw new DatabaseException("Database failure", ex);
		}
		this.listItemService.addListItems(createdList.getId(), list.getListItems(), ownerId);
		
		return this.listDao.findByIdAndOwnerId(list.getId(), ownerId).get();
	}

	@Override
	public java.util.List<List> getLists(String ownerId) throws DatabaseException {
		try {
			java.util.List<List> lists = listDao.findAllByOwnerId(ownerId);
			LOGGER.info("event={} user={}", "get-user-lists", ownerId);
			return lists;
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} e={}", "get-user-lists-failure", ownerId, ex.getLocalizedMessage());
			throw new DatabaseException("Database failure", ex);
		}
	}

	@Override
	public List getList(int listId, String ownerId) {
		try {
			LOGGER.debug("event={} user={} list={}", "get-list", ownerId, listId);
			Optional<List> list = listDao.findByIdAndOwnerId(listId, ownerId);
			if (list.isPresent()) {
				return list.get();
			} else {
				throw new NotFoundException("List not found");
			}
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} list={}", "get-list-failed", ownerId, listId);
			throw new DatabaseException("Database failure", ex);
		}
	}
	
	@Override
	@Transactional
	public List editList(List list, String ownerId) {
		/*
		 * ToDo voisi muuttaa findById:n käyttämään
		 * findByIdAndOwner metodia
		 */
		try {
			LOGGER.debug("event={} user={} list={}", "edit-list-get", ownerId, list.getId());
			Optional<List> origList = listDao.findById(list.getId());
			if (!origList.isPresent()) {
				throw new NotFoundException("List not found");
			}
			else if (!origList.get().getOwner().getId().equals(ownerId)) {
				LOGGER.info("event={} user={} list={}", "edit-list-get-forbidden", ownerId, list.getId());
				throw new ForbiddenException("Forbidden to edit List");
			}
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} list={}", "edit-list-get-failed", ownerId, list.getId());
			throw new DatabaseException("Database failure", ex);
		}
		listItemService.deleteListItems(list.getId(), list.getListItems(), ownerId);
		try {
			LOGGER.debug("event={} user={} list={}", "edit-list-update", ownerId, list.getId());
			listDao.updateById(list);
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} list={}", "edit-list-update-failed", ownerId, list.getId());
		}
		return this.getList(list.getId(), ownerId);
	}

	@Override
	public int deleteList(int listId, String ownerId) {
		List origList = this.getList(listId, ownerId);
		
		if (!ownerId.equals(origList.getOwner().getId())) {
			LOGGER.info("event={} user={} list={}", "delete-list-forbidden", ownerId, listId);
		}

		try {
			LOGGER.debug("event={} user={} list={}", "delete-list", ownerId, listId);
			return listDao.deleteById(listId);
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={} list={}", "delete-list-failed", ownerId, listId);
			throw new DatabaseException("Database failyre", ex);
		}
	}
}
