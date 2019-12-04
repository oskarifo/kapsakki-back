package fi.forsblom.kapsakki.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class User {
	private String id;
	private String email;
	private String profilePicture;
	private String firstName;
	private String lastName;

}
