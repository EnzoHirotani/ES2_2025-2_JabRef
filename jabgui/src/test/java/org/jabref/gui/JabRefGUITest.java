package org.jabref.gui;

import org.jabref.logic.util.InicializationExceptionHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JabRefGUITest {

    @Test
    void start_shouldThrowInicializationExceptionHandler_whenErrorOccurs() {
        JabRefGUI gui = new JabRefGUI();

        assertThrows(InicializationExceptionHandler.class, () -> gui.start(null));
    }
}
