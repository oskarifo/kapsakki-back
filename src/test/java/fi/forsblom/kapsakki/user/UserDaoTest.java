package fi.forsblom.kapsakki.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(UserDao.class)
public class UserDaoTest {
	@Autowired
	UserDao userDao;
	
	@Test
	public void testFindById() {
		User user = userDao.findById("testUser");

		assertThat(user.getId())
		.isEqualTo("testUser");
	}

	@Test
	public void testDeleteById() {
		int rows = userDao.deleteById("testUser");
		
		assertThat(rows)
		.isEqualTo(1);
	}
	
	@Test
	public void testCreateUser() {
		User user = new User();
    	user.setId("testUser1");
    	user.setEmail("test@email.com");
    	user.setFirstName("testFirstName");
    	user.setLastName("testLastName");
    	user.setProfilePicture("testUrl");
		
    	User createdUser = userDao.save(user);
		
		assertThat(createdUser.getId())
		.isEqualTo("testUser1");
	}
}
