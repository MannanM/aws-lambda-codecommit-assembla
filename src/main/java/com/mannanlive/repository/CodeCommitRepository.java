package com.mannanlive.repository;

import com.amazonaws.services.codecommit.AWSCodeCommit;
import com.amazonaws.services.codecommit.AWSCodeCommitClientBuilder;
import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.codecommit.model.GetCommitRequest;

public class CodeCommitRepository {
    private static final AWSCodeCommit CLIENT = AWSCodeCommitClientBuilder.defaultClient();

    public Commit getCommit(String repositoryName, String commitId) {
        return CLIENT.getCommit(
                new GetCommitRequest()
                        .withRepositoryName(repositoryName)
                        .withCommitId(commitId)
        ).getCommit();
    }
}
