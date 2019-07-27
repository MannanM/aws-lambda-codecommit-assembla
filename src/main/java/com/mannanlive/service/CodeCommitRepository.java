package com.mannanlive.service;

import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.AWSCodeCommitClientBuilder;
import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.codecommit.model.GetCommitRequest;

public class CodeCommitRepository {
    private static final AWSCodeCommit CLIENT = AWSCodeCommitClientBuilder.defaultClient();

    public Commit getCommit(final String repositoryName, final String commitId) {
        return CLIENT.getCommit(
                new GetCommitRequest()
                        .withRepositoryName(repositoryName)
                        .withCommitId(commitId)
        ).getCommit();
    }
}
