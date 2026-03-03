package com.dauphine.blogger.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    // 404 - Not Found
    @ExceptionHandler({
            CategoryNotFoundByIdException.class,
            PostNotFoundByIdException.class
    })
    public ResponseEntity<String> handleNotFoundException(Exception ex) {
        logger.warn("[NOT FOUND] {}", ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(ex.getMessage());
    }

    // 400 - Bad Request
    @ExceptionHandler({
            CategoryNameAlreadyExistsException.class,
            PostTitleAlreadyExistsException.class
    })
    public ResponseEntity<String> handleBadRequestException(Exception ex) {
        logger.warn("[BAD REQUEST] {}", ex.getMessage());
        return ResponseEntity
                .status(400)
                .body(ex.getMessage());
    }
}
