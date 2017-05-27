package project.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MasterExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, HttpServletRequest request)
    {
		return new ResponseEntity<String>("Handled Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
