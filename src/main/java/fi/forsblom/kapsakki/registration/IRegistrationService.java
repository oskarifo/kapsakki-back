package fi.forsblom.kapsakki.registration;

import java.security.Principal;

import fi.forsblom.kapsakki.user.User;

public interface IRegistrationService {
	public User register(String accessToken, Principal principal) throws Exception;
}
