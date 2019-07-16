package com.mannanlive.domain;

public class TicketAction {
    private String action;
    private Long ticketId;

    public TicketAction(String action, Long ticketId) {
        this.action = action;
        this.ticketId = ticketId;
    }

    public String getAction() {
        return action;
    }

    public Long getTicketId() {
        return ticketId;
    }
}
