package com.mannanlive.repository;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AssemblaRepositoryTest {
    private static final long TICKET_ID = 1263L;
    private static final String SPACE = "your-space";
    private static final String API_KEY = "your-api-key";
    private static final String API_SECRET = "your-api-secret";

    private final AssemblaRepository repo = new AssemblaRepository(SPACE, API_KEY, API_SECRET);

    @Test
    @Ignore
    public void addComment() {
        assertTrue(repo.addComment(TICKET_ID, "Test Message"));
    }

    @Test
    @Ignore
    public void updateStatus() {
        assertTrue(repo.updateStatus(TICKET_ID, "Progress"));
    }
}