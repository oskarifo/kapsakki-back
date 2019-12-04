package fi.forsblom.kapsakki.registration;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @NoArgsConstructor @ToString
public class IdToken implements Serializable {
	private String given_name;
	private String family_name;
	private String sub;
	private String email;
	private String picture;
}
