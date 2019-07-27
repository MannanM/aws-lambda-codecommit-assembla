package com.mannanlive.domain;

public interface WorkflowClient {
    boolean addComment(final String board, final String cardId, final String comment);
    boolean updateStatus(final String board, final String cardId, final String status);
}
