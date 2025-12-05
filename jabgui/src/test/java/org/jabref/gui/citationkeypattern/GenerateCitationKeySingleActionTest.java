package org.jabref.gui.citationkeypattern;

import org.jabref.gui.DialogService;
import org.jabref.logic.citationkeypattern.CitationKeyGeneratorTestUtils;
import org.jabref.logic.preferences.CliPreferences;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.field.StandardField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.undo.UndoManager;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateCitationKeySingleActionTest {

    @Mock
    private DialogService dialogService;

    @Mock
    private CliPreferences preferences;

    @Mock
    private UndoManager undoManager;

    @Test
    public void execute_entryWithoutKey_addsUndo() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.AUTHOR, "Doe");
        entry.setField(StandardField.YEAR, "2020");

        BibDatabaseContext databaseContext = new BibDatabaseContext();
        databaseContext.getDatabase().insertEntry(entry);

        when(preferences.getCitationKeyPatternPreferences()).thenReturn(CitationKeyGeneratorTestUtils.getInstanceForTesting());

        GenerateCitationKeySingleAction action = new GenerateCitationKeySingleAction(entry, databaseContext, dialogService, preferences, undoManager);

        action.execute();

        verify(undoManager).addEdit(org.mockito.ArgumentMatchers.any());
    }

    @Test
    public void execute_entryWithKey_userDeclines_noUndo() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.AUTHOR, "Doe");
        entry.setField(StandardField.YEAR, "2020");
        entry.setCitationKey("existingKey");

        BibDatabaseContext databaseContext = new BibDatabaseContext();
        databaseContext.getDatabase().insertEntry(entry);

        var prefs = CitationKeyGeneratorTestUtils.getInstanceForTesting();
        prefs.setWarnBeforeOverwriteCiteKey(true);
        when(preferences.getCitationKeyPatternPreferences()).thenReturn(prefs);

        when(dialogService.showConfirmationDialogWithOptOutAndWait(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(false);

        GenerateCitationKeySingleAction action = new GenerateCitationKeySingleAction(entry, databaseContext, dialogService, preferences, undoManager);

        action.execute();

        verify(undoManager, never()).addEdit(org.mockito.ArgumentMatchers.any());
    }

    @Test
    public void execute_entryWithKey_userAccepts_addsUndo() {
        BibEntry entry = new BibEntry();
        entry.setField(StandardField.AUTHOR, "Doe");
        entry.setField(StandardField.YEAR, "2020");
        entry.setCitationKey("oldKey");

        BibDatabaseContext databaseContext = new BibDatabaseContext();
        databaseContext.getDatabase().insertEntry(entry);

        var prefs = CitationKeyGeneratorTestUtils.getInstanceForTesting();
        prefs.setWarnBeforeOverwriteCiteKey(true);
        when(preferences.getCitationKeyPatternPreferences()).thenReturn(prefs);

        when(dialogService.showConfirmationDialogWithOptOutAndWait(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any()))
                .thenReturn(true);

        GenerateCitationKeySingleAction action = new GenerateCitationKeySingleAction(entry, databaseContext, dialogService, preferences, undoManager);

        action.execute();

        verify(undoManager).addEdit(org.mockito.ArgumentMatchers.any());
    }
}
