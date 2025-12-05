package org.jabref.logic.importer.fetcher.citation;

import java.util.List;
import java.util.Optional;

import org.jabref.logic.importer.FetcherException;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.types.StandardEntryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CitationFetcherHelpersForTestTest {

    @Test
    public void mockFetcher_getCitations_returnsProvidedList() throws FetcherException {
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        entry.setCitationKey("test2020");
        
        BibEntry citing1 = new BibEntry(StandardEntryType.Article);
        citing1.setCitationKey("citing1");
        BibEntry citing2 = new BibEntry(StandardEntryType.Article);
        citing2.setCitationKey("citing2");
        
        List<BibEntry> expectedCitations = List.of(citing1, citing2);
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> expectedCitations,  // getCitations
                e -> List.of(),          // getReferences
                e -> Optional.empty()    // getCitationCount
        );
        
        List<BibEntry> citations = fetcher.getCitations(entry);
        
        assertEquals(2, citations.size());
        assertEquals("citing1", citations.get(0).getCitationKey().orElse(null));
        assertEquals("citing2", citations.get(1).getCitationKey().orElse(null));
    }

    @Test
    public void mockFetcher_getReferences_returnsProvidedList() throws FetcherException {
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        entry.setCitationKey("test2020");
        
        BibEntry ref1 = new BibEntry(StandardEntryType.Article);
        ref1.setCitationKey("ref1");
        BibEntry ref2 = new BibEntry(StandardEntryType.Article);
        ref2.setCitationKey("ref2");
        BibEntry ref3 = new BibEntry(StandardEntryType.Article);
        ref3.setCitationKey("ref3");
        
        List<BibEntry> expectedReferences = List.of(ref1, ref2, ref3);
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> List.of(),              // getCitations
                e -> expectedReferences,      // getReferences
                e -> Optional.empty()        // getCitationCount
        );
        
        List<BibEntry> references = fetcher.getReferences(entry);
        
        assertEquals(3, references.size());
        assertEquals("ref1", references.get(0).getCitationKey().orElse(null));
        assertEquals("ref2", references.get(1).getCitationKey().orElse(null));
        assertEquals("ref3", references.get(2).getCitationKey().orElse(null));
    }

    @Test
    public void mockFetcher_getCitationCount_returnsProvidedValue() throws FetcherException {
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        entry.setCitationKey("test2020");
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> List.of(),           // getCitations
                e -> List.of(),           // getReferences
                e -> Optional.of(42)      // getCitationCount returns 42
        );
        
        Optional<Integer> count = fetcher.getCitationCount(entry);
        
        assertTrue(count.isPresent());
        assertEquals(42, count.get());
    }

    @Test
    public void mockFetcher_getCitationCount_returnsEmpty() throws FetcherException {
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        entry.setCitationKey("test2020");
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> List.of(),           // getCitations
                e -> List.of(),           // getReferences
                e -> Optional.empty()     // getCitationCount returns empty
        );
        
        Optional<Integer> count = fetcher.getCitationCount(entry);
        
        assertTrue(count.isEmpty());
    }

    @Test
    public void mockFetcher_getName_returnsFixedValue() {
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> List.of(),
                e -> List.of(),
                e -> Optional.empty()
        );
        
        String name = fetcher.getName();
        
        assertEquals("Test citation fetcher", name);
    }

    @Test
    public void mockFetcher_emptyLists() throws FetcherException {
        BibEntry entry = new BibEntry(StandardEntryType.Article);
        entry.setCitationKey("test2020");
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> List.of(),
                e -> List.of(),
                e -> Optional.empty()
        );
        
        List<BibEntry> citations = fetcher.getCitations(entry);
        List<BibEntry> references = fetcher.getReferences(entry);
        Optional<Integer> count = fetcher.getCitationCount(entry);
        
        assertTrue(citations.isEmpty());
        assertTrue(references.isEmpty());
        assertTrue(count.isEmpty());
    }

    @Test
    public void mockFetcher_usesProvidedFunctions() throws FetcherException {
        BibEntry entry1 = new BibEntry(StandardEntryType.Article);
        entry1.setCitationKey("entry1");
        BibEntry entry2 = new BibEntry(StandardEntryType.Article);
        entry2.setCitationKey("entry2");
        
        BibEntry citation = new BibEntry(StandardEntryType.Article);
        citation.setCitationKey("citation");
        
        CitationFetcher fetcher = CitationFetcherHelpersForTest.Mocks.from(
                e -> e.getCitationKey().orElse("").equals("entry1") ? List.of(citation) : List.of(),
                e -> e.getCitationKey().orElse("").equals("entry2") ? List.of(citation) : List.of(),
                e -> e.getCitationKey().orElse("").equals("entry1") ? Optional.of(1) : Optional.empty()
        );
        
        List<BibEntry> citationsForEntry1 = fetcher.getCitations(entry1);
        List<BibEntry> citationsForEntry2 = fetcher.getCitations(entry2);
        Optional<Integer> countForEntry1 = fetcher.getCitationCount(entry1);
        Optional<Integer> countForEntry2 = fetcher.getCitationCount(entry2);
        
        assertEquals(1, citationsForEntry1.size());
        assertEquals(0, citationsForEntry2.size());
        assertEquals(1, countForEntry1.get());
        assertTrue(countForEntry2.isEmpty());
    }
}
