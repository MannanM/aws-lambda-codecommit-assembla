package com.mannanlive.domain;

import com.amazonaws.services.codecommit.model.Commit;

import static java.lang.String.format;

public abstract class WorkflowComment {
    private final String region;
    private final String repositoryName;

    public WorkflowComment(final String region, final String repositoryName) {
        this.region = region;
        this.repositoryName = repositoryName;
    }

    public abstract String calculate(final Commit commit);

    protected String getUrl(final Commit commit) {
        return format("https://%s.console.aws.amazon.com/codesuite/codecommit/repositories/%s/commit/%s?region=%s",
                region, repositoryName, commit.getCommitId(), region);
    }

    public String getRepositoryName() {
        return repositoryName;
    }
}
