package fi.forsblom.kapsakki.list;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@JdbcTest
@Import(ListDao.class)
public class ListDaoTest {
	
	@Autowired
	ListDao listDao;
	
	@Test
	@Sql("init.sql")
	public void findByIdAndOwnerIdTest() {
		java.util.List<List> lists = listDao.findAllByOwnerId("testUser");
		
		assertThat(lists.size())
		.isEqualTo(3);
		
		assertThat(lists.get(0).getId())
		.isEqualTo(1);
		
		assertThat(lists.get(0).getName())
		.isEqualTo("Testilista 1");
	}
	
}
