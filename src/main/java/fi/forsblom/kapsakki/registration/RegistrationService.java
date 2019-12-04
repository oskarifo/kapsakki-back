package fi.forsblom.kapsakki.registration;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.user.User;
import fi.forsblom.kapsakki.user.UserService;

@Service
public class RegistrationService implements IRegistrationService {
	
	@Autowired
	UserService userService;
	
	@Override
	public User register(String accessToken, Principal principal) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = "https://dev-forceblom.eu.auth0.com/userinfo";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", accessToken);

		try {
			HttpEntity<Map<String, String> > entity = new HttpEntity<Map<String, String> >(null, headers);
			IdToken result = restTemplate.postForObject(resourceUrl, entity, IdToken.class);
			User user =  new User();
			user.setId(result.getSub());
			user.setEmail(result.getEmail());
			user.setProfilePicture(result.getPicture());
			user.setFirstName(result.getGiven_name());
			user.setLastName(result.getFamily_name());

			return userService.createUser(user);
		    
		} catch (HttpClientErrorException.NotFound exc) {
			throw new NotFoundException("URL not found");
		}
		
	}
}
