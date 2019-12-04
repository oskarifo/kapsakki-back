package fi.forsblom.kapsakki.user;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.forsblom.kapsakki.list.ListController;

@RestController()
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	IUserService userService;

	private static Logger LOGGER = LoggerFactory.getLogger(ListController.class);

	@GetMapping()
	public User getUser(Principal principal) {
		LOGGER.debug("event={} user={}", "get-user-data", principal.getName());
		return userService.getUserById(principal.getName());
	}
	
	@DeleteMapping()
	public void deleteUser(Principal principal) {
		LOGGER.debug("event={} user={}", "delete-user-data", principal.getName());
		userService.deleteUserById(principal.getName());
	}
	
}
