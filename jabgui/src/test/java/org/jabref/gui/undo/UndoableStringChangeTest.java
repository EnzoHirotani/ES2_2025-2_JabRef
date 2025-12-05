package org.jabref.gui.undo;

import org.jabref.model.entry.BibtexString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UndoableStringChangeTest {

    private BibtexString bibtexString;

    @BeforeEach
    void setUp() {
        bibtexString = mock(BibtexString.class);
    }

    @Test
    void testGetPresentationName_NameChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                true,
                "OldName",
                "NewName"
        );

        String presentation = edit.getPresentationName();

        assertTrue(presentation.contains("OldName"));
        assertTrue(presentation.contains("NewName"));
        assertTrue(presentation.toLowerCase().contains("change string name"));
    }

    @Test
    void testGetPresentationName_ContentChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                false,
                "oldContent",
                "newContent"
        );

        String presentation = edit.getPresentationName();

        assertTrue(presentation.contains("oldContent"));
        assertTrue(presentation.contains("newContent"));
        assertTrue(presentation.toLowerCase().contains("change string content"));
    }

    @Test
    void testUndo_NameChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                true,
                "OldName",
                "NewName"
        );

        edit.undo();

        verify(bibtexString, times(1)).setName("OldName");
        verify(bibtexString, never()).setContent(anyString());
    }

    @Test
    void testUndo_ContentChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                false,
                "oldValue",
                "newValue"
        );

        edit.undo();

        verify(bibtexString, times(1)).setContent("oldValue");
        verify(bibtexString, never()).setName(anyString());
    }

    @Test
    void testRedo_NameChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                true,
                "OldName",
                "NewName"
        );

        edit.undo();
        edit.redo();

        verify(bibtexString, times(1)).setName("NewName");
        verify(bibtexString, never()).setContent(anyString());
    }

    @Test
    void testRedo_ContentChange() {
        UndoableStringChange edit = new UndoableStringChange(
                bibtexString,
                false,
                "oldValue",
                "newValue"
        );

        edit.undo();
        edit.redo();

        verify(bibtexString, times(1)).setContent("newValue");
        verify(bibtexString, never()).setName(anyString());
    }
}
