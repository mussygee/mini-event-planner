package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Venue;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SeatingPlannerTest {

    @Test
    void generateSeating_returnsEmptyMapWhenNoGuests() {
        Venue v = new Venue("Test", 100, 2, 4, 3);
        SeatingPlanner planner = new SeatingPlanner(v);

        Map<Integer, List<Guest>> seating = planner.generateSeating(List.of());

        assertTrue(seating.isEmpty());
    }

    @Test
    void generateSeating_seatsAllGuestsWithoutExceedingTableSize() {
        Venue v = new Venue("Test", 100, 5, 3, 2); // 3 tables, size 2 => max 6 seats
        SeatingPlanner planner = new SeatingPlanner(v);

        List<Guest> guests = List.of(
                new Guest("A", "g1"),
                new Guest("B", "g1"),
                new Guest("C", "g2"),
                new Guest("D", "g2"),
                new Guest("E", "g3")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        // all guests included exactly once
        Set<String> seatedNames = new HashSet<>();
        int total = 0;

        for (Map.Entry<Integer, List<Guest>> e : seating.entrySet()) {
            assertTrue(e.getKey() >= 1);
            assertTrue(e.getValue().size() <= v.getSeatsPerTable());
            for (Guest g : e.getValue()) {
                seatedNames.add(g.getName());
                total++;
            }
        }

        assertEquals(guests.size(), total);
        assertEquals(guests.size(), seatedNames.size());
    }

    @Test
    void generateSeating_tableNumbersAreSortedStartingAtOne() {
        Venue v = new Venue("Test", 100, 5, 2, 3);
        SeatingPlanner planner = new SeatingPlanner(v);

        List<Guest> guests = List.of(
                new Guest("A", "g1"),
                new Guest("B", "g1"),
                new Guest("C", "g1")
        );

        Map<Integer, List<Guest>> seating = planner.generateSeating(guests);

        List<Integer> keys = new ArrayList<>(seating.keySet());
        List<Integer> sorted = new ArrayList<>(keys);
        Collections.sort(sorted);

        assertEquals(sorted, keys);
        assertEquals(1, sorted.getFirst());
    }
}
