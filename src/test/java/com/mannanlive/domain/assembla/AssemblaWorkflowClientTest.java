package com.mannanlive.domain.assembla;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AssemblaWorkflowClientTest {
    private static final String TICKET_ID = "1263";
    private static final String SPACE = "your-space";
    private static final String API_KEY = "your-api-key";
    private static final String API_SECRET = "your-api-secret";

    private final AssemblaWorkflowClient repo = new AssemblaWorkflowClient(API_KEY, API_SECRET);

    @Test
    @Ignore
    public void addComment() {
        assertTrue(repo.addComment(SPACE, TICKET_ID, "Test Message"));
    }

    @Test
    @Ignore
    public void updateStatus() {
        assertTrue(repo.updateStatus(SPACE, TICKET_ID, "Progress"));
    }
}