package com.echg.project.cayusedemo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        body = body!=null? body : defaultErrorResponse(ex, status, request);
        log.warn("Resolving Exception: " + ex.toString() + " ResponseBody: " + body);
        ex.printStackTrace();

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Build a standard {@link ErrorResponse} to return to the client in case a more specialized response is not
     * already built in the custom exception handler method.
     * @param ex - the {@link Exception} that was raised.
     * @param status - the {@link HttpStatus} if any was set prior.
     * @param request - the request.
     * @return - the default error response.
     */
    private ErrorResponse defaultErrorResponse(Exception ex, HttpStatus status, WebRequest request){
        int statusCode = status.value()!=0? status.value() : INTERNAL_SERVER_ERROR.value();
        ErrorResponse errorResponse = ErrorResponse.fromException(ex, (NativeWebRequest) request)
                .setStatus(statusCode)
                .setError("Something went wrong! Please contact a System Administrator.");

        return errorResponse;
    }

    /**
     * Handler for {@link IllegalArgumentException}.
     * @param ex - the exception.
     * @param request - the request.
     * @return - The error  message with an {@link HttpStatus#BAD_REQUEST} 400 status.
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadData(RuntimeException ex, NativeWebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.fromException(ex, request)
                .setStatus(BAD_REQUEST.value())
                .setError("Bad Request, IllegalArgument.");

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handler for the {@link ResourceNotFoundException}. Mimic the spring data rest behavior in custom controllers
     * by simply returning a 404 {@link HttpStatus#NOT_FOUND} status code.
     * @param ex - the exception.
     * @param request - the request.
     * @return - the response entity with the 404 not found status code.
     */
    @ExceptionHandler(value = { ResourceNotFoundException.class })
    protected ResponseEntity<Object> handleResourceNotFound(RuntimeException ex, NativeWebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.fromException(ex,request)
                .setStatus(NOT_FOUND.value())
                .setError("The requested resource was not found.");

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handler for {@link UnsupportedOperationException}.
     * @param ex - the exception.
     * @param request - the request.
     * @return - The error  message with an {@link HttpStatus#BAD_REQUEST} 400 status.
     */
    @ExceptionHandler(value = { UnsupportedOperationException.class })
    protected ResponseEntity<Object> handleUnsupportedOp(RuntimeException ex, NativeWebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.fromException(ex, request)
                .setStatus(BAD_REQUEST.value())
                .setError("Bad Request, Unsupported Operation.");

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
