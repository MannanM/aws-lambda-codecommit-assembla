package com.mannanlive.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketExtractor {
    private static Pattern regex = Pattern.compile("#\\d+");

    public static List<TicketAction> extract(final String message) {
        return extractActions(extractIds(message), message);
    }

    private static List<Long> extractIds(final String message) {
        final Matcher matcher = regex.matcher(message);
        final List<Long> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(Long.parseLong(matcher.group().substring(1)));
        }
        return results;
    }

    private static List<TicketAction> extractActions(final List<Long> ids, final String message) {
        final List<TicketAction> results = new ArrayList<>();

        ids.forEach(id -> {
            final Matcher matcher = Pattern.compile("(\\w+)\\s#" + id).matcher(message);
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
