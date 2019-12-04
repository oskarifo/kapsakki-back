package fi.forsblom.kapsakki.registration;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.forsblom.kapsakki.user.User;

@RestController()
@RequestMapping("/api/register")
public class RegistrationController {
	
	@Autowired
	RegistrationService registerService;
	
	@PostMapping()
	public User register(@RequestHeader("Authorization") String accessToken, Principal principal) throws Exception {
		User user = registerService.register(accessToken, principal);
		
		return user;
	}
}
