package com.mannanlive.domain;

import java.util.HashMap;
import java.util.Map;

public class AssemblaStatus {
    private Map<String, String> ticket;

    public AssemblaStatus(String newStatus) {
        ticket = new HashMap<>();
        ticket.put("status", newStatus);
    }

    public Map<String, String> getTicket() {
        return ticket;
    }
}
