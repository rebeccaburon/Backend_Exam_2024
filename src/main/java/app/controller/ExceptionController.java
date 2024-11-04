package app.controller;

import app.exceptions.ApiException;
import app.exceptions.JPAException;
import app.exceptions.Message;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ExceptionController {
    private final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    public void handleException(int statusCode, String errorMessage, Context ctx) {
        log.error("{} {}", statusCode, errorMessage);
        ctx.status(statusCode);
        // Format the current LocalDateTime
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Create the Message instance
        Message errorResponse = new Message(statusCode, errorMessage);

        // Send the error response with the formatted time as a separate field
        ctx.json(Map.of(
                "error message", errorResponse,
                "time of error", formattedTime
        ));
    }

    public void apiExceptionHandler(ApiException e, Context ctx) {
        handleException(e.getStatusCodeForAPI(), e.getMessage(), ctx);
    }

    public void jpaExceptionHandler(JPAException e, Context ctx) {
        handleException(e.getStatusCodeForJPA(), e.getMessage(), ctx);
    }

    public void exceptionHandler(Exception e, Context ctx) {
        log.error("{} {}", 400, e.getMessage());
        ctx.status(400);
        ctx.result(e.getMessage());
    }



}

