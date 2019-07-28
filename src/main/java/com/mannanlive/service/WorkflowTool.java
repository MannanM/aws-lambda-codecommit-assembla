package com.mannanlive.service;

import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.mannanlive.domain.TicketAction;
import com.mannanlive.domain.WorkflowComment;
import com.mannanlive.domain.WorkflowClient;

public class WorkflowTool {
    private final LambdaLogger logger = LambdaRuntime.getLogger();
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
            logger.log(String.format("Attempting to add comment to ticket/card '%s'...", ticket.getTicketId()));
            if (workflowClient.addComment(spaceOrBoard, ticket.getTicketId(), message)) {
                logger.log(String.format("Successfully added comment to ticket/card '%s'", ticket.getTicketId()));
                updateStatusIfNeeded(ticket);
            } else {
                logger.log(String.format("Failed to add comment to ticket/card '%s'", ticket.getTicketId()));
            }
        });
    }

    private void updateStatusIfNeeded(final TicketAction ticket) {
        if (ticket.getAction() != null) {
            logger.log(String.format("Attempting to update status of ticket/card '%s' to '%s",
                    ticket.getTicketId(), ticket.getAction()));
            if (workflowClient.updateStatus(spaceOrBoard, ticket.getTicketId(), ticket.getAction())) {
                logger.log(String.format("Successfully updated status of ticket/card '%s' to '%s'",
                        ticket.getTicketId(), ticket.getAction()));
            } else {
                logger.log(String.format("Failed to update status of ticket/card '%s' to '%s'",
                        ticket.getTicketId(), ticket.getAction()));
            }
        }
    }

    public String getRepositoryName() {
        return comment.getRepositoryName();
    }
}
