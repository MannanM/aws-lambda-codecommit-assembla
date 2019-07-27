package com.mannanlive.domain.assembla;

import com.amazonaws.services.codecommit.model.Commit;
import com.mannanlive.domain.WorkflowComment;
import com.mannanlive.service.CodeCommitRepository;

import static java.lang.String.format;

public class AssemblaComment extends WorkflowComment {

    public AssemblaComment(String region, String repositoryName) {
        super(region, repositoryName);
    }

    public String calculate(final Commit commit) {
        return format("%s has [[url:%s|committed a change]] related to this ticket: <pre><code>%s</code></pre>",
                commit.getAuthor().getName(), getUrl(commit), commit.getMessage());
    }
}
