package com.mannanlive.service;

import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
import com.mannanlive.domain.assembla.AssemblaComment;
import com.mannanlive.domain.assembla.AssemblaWorkflowClient;
import com.mannanlive.domain.trello.TrelloComment;
import com.mannanlive.domain.trello.TrelloWorkflowClient;

public class WorkflowToolBuilder {

    public WorkflowTool create(final CodeCommitEvent.Record record) {
        final String[] segments = record.getCustomData().split(":");
        final String tool = segments[0];
        final String spaceOrBoard = segments[1];
        final String region = record.getAwsRegion();
        final String repositoryName = record.getEventSourceArn().split(":")[5];

        switch (tool.toLowerCase()) {
            case "assembla":
                return new WorkflowTool(
                        new AssemblaComment(region, repositoryName),
                        new AssemblaWorkflowClient(),
                        new TicketExtractor("#", "\\d+"),
                        spaceOrBoard);
            case "trello":
                return new WorkflowTool(
                        new TrelloComment(region, repositoryName),
                        new TrelloWorkflowClient(),
                        new TicketExtractor("#", "[a-z0-9]+"),
                        spaceOrBoard);
            default:
                throw new IllegalArgumentException(tool + " is not a supported workflow tool");
        }
    }
}
