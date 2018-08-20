package com.echg.project.cayusedemo.exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Data @Accessors
public class ErrorResponse {

    private static final Logger logger = LoggerFactory.getLogger(ErrorResponse.class);

    /** When the error happened. */
    private Instant timestamp;

    /** The http status code */
    private int     status;

    /** A short name describing what went wrong. */
    private String  error;

    /** The exception that was resolved and translated into this ErrorReponse. */
    private String  exception;

    private String  message;

    /** The uri of web service endpoint that was requested */
    private String  path;

    public static ErrorResponse fromException(Exception ex, NativeWebRequest request){
        String path = ((HttpServletRequest) request.getNativeRequest()).getServletPath();

        return new ErrorResponse()
                .setTimestamp(Instant.now())
                .setException(ex.getClass().getName())
                .setMessage(ex.getMessage())
                .setPath(path);
    }

    /**
     * @return - a String representation of this ErrorResponse. Used as a fallback if there is an issue with {@link #toString()}.
     */
    private String fallBackToString(){
        StringBuilder builder = new StringBuilder()
                .append("Timestamp: ")  .append(timestamp)  .append(", ")
                .append("Status: ")     .append(status)     .append(", ")
                .append("Error: ")      .append(error)      .append(", ")
                .append("Exception: ")  .append(exception)  .append(", ")
                .append("Message: ")    .append(message)    .append(", ")
                .append("Path: ")       .append(path)       .append(", ");

        return builder.toString();
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        String value;
        try {
            value = mapper.writeValueAsString(this);
        } catch(JsonProcessingException ex){
            value = fallBackToString();
            logger.warn("Unable to convert object to json string: "+ex);
        }
        return value;
    }
}
