package fi.forsblom.kapsakki.user;

import fi.forsblom.kapsakki.exceptions.AlreadyRegisteredException;
import fi.forsblom.kapsakki.exceptions.UserNotFoundException;

public interface IUserService {
	int deleteUserById(String id);
	User createUser(User user) throws AlreadyRegisteredException;
	User getUserById(String id) throws UserNotFoundException;
}
