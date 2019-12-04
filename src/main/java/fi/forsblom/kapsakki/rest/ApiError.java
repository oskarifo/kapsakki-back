package fi.forsblom.kapsakki.rest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@SuppressWarnings("serial")
@JsonInclude(Include.NON_NULL)
public class ApiError implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private HttpStatus status;
	private String message;	
	private String detailedMessage;
	private List<ValidationError> validationErrors;
	
	private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.detailedMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.detailedMessage = ex.getLocalizedMessage();
    }
    
    private void addValidationError(FieldError fieldError) {
    	if (this.validationErrors == null) {
    		this.validationErrors = new ArrayList<ValidationError>();
    	}
    	this.validationErrors.add(new ValidationError(
    			fieldError.getField(),
                fieldError.getRejectedValue().toString(),
                fieldError.getDefaultMessage()));
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
    	this.detailedMessage = null;
        fieldErrors.forEach(this::addValidationError);
    }
}
