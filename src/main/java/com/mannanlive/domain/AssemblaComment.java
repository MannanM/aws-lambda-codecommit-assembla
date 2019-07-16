package com.mannanlive.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class AssemblaComment {
    @JsonProperty("ticket_comment")
    private Map<String, String> ticketComment;

    public AssemblaComment(String comment) {
        ticketComment = new HashMap<>();
        ticketComment.put("comment", comment);
    }

    public Map<String, String> getTicketComment() {
        return ticketComment;
    }
}
