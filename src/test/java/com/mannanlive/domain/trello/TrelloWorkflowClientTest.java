package com.mannanlive.domain.trello;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TrelloWorkflowClientTest {
    private static final String CARD_ID = "your-card";
    private static final String BOARD = "your-board";
    private static final String API_KEY = "your-api-key";
    private static final String API_SECRET = "your-api-secret";

    private final TrelloWorkflowClient repo = new TrelloWorkflowClient(API_KEY, API_SECRET);

    @Test
    @Ignore
    public void addComment() {
        assertTrue(repo.addComment(BOARD, CARD_ID, "This is a **test** comment with a " +
                "[link](https://mannanlive.com) and some code.\n\n```int i = 0;```"));
    }

    @Test
    @Ignore
    public void updateStatus() {
        assertTrue(repo.updateStatus(BOARD, CARD_ID, "progress"));
    }
}