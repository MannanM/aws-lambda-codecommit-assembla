package com.mannanlive.domain;

public class TicketAction {
    private String action;
    private String ticketId;

    public TicketAction(final String action, final String ticketId) {
        this.action = action;
        this.ticketId = ticketId;
    }

    public String getAction() {
        return action;
    }

    public String getTicketId() {
        return ticketId;
    }
}
