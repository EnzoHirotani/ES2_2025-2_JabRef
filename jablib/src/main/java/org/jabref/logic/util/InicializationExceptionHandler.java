package org.jabref.logic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InicializationExceptionHandler extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(InicializationExceptionHandler.class);

    public InicializationExceptionHandler(String message, Throwable cause) {
        super(message, cause);
        LOGGER.error(message, cause);
    }

    public InicializationExceptionHandler(Throwable cause) {
        this("Failed to initialize JabRef modules", cause);
    }
}
