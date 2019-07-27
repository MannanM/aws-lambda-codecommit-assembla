package com.mannanlive.domain.assembla;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class AssemblaTicketComment {
    @JsonProperty("ticket_comment")
    private Map<String, String> ticketComment;

    public AssemblaTicketComment(String comment) {
        ticketComment = new HashMap<>();
        ticketComment.put("comment", comment);
    }

    public Map<String, String> getTicketComment() {
        return ticketComment;
    }
}
