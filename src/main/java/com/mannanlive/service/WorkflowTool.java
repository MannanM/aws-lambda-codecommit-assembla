package com.mannanlive.service;

import com.amazonaws.services.codecommit.model.Commit;
import com.mannanlive.domain.WorkflowComment;
import com.mannanlive.domain.WorkflowClient;

public class WorkflowTool {
    private final WorkflowComment comment;
    private final WorkflowClient workflowClient;
    private final TicketExtractor ticketExtractor;
    private final String spaceOrBoard;

    public WorkflowTool(final WorkflowComment comment, final WorkflowClient workflowClient,
                        final TicketExtractor ticketExtractor, final String spaceOrBoard) {
        this.comment = comment;
        this.workflowClient = workflowClient;
        this.ticketExtractor = ticketExtractor;
        this.spaceOrBoard = spaceOrBoard;
    }

    public void process(final Commit commit) {
        ticketExtractor.extract(commit.getMessage()).forEach(ticket -> {
            final String message = comment.calculate(commit);
            if (workflowClient.addComment(spaceOrBoard, ticket.getTicketId(), message)) {
                workflowClient.updateStatus(spaceOrBoard, ticket.getTicketId(), ticket.getAction());
            }
        });
    }

    public String getRepositoryName() {
        return comment.getRepositoryName();
    }
}
