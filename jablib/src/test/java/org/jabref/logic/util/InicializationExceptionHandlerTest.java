package org.jabref.logic.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InicializationExceptionHandlerTest {

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("root");
        String message = "Something failed";

        InicializationExceptionHandler ex =
                new InicializationExceptionHandler(message, cause);

        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testConstructorWithOnlyCauseUsesDefaultMessage() {
        Throwable cause = new RuntimeException("root");

        InicializationExceptionHandler ex =
                new InicializationExceptionHandler(cause);

        assertEquals("Failed to initialize JabRef modules", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
