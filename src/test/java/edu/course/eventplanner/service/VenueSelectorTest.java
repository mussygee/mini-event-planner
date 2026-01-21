package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Venue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VenueSelectorTest {

    @Test
    void selectVenue_returnsNullWhenNoVenueWithinBudget() {
        List<Venue> venues = List.of(
                new Venue("A", 1000, 10, 10, 8),
                new Venue("B", 900,  10, 10, 8)
        );

        VenueSelector selector = new VenueSelector(venues);

        assertNull(selector.selectVenue(100, 200));
    }

    @Test
    void selectVenue_picksCheapestWithinBudget() {
        List<Venue> venues = List.of(
                new Venue("Cheap", 300, 1000, 10, 8),
                new Venue("Mid", 400, 1000, 10, 8)
        );

        VenueSelector selector = new VenueSelector(venues);

        Venue chosen = selector.selectVenue(1000, 50);
        assertNotNull(chosen);
        assertEquals("Cheap", chosen.getName());
    }

    @Test
    void selectVenue_tieOnCost_picksSmallerCapacity() {
        Venue small = new Venue("Small", 500, 5, 10, 8);
        Venue large = new Venue("Large", 500, 10, 10, 8);

        VenueSelector selector = new VenueSelector(List.of(large, small));
        Venue result = selector.selectVenue(500, 5);

        assertNotNull(result);
        assertEquals("Small", result.getName());
    }
}
