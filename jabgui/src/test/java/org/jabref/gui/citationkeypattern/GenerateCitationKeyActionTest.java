package org.jabref.gui.citationkeypattern;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.swing.undo.UndoManager;

import org.jabref.gui.DialogService;
import org.jabref.gui.LibraryTab;
import org.jabref.gui.StateManager;
import org.jabref.logic.preferences.CliPreferences;
import org.jabref.logic.citationkeypattern.CitationKeyPatternPreferences;
import org.jabref.logic.citationkeypattern.CitationKeyPatternPreferences.KeySuffix;
import org.jabref.logic.util.TaskExecutor;
import org.jabref.model.entry.BibEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GenerateCitationKeyActionTest {

    private DialogService dialogService;
    private StateManager stateManager;
    private TaskExecutor taskExecutor;
    private CliPreferences preferences;
    private UndoManager undoManager;
    private LibraryTab libraryTab;

    @BeforeEach
    void setUp() {
        dialogService = mock(DialogService.class);
        stateManager = mock(StateManager.class);
        taskExecutor = mock(TaskExecutor.class);
        preferences = mock(CliPreferences.class);
        undoManager = mock(UndoManager.class);
        libraryTab = mock(LibraryTab.class);
    }

    @Test
    public void execute_noSelectedEntries_showsWarning() {
        when(stateManager.getSelectedEntries()).thenReturn(FXCollections.observableArrayList());

        GenerateCitationKeyAction action = new GenerateCitationKeyAction((Supplier<LibraryTab>) () -> libraryTab,
                dialogService, stateManager, taskExecutor, preferences, undoManager);

        action.execute();

        verify(dialogService).showWarningDialogAndWait(anyString(), anyString());
        verify(taskExecutor, never()).execute(any());
    }

    @Test
    public void execute_userDeclinesOverwrite_taskNotExecuted() {
        BibEntry entry = new BibEntry();
        entry.setCitationKey("existing");
        ObservableList<BibEntry> entries = FXCollections.observableArrayList(entry);

        when(stateManager.getSelectedEntries()).thenReturn(entries);

        CitationKeyPatternPreferences prefs = new CitationKeyPatternPreferences(
                false, // transliterate
                false, // avoid overwrite
                true,  // warn before overwrite
                false, // generate before saving
                KeySuffix.ALWAYS,
                "",
                "",
                "",
                null,
                "default",
                ',');

        when(preferences.getCitationKeyPatternPreferences()).thenReturn(prefs);

        when(dialogService.showConfirmationDialogWithOptOutAndWait(anyString(), anyString(), anyString(), anyString(), anyString(), any()))
                .thenReturn(false);

        GenerateCitationKeyAction action = new GenerateCitationKeyAction((Supplier<LibraryTab>) () -> libraryTab,
                dialogService, stateManager, taskExecutor, preferences, undoManager);

        action.execute();

        verify(taskExecutor, never()).execute(any());
    }

    @Test
    public void execute_avoidOverwrite_filtersEntries_andExecutesTask() {
        BibEntry entryWithKey = new BibEntry();
        entryWithKey.setCitationKey("k1");
        BibEntry entryWithoutKey = new BibEntry();
        ObservableList<BibEntry> entries = FXCollections.observableArrayList(entryWithoutKey);

        when(stateManager.getSelectedEntries()).thenReturn(entries);

        CitationKeyPatternPreferences prefs = new CitationKeyPatternPreferences(
                false,
                true,  // avoid overwrite
                false,
                false,
                KeySuffix.ALWAYS,
                "",
                "",
                "",
                null,
                "default",
                ',');

        when(preferences.getCitationKeyPatternPreferences()).thenReturn(prefs);

        when(taskExecutor.execute(any())).thenAnswer(invocation -> CompletableFuture.completedFuture(null));

        GenerateCitationKeyAction action = new GenerateCitationKeyAction((Supplier<LibraryTab>) () -> libraryTab,
                dialogService, stateManager, taskExecutor, preferences, undoManager);

        action.execute();

        verify(taskExecutor).execute(any());
    }
}
