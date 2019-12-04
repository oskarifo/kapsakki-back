package fi.forsblom.kapsakki.rest;

import org.springframework.core.annotation.Order;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import fi.forsblom.kapsakki.exceptions.AlreadyRegisteredException;
import fi.forsblom.kapsakki.exceptions.DatabaseException;
import fi.forsblom.kapsakki.exceptions.ForbiddenException;
import fi.forsblom.kapsakki.exceptions.NotFoundException;
import fi.forsblom.kapsakki.exceptions.UserNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {
	
	/*
	 * 
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException exception, 
																	   ServletWebRequest request) {
		return ResponseEntity
	            .ok()
	            .body(new ApiError(HttpStatus.BAD_REQUEST, "Validation error", exception));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException .class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValidException (MethodArgumentNotValidException  exception, 
																	   	   ServletWebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error", exception);
        apiError.addValidationErrors(exception.getBindingResult().getFieldErrors());
		
		return ResponseEntity
	            .status(400)
	            .body(apiError);
	}
	/*
	 * Custom exceptions
	 */
	@ExceptionHandler(AlreadyRegisteredException.class)
	public ResponseEntity<ApiError> handleAlreadyRegisteredException(AlreadyRegisteredException exception,
                                                                     ServletWebRequest request) {
        return ResponseEntity
            .ok()
            .body(new ApiError(HttpStatus.OK, "Already registered", exception));
    }
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception,
                                                            ServletWebRequest request) {
        return ResponseEntity
            .status(404)
            .body(new ApiError(HttpStatus.NOT_FOUND, "Not found", exception));
    }
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserFoundException(UserNotFoundException exception,
                                                             ServletWebRequest request) {
        return ResponseEntity
            .status(404)
            .body(new ApiError(HttpStatus.NOT_FOUND, "User not found", exception));
    }
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiError> handleForbiddenExcepton(ForbiddenException exception, 
															ServletWebRequest request) {
		return ResponseEntity
			.status(403)
			.body(new ApiError(HttpStatus.FORBIDDEN, "Forbidden", exception));
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ApiError> handleInternalDatabaseException(DatabaseException exception,
																	ServletWebRequest request) {
		return ResponseEntity
				.status(500)
				.body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Database failure", exception));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception exception,
																	ServletWebRequest request) {
		return ResponseEntity
				.status(500)
				.body(new ApiError(HttpStatus.BAD_REQUEST, "Generic exception", exception));
	}
}
