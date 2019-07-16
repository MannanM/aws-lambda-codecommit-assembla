package com.mannanlive.handler;

import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CodeCommitEvent;
import com.mannanlive.domain.TicketAction;
import com.mannanlive.domain.TicketExtractor;
import com.mannanlive.repository.AssemblaRepository;
import com.mannanlive.repository.CodeCommitRepository;

import java.util.List;

public class CodeCommitHandler implements RequestHandler<CodeCommitEvent, Void> {

    private CodeCommitRepository codeCommit = new CodeCommitRepository();
    private AssemblaRepository assembla = new AssemblaRepository();

    public Void handleRequest(final CodeCommitEvent input, final Context context) {
        input.getRecords().forEach(record -> {
            String arn = record.getEventSourceArn();
            String repositoryName = arn.split(":")[5];

            record.getCodeCommit().getReferences().forEach(reference -> {
                final Commit commit = codeCommit.getCommit(repositoryName, reference.getCommit());
                final List<TicketAction> tickets = TicketExtractor.extract(commit.getMessage());
                tickets.forEach(ticket -> {
                    if (assembla.addComment(ticket.getTicketId(), record.getAwsRegion(), commit)) {
                        assembla.updateStatus(ticket.getTicketId(), ticket.getAction());
                    }
                });
            });
        });
        return null;
    }
}