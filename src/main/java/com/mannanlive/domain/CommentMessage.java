package com.mannanlive.domain;

import com.amazonaws.services.codecommit.model.Commit;

public abstract class CommentMessage {
    private static final String AWS_CONSOLE_URL = "https://%s.console.aws.amazon.com/" +
            "codesuite/codecommit/repositories/%s/commit/%s?region=%s";

    public static String create(final String region, final String repository, final Commit commit) {
        final String commitUrl = String.format(AWS_CONSOLE_URL, region, repository, commit.getCommitId(), region);
        return String.format("%s has [[url:%s|committed a change]] related to this ticket: <pre><code>%s</code></pre>",
                commit.getAuthor().getName(), commitUrl, commit.getMessage());
    }
}
