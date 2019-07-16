package com.mannanlive.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mannanlive.domain.AssemblaComment;
import com.mannanlive.domain.AssemblaStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AssemblaRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String URL = "https://api.assembla.com/v1/spaces/%s/tickets/%d";
    private static final CloseableHttpClient client = HttpClients.createDefault();

    private final String space;
    private final String apiKey;
    private final String apiSecret;

    public AssemblaRepository() {
        space = System.getenv("SPACE");
        apiKey = System.getenv("API_KEY");
        apiSecret = System.getenv("API_SECRET");
        System.out.println("Assembla WorkSpace: " + space);
    }

    public AssemblaRepository(final String space, final String apiKey, final String apiSecret) {
        this.space = space;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public boolean addComment(final long ticketId, final String comment) {
        return sendRequest(new HttpPost(String.format(URL + "/ticket_comments.json", space, ticketId)),
                new AssemblaComment(comment));
    }

    public boolean updateStatus(final long ticketId, final String status) {
        if (status == null) {
            return false;
        } else {
            return sendRequest(new HttpPut(String.format(URL + ".json", space, ticketId)), new AssemblaStatus(status));
        }
    }

    private boolean sendRequest(final HttpEntityEnclosingRequestBase request, final Object payload) {
        try {
            request.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(payload)));
            request.setHeader("Content-type", "application/json");
            request.setHeader("X-Api-Key", apiKey);
            request.setHeader("X-Api-Secret", apiSecret);

            try (CloseableHttpResponse response = client.execute(request)) {
                final int statusCode = response.getStatusLine().getStatusCode();
                return statusCode >= 200 && statusCode < 300;
            }
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
