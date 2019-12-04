package fi.forsblom.kapsakki.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fi.forsblom.kapsakki.exceptions.DatabaseException;
import fi.forsblom.kapsakki.user.User;

@ExtendWith(SpringExtension.class)
public class ListServiceTest {
	
	@InjectMocks
	ListService listService;
	
	@Mock
	ListDao listDaoMock;
	
	@DisplayName("Test Get All Lists Database Failure")
	@Test
	public void testGetAllListsException() {
		when(listDaoMock.findAllByOwnerId("testUser")).thenThrow(new DataRetrievalFailureException(anyString()));
		assertThrows(DatabaseException.class, () -> { 
			listService.getLists("testUser");
		});
	}
	
	@DisplayName("Test Get All Lists")
	@Test
	public void testGetAllLists() {
		List list = new List();
    	list.setId(1);
    	list.setName("Nimi");
    	list.setDescription("Kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	    	
    	list.addListItem(null);
    	list.setOwner(user);
    	
    	ArrayList<List> array = new ArrayList<List>();
    	array.add(list);
    	
		when(listDaoMock.findAllByOwnerId("testUser")).thenReturn(array);
		
		java.util.List<List> lists = listService.getLists("testUser");

		assertThat(lists.size())
			.isEqualTo(1);
		assertThat(lists.get(0).getId())
	     	.isEqualTo(1);
		assertThat(lists.get(0).getDescription())
			.isEqualTo("Kuvaus");
		assertThat(lists.get(0).getName())
			.isEqualTo("Nimi");
		
		verify(listDaoMock, times(1)).findAllByOwnerId("testUser");
        verifyNoMoreInteractions(listDaoMock);
	}
	
	@DisplayName("Test Get List")
	@Test
	public void testGetList() {
		List list = new List();
    	list.setId(1);
    	list.setName("Nimi");
    	list.setDescription("Kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	    	
    	list.addListItem(null);
    	list.setOwner(user);
    	
    	when(listDaoMock.findByIdAndOwnerId(anyInt(), anyString())).thenReturn(Optional.of(list));
    	
    	List fetchedList = listService.getList(1, "testUser");

    	assertThat(fetchedList.getId())
     	.isEqualTo(1);
    	assertThat(fetchedList.getDescription())
		.isEqualTo("Kuvaus");
    	assertThat(fetchedList.getName())
		.isEqualTo("Nimi");
	
    	verify(listDaoMock, times(1)).findByIdAndOwnerId(anyInt(), anyString());
    	verifyNoMoreInteractions(listDaoMock);
    	
	}
	
}
