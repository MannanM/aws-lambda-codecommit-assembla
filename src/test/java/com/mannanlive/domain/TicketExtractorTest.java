package com.mannanlive.domain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TicketExtractorTest {

    @Test
    public void extract() {
        final List<TicketAction> actual = TicketExtractor.extract("#123");
        assertEquals(1, actual.size());
        assertEquals((Long) 123L, actual.get(0).getTicketId());
        assertNull(actual.get(0).getAction());
    }

    @Test
    public void none_found() {
        assertTrue(TicketExtractor.extract("aerafe").isEmpty());
    }

    @Test
    public void multiple_no_actions() {
        final List<TicketAction> actual = TicketExtractor.extract("#321, #456");
        assertEquals(2, actual.size());

        assertEquals((Long) 321L, actual.get(0).getTicketId());
        assertNull(actual.get(0).getAction());

        assertEquals((Long) 456L, actual.get(1).getTicketId());
        assertNull(actual.get(1).getAction());
    }

    @Test
    public void multiple_with_actions() {
        final List<TicketAction> actual = TicketExtractor.extract("Test #321, Complete #456");
        assertEquals(2, actual.size());

        assertEquals("Test", actual.get(0).getAction());
        assertEquals((Long) 321L, actual.get(0).getTicketId());

        assertEquals("Complete", actual.get(1).getAction());
        assertEquals((Long) 456L, actual.get(1).getTicketId());
    }

    @Test
    public void multiple_first_action() {
        final List<TicketAction> actual = TicketExtractor.extract("Test #321, #456");
        assertEquals(2, actual.size());

        assertEquals("Test", actual.get(0).getAction());
        assertEquals((Long) 321L, actual.get(0).getTicketId());

        assertEquals("Test", actual.get(1).getAction());
        assertEquals((Long) 456L, actual.get(1).getTicketId());
    }

    @Test
    public void multiple_last_action() {
        final List<TicketAction> actual = TicketExtractor.extract("#321, Test #456");
        assertEquals(2, actual.size());

        assertNull(actual.get(0).getAction());
        assertEquals((Long) 321L, actual.get(0).getTicketId());

        assertEquals("Test", actual.get(1).getAction());
        assertEquals((Long) 456L, actual.get(1).getTicketId());
    }
}