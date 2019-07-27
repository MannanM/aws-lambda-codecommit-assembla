package com.mannanlive.domain.trello;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mannanlive.domain.WorkflowClient;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class TrelloWorkflowClient implements WorkflowClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String URL = "https://api.trello.com/1/";
    private static final CloseableHttpClient client = HttpClients.createDefault();

    private final String apiKey;
    private final String apiSecret;

    public TrelloWorkflowClient() {
        apiKey = System.getenv("API_KEY");
        apiSecret = System.getenv("API_SECRET");
        System.out.println("Trello Key: " + apiKey);
    }

    public TrelloWorkflowClient(final String apiKey, final String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public boolean addComment(final String board, final String cardId, final String comment) {
        final URI url = createUrl(format("cards/%s/actions/comments", cardId), new BasicNameValuePair("text", comment));
        return sendRequest(new HttpPost(url));
    }

    public boolean updateStatus(final String board, final String cardId, final String status) {
        if (status == null) {
            return false;
        } else {
            getStatuses(board).stream()
                    .filter(boardList -> status.equalsIgnoreCase(boardList.getName()))
                    .findFirst()
                    .ifPresent(boardList -> {
                        final URI url = createUrl("cards/" + cardId,
                                new BasicNameValuePair("idList", boardList.getId()));
                        sendRequest(new HttpPut(url));
                    });
            return true;
        }
    }

    private List<TrelloBoardList> getStatuses(final String board) {
        final URI url = createUrl("boards/" + board + "/lists",
                new BasicNameValuePair("cards", "none"),
                new BasicNameValuePair("card_fields", "all"),
                new BasicNameValuePair("filter", "open"),
                new BasicNameValuePair("fields", "name,id"));

        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return OBJECT_MAPPER.readValue(response.getEntity().getContent(),
                        new TypeReference<List<TrelloBoardList>>() {});
            } else {
                System.out.printf("Received status=%d when retrieving status for board=%s", statusCode, board);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private boolean sendRequest(final HttpEntityEnclosingRequestBase request) {
        try {
            try (CloseableHttpResponse response = client.execute(request)) {
                final int statusCode = response.getStatusLine().getStatusCode();
                return statusCode >= 200 && statusCode < 300;
            }
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    private URI createUrl(String format, NameValuePair... keys) {
        try {
            return new URIBuilder(URL + format)
                    .setParameters(keys)
                    .setParameter("key", apiKey)
                    .setParameter("token", apiSecret)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Not a valid URL");
        }
    }
}
