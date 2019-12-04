package fi.forsblom.kapsakki.exceptions;

@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException {
	public DatabaseException(String message) {
		super(message);
	}

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }	
}
