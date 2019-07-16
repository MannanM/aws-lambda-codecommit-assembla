package com.mannanlive.domain;

import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.codecommit.model.UserInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentMessageTest {

    @Test
    public void create() {
        final Commit commit = new Commit().withAuthor(new UserInfo().withName("Mannan Mackie"))
                .withCommitId("038f58ab000ab391c8e5c115bfaea6c5ebb808db")
                .withMessage("Test \"X\" Message");

        assertEquals("Mannan Mackie has [[url:https://ap-southeast-2.console.aws.amazon.com/" +
                "codesuite/codecommit/repositories/your-rep/commit/038f58ab000ab391c8e5c115bfaea6c5ebb808db" +
                "?region=ap-southeast-2|committed a change]] related to this ticket: " +
                "<pre><code>Test \"X\" Message</code></pre>",
                CommentMessage.create("ap-southeast-2", "your-rep", commit));
    }
}