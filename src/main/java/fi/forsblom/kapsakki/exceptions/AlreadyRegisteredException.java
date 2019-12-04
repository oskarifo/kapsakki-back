package fi.forsblom.kapsakki.exceptions;

@SuppressWarnings("serial")
public class AlreadyRegisteredException extends RuntimeException {
	public AlreadyRegisteredException(String message) {
		super(message);
	}

    public AlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }	
}
