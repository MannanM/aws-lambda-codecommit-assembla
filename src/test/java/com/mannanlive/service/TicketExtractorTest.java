package com.mannanlive.service;

import com.mannanlive.domain.TicketAction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TicketExtractorTest {

    private final TicketExtractor ticketExtractor = new TicketExtractor("#", "\\d+");

    @Test
    public void extract() {
        final List<TicketAction> actual = ticketExtractor.extract("#123");
        assertEquals(1, actual.size());
        assertEquals("123", actual.get(0).getTicketId());
        assertNull(actual.get(0).getAction());
    }

    @Test
    public void extract_different_regex() {
        final List<TicketAction> actual = new TicketExtractor("--", "[a-z0-9]+")
                .extract("blah --ajflj34304834 alkjfd");
        assertEquals(1, actual.size());
        assertEquals("ajflj34304834", actual.get(0).getTicketId());
        assertEquals("blah", actual.get(0).getAction());
    }

    @Test
    public void none_found() {
        assertTrue(ticketExtractor.extract("aerafe").isEmpty());
    }

    @Test
    public void multiple_no_actions() {
        final List<TicketAction> actual = ticketExtractor.extract("#321, #456");
        assertEquals(2, actual.size());

        assertEquals("321", actual.get(0).getTicketId());
        assertNull(actual.get(0).getAction());

        assertEquals("456", actual.get(1).getTicketId());
        assertNull(actual.get(1).getAction());
    }

    @Test
    public void multiple_with_actions() {
        final List<TicketAction> actual = ticketExtractor.extract("Test #321, Complete #456");
        assertEquals(2, actual.size());

        assertEquals("Test", actual.get(0).getAction());
        assertEquals("321", actual.get(0).getTicketId());

        assertEquals("Complete", actual.get(1).getAction());
        assertEquals("456", actual.get(1).getTicketId());
    }

    @Test
    public void multiple_first_action() {
        final List<TicketAction> actual = ticketExtractor.extract("Test #321, #456");
        assertEquals(2, actual.size());

        assertEquals("Test", actual.get(0).getAction());
        assertEquals("321", actual.get(0).getTicketId());

        assertEquals("Test", actual.get(1).getAction());
        assertEquals("456", actual.get(1).getTicketId());
    }

    @Test
    public void multiple_last_action() {
        final List<TicketAction> actual = ticketExtractor.extract("#321, Test #456");
        assertEquals(2, actual.size());

        assertNull(actual.get(0).getAction());
        assertEquals("321", actual.get(0).getTicketId());

        assertEquals("Test", actual.get(1).getAction());
        assertEquals("456", actual.get(1).getTicketId());
    }
}