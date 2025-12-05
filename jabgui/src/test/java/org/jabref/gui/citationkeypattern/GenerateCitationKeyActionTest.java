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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateCitationKeyActionTest {

    @Mock
    private DialogService dialogService;

    @Mock
    private StateManager stateManager;

    @Mock
    private TaskExecutor taskExecutor;

    @Mock
    private CliPreferences preferences;

    @Mock
    private UndoManager undoManager;

    @Mock
    private LibraryTab libraryTab;

    @Test
    public void execute_noSelectedEntries_showsWarning() {
        when(stateManager.getSelectedEntries()).thenReturn(Collections.emptyList());

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
        List<BibEntry> entries = List.of(entry);

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
        List<BibEntry> entries = List.of(entryWithKey, entryWithoutKey);

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
