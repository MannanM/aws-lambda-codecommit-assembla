package com.mannanlive.repository;

import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.codecommit.model.UserInfo;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class AssemblaRepositoryTest {
    private static final long TICKET_ID = 1263L;
    private static final String SPACE = "your-space";
    private static final String API_KEY = "your-api-key";
    private static final String API_SECRET = "your-api-secret";

    private final AssemblaRepository repo = new AssemblaRepository(SPACE, API_KEY, API_SECRET);

    @Test
    @Ignore
    public void addComment() {
        final Commit commit = new Commit().withAuthor(new UserInfo().withName("Mannan Mackie"))
                                          .withCommitId("038f58ab000ab391c8e5c115bfaea6c5ebb808db")
                                          .withMessage("Test \"X\" Message");

        assertTrue(repo.addComment(TICKET_ID, "ap-southeast-2", commit));
    }

    @Test
    @Ignore
    public void updateStatus() {
        assertTrue(repo.updateStatus(TICKET_ID, "Progress"));
    }
}