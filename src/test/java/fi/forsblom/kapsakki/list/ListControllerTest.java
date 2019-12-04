package fi.forsblom.kapsakki.list;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.listItem.ListItem;
import fi.forsblom.kapsakki.listItem.ListItemDto;
import fi.forsblom.kapsakki.listItem.ListItemService;
import fi.forsblom.kapsakki.user.User;

@WebMvcTest(controllers = ListController.class)
public class ListControllerTest {	
	@MockBean
	ListItemService listItemServiceMock;
	
    @MockBean
    ListService listServiceMock;
    
    @Autowired
    private MockMvc mockMvc;
    
	ObjectMapper mapper = new ObjectMapper();
     
    @DisplayName("Test Unauthorized Request")
    @Test
    public void testUnauthorizedRequest() throws Exception {
    	this.mockMvc.perform(get("/api/lists"))
    		.andExpect(status().isUnauthorized());
    }

    @DisplayName("Test Get Empty List")
    @Test
    @WithMockUser(username = "testUser")
    public void testGetEmptyList() throws Exception {
    	when(listServiceMock.getLists("testUser")).thenReturn(new ArrayList<List>());

    	this.mockMvc.perform(get("/api/lists"))
    		.andExpect(status().isOk());
    	
    	verify(listServiceMock, times(1)).getLists("testUser");
        verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test List Not Found")
    @Test
    @WithMockUser(username = "testUser")
    public void testListNotFound() throws Exception {
        when(listServiceMock.getList(1, "testUser")).thenThrow(new NotFoundException("List not found"));
 
        mockMvc.perform(get("/api/lists/{id}", 1))
                .andExpect(status().isNotFound());
 
        verify(listServiceMock, times(1)).getList(1,"testUser");
        verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test Create List")
    @Test
    @WithMockUser(username = "testUser")
    public void testCreateList() throws Exception {
    	ListDto listDto = new ListDto();
    	listDto.setId(1);
    	listDto.setName("Nimi");
    	listDto.setDescription("Kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	
    	ArrayList<ListItemDto> listItemsDto = new ArrayList<ListItemDto>();
    	
    	ListItemDto listItemDto1 = new ListItemDto();
    	listItemDto1.setId(1);
    	listItemDto1.setName("Testi");
    	listItemDto1.setPriority("Tärkeä");
    	listItemDto1.setQuantity(5);
    	
    	ListItemDto listItemDto2 = new ListItemDto();
    	listItemDto2.setId(2);
    	listItemDto2.setName("Testi");
    	listItemDto2.setPriority("Tärkeä");
    	listItemDto2.setQuantity(5);
    	
    	listItemsDto.add(listItemDto1);
    	listItemsDto.add(listItemDto2);
    	
    	List createdList = new List();
    	createdList.setId(1);
    	createdList.setDescription("Kuvaus");
    	createdList.setName("Nimi");
    	createdList.setOwner(user);
    	
    	ListItem listItem1 = new ListItem();
    	listItem1.setId(1);
    	listItem1.setName("Testi");
    	listItem1.setPriority("Tärkeä");
    	listItem1.setQuantity(5);
    	
    	ListItem listItem2 = new ListItem();
    	listItem2.setId(2);
    	listItem2.setName("Testi");
    	listItem2.setPriority("Tärkeä");
    	listItem2.setQuantity(5);
    	
    	createdList.addListItem(listItem1);
    	createdList.addListItem(listItem2);
    	
    	when(listServiceMock.createList(any(List.class), anyString())).thenReturn(createdList);
    	
    	mockMvc.perform(post("/api/lists")
    			.content(mapper.writeValueAsString(listDto))
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)));

		verify(listServiceMock, times(1)).createList(any(List.class), anyString());
		verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test Put List")
    @Test
    @WithMockUser(username = "testUser")
    public void testPutList() throws Exception {
       	ListDto listDto = new ListDto();
    	listDto.setName("Päivitetty nimi");
    	listDto.setDescription("Päivitetty kuvaus");
    	
    	ArrayList<ListItemDto> listItemsDto = new ArrayList<ListItemDto>();
    	
    	ListItemDto listItemDto = new ListItemDto();
    	listItemDto.setId(1);
    	listItemDto.setName("Testi");
    	listItemDto.setPriority("Tärkeä");
    	listItemDto.setQuantity(5);
    	listItemsDto.add(listItemDto);
    	
    	listDto.setListItems(listItemsDto);
    	
    	List list = new List();
    	list.setId(1);
    	list.setName("Päivitetty nimi");
    	list.setDescription("Päivitetty kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	
    	list.setOwner(user);
    	
    	ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    	
    	ListItem listItem = new ListItem();
    	listItem.setId(1);
    	listItem.setList(list);
    	listItem.setName("Testi");
    	listItem.setOwner(user);
    	listItem.setPriority("Tärkeä");
    	listItem.setQuantity(5);
    	listItems.add(listItem);
    	
    	list.setListItems(listItems);
    	
    	when(listServiceMock.editList(any(List.class), anyString())).thenReturn(list);
    	
    	mockMvc.perform(put("/api/lists/{id}", 1)
    			.content(mapper.writeValueAsString(listDto))
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Päivitetty nimi")));

    	verify(listServiceMock, times(1)).editList(any(List.class), anyString());
    	verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test Delete List")
    @Test
    @WithMockUser(username = "testUser")
    public void testDeleteList() throws Exception {
    	when(listServiceMock.deleteList(1, "testUser")).thenReturn(1);
    	 
        mockMvc.perform(delete("/api/lists/{id}", 1)
    			.contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
 
        verify(listServiceMock, times(1)).deleteList(1,"testUser");
        verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test Add List Item into the List")
    @Test
    @WithMockUser(username = "testUser")
    public void testAddListItem() throws Exception {
    	List list = new List();
    	list.setId(1);
    	list.setName("Päivitetty nimi");
    	list.setDescription("Päivitetty kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	
    	list.setOwner(user);
    	
    	ListItemDto listItemDto = new ListItemDto();
    	listItemDto.setName("Testi");
    	listItemDto.setPriority("Tärkeä");
    	listItemDto.setQuantity(5);
    	listItemDto.setChecked(true);
    	
    	ListItem listItem = new ListItem();
    	listItem.setId(1);
    	listItem.setList(list);
    	listItem.setOwner(user);
    	listItem.setPriority("Tärkeä");
    	listItem.setName("Testi");
    	listItem.setChecked(true);
    	listItem.setQuantity(5);
    	
    	when(listItemServiceMock.addListItem(anyInt(), any(ListItem.class), anyString())).thenReturn(listItem);
    	
        mockMvc.perform(post("/api/lists/{id}/listitems", 1)
        		.content(mapper.writeValueAsString(listItemDto))
        		.contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.name", is("Testi")))
        	   .andExpect(jsonPath("$.priority", is("Tärkeä")));
        
        verify(listItemServiceMock, times(1)).addListItem(anyInt(), any(ListItem.class), anyString());
        verifyNoMoreInteractions(listItemServiceMock);
    }
    
    @DisplayName("Test Delete List Items")
    @Test
    @WithMockUser(username = "testUser") 
    public void testDeleteListItems() throws Exception {

    	doAnswer((i) -> {
			return null;
		}).when(listItemServiceMock).deleteListItems(anyInt(), anyList(), anyString());
    	
    	ListItemDto listItemDto1 = new ListItemDto();
    	listItemDto1.setId(1);
    	listItemDto1.setName("Testi");
    	listItemDto1.setPriority("Tärkeä");
    	listItemDto1.setQuantity(5);
    	listItemDto1.setChecked(true);
    	
    	ListItemDto listItemDto2 = new ListItemDto();
    	listItemDto2.setId(2);
    	listItemDto2.setName("Testi");
    	listItemDto2.setPriority("Tärkeä");
    	listItemDto2.setQuantity(5);
    	listItemDto2.setChecked(true);
    	
    	ArrayList<ListItemDto> listItemsDto = new ArrayList<ListItemDto>();
    	listItemsDto.add(listItemDto1);
    	listItemsDto.add(listItemDto2);
    	
        mockMvc.perform(delete("/api/lists/{id}/listitems", 1)
        		.content(mapper.writeValueAsString(listItemsDto))
        		.contentType(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk());
        
        verify(listItemServiceMock, times(1)).deleteListItems(anyInt(), anyList(), anyString());
        verifyNoMoreInteractions(listItemServiceMock);    	
    }
    
    @DisplayName("Test Patch List Items")
    @Test
    @WithMockUser(username = "testUser")
    public void testPatchListItems() throws Exception {
    	ListItemDto listItemDto1 = new ListItemDto();
    	listItemDto1.setId(1);
    	listItemDto1.setName("Testi");
    	listItemDto1.setPriority("Tärkeä");
    	listItemDto1.setQuantity(5);
    	listItemDto1.setChecked(true);
    	
    	ListItemDto listItemDto2 = new ListItemDto();
    	listItemDto2.setId(2);
    	listItemDto2.setName("Testi");
    	listItemDto2.setPriority("Tärkeä");
    	listItemDto2.setQuantity(5);
    	listItemDto2.setChecked(true);
    	
    	ArrayList<ListItemDto> listItemsDto = new ArrayList<ListItemDto>();
    	listItemsDto.add(listItemDto1);
    	listItemsDto.add(listItemDto2);
    	
    	List list = new List();
    	list.setId(1);
    	list.setName("Päivitetty nimi");
    	list.setDescription("Päivitetty kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	
    	ListItem listItem1 = new ListItem();
    	listItem1.setId(1);
    	listItem1.setList(list);
    	listItem1.setOwner(user);
    	listItem1.setPriority("Tärkeä");
    	listItem1.setName("Testi");
    	listItem1.setChecked(true);
    	listItem1.setQuantity(5);
    	
    	ListItem listItem2 = new ListItem();
    	listItem2.setId(2);
    	listItem2.setList(list);
    	listItem2.setOwner(user);
    	listItem2.setPriority("Tärkeä");
    	listItem2.setName("Testi");
    	listItem2.setChecked(true);
    	listItem2.setQuantity(5);
    	
    	ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    	listItems.add(listItem1);
    	listItems.add(listItem2);
    	
    	when(listItemServiceMock.patchListItems(anyInt(), anyList(), anyString())).thenReturn(listItems);
    	
    	mockMvc.perform(patch("/api/lists/{id}/listitems", 1)
        		.content(mapper.writeValueAsString(listItemsDto))
        		.contentType(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk());
        
        verify(listItemServiceMock, times(1)).patchListItems(anyInt(), anyList(), anyString());
        verifyNoMoreInteractions(listItemServiceMock);
    }
    
    @DisplayName("Test Edit List Item")
    @Test
    @WithMockUser(username = "testUser")
    public void testEditListItem() throws Exception {
    	ListItemDto listItemDto = new ListItemDto();
    	listItemDto.setId(1);
    	listItemDto.setName("Testi");
    	listItemDto.setPriority("Tärkeä");
    	listItemDto.setQuantity(5);
    	listItemDto.setChecked(true);
    	
    	List list = new List();
    	list.setId(1);
    	list.setName("Päivitetty nimi");
    	list.setDescription("Päivitetty kuvaus");
    	
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	ListItem listItem = new ListItem();
    	listItem.setId(1);
    	listItem.setList(list);
    	listItem.setOwner(user);
    	listItem.setPriority("Tärkeä");
    	listItem.setName("Testi");
    	listItem.setChecked(true);
    	listItem.setQuantity(5);
    	
    	when(listItemServiceMock.editListItem(any(ListItem.class), anyString())).thenReturn(listItem);
    	
    	mockMvc.perform(put("/api/lists/{id}/listitems/{listItemId}", 1, 1)
        		.content(mapper.writeValueAsString(listItemDto))
        		.contentType(MediaType.APPLICATION_JSON_VALUE))
	    	.andExpect(status().isOk());
        
        verify(listItemServiceMock, times(1)).editListItem(any(ListItem.class), anyString());
        verifyNoMoreInteractions(listItemServiceMock);
    }
    
    @DisplayName("Test Get List")
    @Test
    @WithMockUser(username = "testUser")
    public void testGetList() throws Exception {
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
    	
    	ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    	
    	ListItem listItem = new ListItem();
    	listItem.setId(1);
    	listItem.setList(list);
    	listItem.setName("Testi");
    	listItem.setOwner(user);
    	listItem.setPriority("Tärkeä");
    	listItem.setQuantity(5);
    	
    	list.addListItem(listItem);
    	
    	list.setListItems(listItems);
    	list.setOwner(user);

    	when(listServiceMock.getList(1, "testUser")).thenReturn(list);
 
        mockMvc.perform(get("/api/lists/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
 
        verify(listServiceMock, times(1)).getList(1,"testUser");
        verifyNoMoreInteractions(listServiceMock);
    }
    
    @DisplayName("Test Get Lists")
    @Test
    @WithMockUser(username = "testUser")
    public void testGetLists() throws Exception {
    	List list1 = new List();
    	list1.setId(1);
    	list1.setName("Nimi1");
    	list1.setDescription("Kuvaus1");
    	
    	List list2 = new List();
    	list2.setId(2);
    	list2.setName("Nimi2");
    	list2.setDescription("Kuvaus2");

    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("oskari.forsblom@gmail.com");
    	user.setFirstName("Oskari");
    	user.setLastName("Forsblom");
    	user.setProfilePicture("/kuva");
    	
    	ArrayList<ListItem> emptyArrayList = new ArrayList<ListItem>();
    	list1.setListItems(emptyArrayList);
    	list1.setOwner(user);
    	
    	list2.setListItems(emptyArrayList);
    	list2.setOwner(user);
    	
    	when(listServiceMock.getLists("testUser")).thenReturn(Arrays.asList(list1,list2));
    	
    	this.mockMvc.perform(get("/api/lists"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Nimi1")))
        .andExpect(jsonPath("$[0].description", is("Kuvaus1")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Nimi2")))
        .andExpect(jsonPath("$[1].description", is("Kuvaus2")));
    	verify(listServiceMock, times(1)).getLists("testUser");
        verifyNoMoreInteractions(listServiceMock);
    }
}
