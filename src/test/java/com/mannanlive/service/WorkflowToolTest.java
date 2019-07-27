package com.mannanlive.service;

import com.amazonaws.services.codecommit.model.Commit;
import com.amazonaws.services.codecommit.model.UserInfo;
import com.mannanlive.domain.WorkflowClient;
import com.mannanlive.domain.assembla.AssemblaComment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowToolTest {
    private static final VerificationMode ONCE = times(1);
    private static final String TEST_SPACE = "test-space";
    private static final String REAL_CARD = "123";
    private static final String NON_EXISTENT_CARD = "456";
    private static final String ACTION = "Test";
    private static final String COMMIT_MESSAGE = ACTION + " #" + REAL_CARD + ", Not a real id #" + NON_EXISTENT_CARD;
    private static final String AUTHOR = "Bob Boberson";
    private static final String COMMIT_ID = "abcdef123456";
    private static final String REPO_NAME = "repo-name";
    private static final String REGION = "ap-southeast-2";

    @Mock
    private WorkflowClient client;
    private WorkflowTool tool;

    @Before
    public void setUp() {
        tool = new WorkflowTool(new AssemblaComment(REGION, REPO_NAME), client,
                new TicketExtractor("#", "\\d+"), TEST_SPACE);
    }

    @Test
    public void workflow_tool_process_works_as_expected() {
        Assert.assertEquals(REPO_NAME, tool.getRepositoryName());

        final String expectedComment = AUTHOR + " has [[url:https://" + REGION +
                ".console.aws.amazon.com/codesuite/codecommit/repositories/" + REPO_NAME + "/commit/" + COMMIT_ID +
                "?region=" + REGION + "|committed a change]] related to this ticket: <pre><code>" + COMMIT_MESSAGE +
                "</code></pre>";

        when(client.addComment(TEST_SPACE, REAL_CARD, expectedComment)).thenReturn(true);
        when(client.updateStatus(TEST_SPACE, REAL_CARD, ACTION)).thenReturn(true);
        when(client.addComment(TEST_SPACE, NON_EXISTENT_CARD, expectedComment)).thenReturn(false);

        tool.process(new Commit()
                .withMessage(COMMIT_MESSAGE)
                .withAuthor(new UserInfo().withName(AUTHOR))
                .withCommitId(COMMIT_ID));

        verify(client, ONCE).addComment(TEST_SPACE, REAL_CARD, expectedComment);
        verify(client, ONCE).updateStatus(TEST_SPACE, REAL_CARD, ACTION);
        verify(client, ONCE).addComment(TEST_SPACE, NON_EXISTENT_CARD, expectedComment);
        verifyNoMoreInteractions(client);
    }
}