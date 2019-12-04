package fi.forsblom.kapsakki.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fi.forsblom.kapsakki.exceptions.AlreadyRegisteredException;
import fi.forsblom.kapsakki.exceptions.UserNotFoundException;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	UserService userService;
	
	@Mock
	UserDao userDaoMock;
	
	@Test
	public void testDeleteUserById() {
		when(userDaoMock.deleteById("testUser")).thenReturn(1);
		int removedUsers = userService.deleteUserById("testUser");
		
		assertThat(removedUsers)
		.isEqualTo(1);
		
    	verify(userDaoMock, times(1)).deleteById("testUser");
    	verifyNoMoreInteractions(userDaoMock);
	}
	
	@Test
	public void testGetUserById() {
		User user = new User();
    	user.setId("testUser");
    	user.setEmail("test@email.com");
    	user.setFirstName("testFirstName");
    	user.setLastName("testLastName");
    	user.setProfilePicture("testUrl");
    	
		when(userDaoMock.findById("testUser")).thenReturn(user);
		
		User fetchedUser = userService.getUserById("testUser");

		assertThat(fetchedUser.getId())
	     	.isEqualTo("testUser");
		
		verify(userDaoMock, times(1)).findById("testUser");
        verifyNoMoreInteractions(userDaoMock);
	}
	
	@Test
	public void testCreateUser() throws AlreadyRegisteredException {
		User user = new User();
    	user.setId("testUser");
    	user.setEmail("test@email.com");
    	user.setFirstName("testFirstName");
    	user.setLastName("testLastName");
    	user.setProfilePicture("testUrl");
    	
		when(userDaoMock.save(any(User.class))).thenReturn(user);
		
		User createdUser = userService.createUser(user);
		
		assertThat(createdUser.getId())
     	.isEqualTo("testUser");
	
		verify(userDaoMock, times(1)).save(any(User.class));
		verifyNoMoreInteractions(userDaoMock);
	}
	
	@Test
	public void testUserNotFoundException() throws UserNotFoundException {
		when(userDaoMock.findById(anyString())).thenThrow(new EmptyResultDataAccessException("", 0));
		assertThrows(UserNotFoundException.class, () -> { 
			userService.getUserById(anyString());
		});
	}
	
	@Test
	public void testAlreadyRegisteredException() throws AlreadyRegisteredException {
		User user = new User();
    	user.setId("testUser");
    	user.setEmail("test@email.com");
    	user.setFirstName("testFirstName");
    	user.setLastName("testLastName");
    	user.setProfilePicture("testUrl");
    	
		when(userDaoMock.save(any(User.class))).thenThrow(new DuplicateKeyException(anyString()));
		assertThrows(AlreadyRegisteredException.class, () -> { 
			userService.createUser(user);
		});
	}
}
