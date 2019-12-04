package fi.forsblom.kapsakki.user;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import fi.forsblom.kapsakki.exceptions.UserNotFoundException;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {	
	
    @MockBean
    IUserService userServiceMock;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testUnauthroizedRequest() throws Exception {
    	this.mockMvc.perform(get("/api/user"))
    		.andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(username = "testUser")
    public void testGetUserById() throws Exception {
    	User user = new User();
    	user.setId("testUser");
    	user.setEmail("test@email.com");
    	user.setFirstName("testFirstName");
    	user.setLastName("testLastName");
    	user.setProfilePicture("testUrl");
    	
    	when(userServiceMock.getUserById("testUser")).thenReturn(user);

    	this.mockMvc.perform(get("/api/user"))
    		.andExpect(status().isOk());
    	
    	verify(userServiceMock, times(1)).getUserById("testUser");
        verifyNoMoreInteractions(userServiceMock);
    }
    
    @Test
    @WithMockUser(username = "testUser")
    public void testUserNotFound() throws Exception  {
    	when(userServiceMock.getUserById("testUser")).thenThrow(new UserNotFoundException(""));

    	this.mockMvc.perform(get("/api/user"))
    		.andExpect(status().isNotFound());
    	
    	verify(userServiceMock, times(1)).getUserById("testUser");
        verifyNoMoreInteractions(userServiceMock);
    }
    
    @Test
    @WithMockUser(username = "testUser")
    public void testDeleteUser() throws Exception {
    	doAnswer((i) -> {
			return null;
		}).when(userServiceMock).deleteUserById("testUser");
    	this.mockMvc.perform(delete("/api/user"))
			.andExpect(status().isOk());
	
    	verify(userServiceMock, times(1)).deleteUserById("testUser");
    	verifyNoMoreInteractions(userServiceMock);
    }
}
