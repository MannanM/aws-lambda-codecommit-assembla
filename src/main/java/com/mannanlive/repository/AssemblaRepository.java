package com.mannanlive.repository;

import com.amazonaws.services.codecommit.model.Commit;
import com.mannanlive.domain.AssemblaComment;
import com.mannanlive.domain.AssemblaStatus;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class AssemblaRepository {
    private final JerseyWebTarget TARGET = JerseyClientBuilder.createClient().target("https://api.assembla.com");
    private final String space;
    private final String apiKey;
    private final String apiSecret;

    public AssemblaRepository() {
        space = System.getProperty("SPACE");
        apiKey = System.getProperty("API_KEY");
        apiSecret = System.getProperty("API_SECRET");
    }

    public AssemblaRepository(final String space, final String apiKey, final String apiSecret) {
        this.space = space;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public boolean addComment(Long id, final String region, Commit commit) {
        final String message = String.format("%s has [[url:https://%s.console.aws.amazon.com/codesuite/codecommit/" +
                        "repositories/roster-portal/commit/%s?region=%s|committed a change]] related to this ticket: " +
                        "<pre><code>%s</code></pre>",
                commit.getAuthor().getName(), region, commit.getCommitId(), region, commit.getMessage());

        try (Response response = TARGET
                .path("/v1/spaces/")
                .path(space)
                .path("/tickets/")
                .path(id.toString())
                .path("ticket_comments.xml")
                .request()
                .header("X-Api-Key", apiKey)
                .header("X-Api-Secret", apiSecret)
                .post(Entity.json(new AssemblaComment(message)))) {
            return successOrFailure(response);
        }
    }

    public boolean updateStatus(Long ticketId, String status) {
        if (status != null) {
            try (Response response = TARGET
                    .path("/v1/spaces/")
                    .path(space)
                    .path("/tickets")
                    .path(ticketId.toString() + ".xml")
                    .request()
                    .header("X-Api-Key", apiKey)
                    .header("X-Api-Secret", apiSecret)
                    .put(Entity.json(new AssemblaStatus(status)))) {
                return successOrFailure(response);
            }
        }
        return false;
    }

    private boolean successOrFailure(Response response) {
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return true;
        } else {
            System.out.println(String.format("Received response code %d with body %s",
                    response.getStatus(), response.readEntity(String.class)));
            return false;
        }
    }
}
