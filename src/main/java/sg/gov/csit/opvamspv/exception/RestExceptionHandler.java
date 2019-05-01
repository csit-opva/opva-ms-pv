package sg.gov.csit.opvamspv.exception;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object>
    handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders httpHeaders, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

//    @ExceptionHandler({ResourceNotFoundException.class,OfficerNotFoundException.class})
//    public void springHandle(HttpServletResponse response) throws Exception {
//        response.sendError(HttpStatus.NOT_ACCEPTABLE.value());
//    }

//    @ExceptionHandler(OfficerNotFoundException.class)
//    public ResponseEntity<ApiError> handleOfficerNotFound(Exception ex, WebRequest request) {
//        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(),ex);
//
//        return new ResponseEntity<>(error, error.getStatus());


//    }





}
