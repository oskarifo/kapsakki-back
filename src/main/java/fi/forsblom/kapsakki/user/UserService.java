package fi.forsblom.kapsakki.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import fi.forsblom.kapsakki.exceptions.AlreadyRegisteredException;
import fi.forsblom.kapsakki.exceptions.DatabaseException;
import fi.forsblom.kapsakki.exceptions.UserNotFoundException;
import fi.forsblom.kapsakki.list.ListController;

@Service
public class UserService implements IUserService {

	@Autowired
	UserDao userDao;

	private static Logger LOGGER = LoggerFactory.getLogger(ListController.class);

	@Override
	public int deleteUserById(String userId) {
		try {
			LOGGER.debug("event={} user={}", "delete-user", userId);
			return userDao.deleteById(userId);
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={}", "delete-user-failed", userId);
			throw new DatabaseException("Database error", ex);
		}
	}

	@Override
	public User createUser(User newUser) throws AlreadyRegisteredException {
		try {
			LOGGER.debug("event={} user={}", "new-user-creation", newUser.getId());
			return userDao.save(newUser);
		} catch (DuplicateKeyException ex) {
			LOGGER.debug("event={} user={}", "new-user-creation-failed-dublicate", newUser.getId());
			throw new AlreadyRegisteredException("User already registered");
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={}", "new-user-creation-failed", newUser.getId());
			throw new DatabaseException("Database error", ex);
		}
	}

	@Override
	public User getUserById(String userId) throws UserNotFoundException {
		try {
			LOGGER.debug("event={} user={}", "get-user", userId);
			return userDao.findById(userId);
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.info("event={} user={}", "get-user-not-found", userId);
			throw new UserNotFoundException("User not found");
		} catch (DataAccessException ex) {
			LOGGER.info("event={} user={}", "get-user-not-found", userId);
			throw new DatabaseException("Database error", ex);
		}
	}
	
}
