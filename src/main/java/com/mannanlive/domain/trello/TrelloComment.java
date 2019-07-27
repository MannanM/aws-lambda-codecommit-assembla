package com.mannanlive.domain.trello;

import com.amazonaws.services.codecommit.model.Commit;
import com.mannanlive.domain.WorkflowComment;

import static java.lang.String.format;

public class TrelloComment extends WorkflowComment {

    public TrelloComment(String region, String repositoryName) {
        super(region, repositoryName);
    }

    public String calculate(final Commit commit) {
        return format("%s has [committed a change](%s) related to this ticket:\n\n```%s```",
                commit.getAuthor().getName(), getUrl(commit), commit.getMessage());
    }
}
