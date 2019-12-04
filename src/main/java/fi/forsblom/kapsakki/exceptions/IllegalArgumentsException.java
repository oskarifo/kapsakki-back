package fi.forsblom.kapsakki.exceptions;

@SuppressWarnings("serial")
public class IllegalArgumentsException extends RuntimeException {
	public IllegalArgumentsException(String message) {
		super(message);
	}

    public IllegalArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }	
}
