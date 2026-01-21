package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Venue;
import java.util.*;

public class VenueSelector {
    private final List<Venue> venues;
    public VenueSelector(List<Venue> venues) { this.venues = venues; }
    public Venue selectVenue(double budget, int guestCount) {
        List<Venue> valid = new ArrayList<>();

        for (Venue v : venues) {
            if (v.getCost() <= budget && v.getCapacity() >= guestCount) {
                valid.add(v);
            }
        }

        if (valid.isEmpty()) {
            return null;
        }

        valid.sort((a,b) -> {
            int costCompare = Double.compare(a.getCost(), b.getCost());
            if (costCompare != 0) return costCompare;
            return Integer.compare(a.getCapacity(), b.getCapacity());
        });

        return valid.get(0);
    }
}
