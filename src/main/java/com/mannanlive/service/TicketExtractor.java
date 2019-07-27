package com.mannanlive.service;

import com.mannanlive.domain.TicketAction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketExtractor {
    private final String prefix;
    private final Pattern regex;

    public TicketExtractor(final String prefix, final String ticketRegex) {
        this.prefix = prefix;
        this.regex = Pattern.compile(prefix + ticketRegex);
    }

    public List<TicketAction> extract(final String message) {
        return extractActions(extractIds(message), message);
    }

    private List<String> extractIds(final String message) {
        final Matcher matcher = regex.matcher(message);
        final List<String> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(matcher.group().substring(1));
        }
        return results;
    }

    private List<TicketAction> extractActions(final List<String> ids, final String message) {
        final List<TicketAction> results = new ArrayList<>();

        ids.forEach(id -> {
            final Matcher matcher = Pattern.compile("(\\w+)\\s" + prefix + id).matcher(message);
            if (matcher.find()) {
                results.add(new TicketAction(matcher.group(1), id));
            } else if (results.isEmpty()) {
                results.add(new TicketAction(null, id));
            } else {
                final String lastAction = results.get(results.size() -1).getAction();
                results.add(new TicketAction(lastAction, id));
            }
        });

        return results;
    }
}
