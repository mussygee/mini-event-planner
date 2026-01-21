package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Guest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GuestListManagerTest {
    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(Object obj, String fieldName, Class<T> type) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (Exception e) {
            fail("could not access field '" + fieldName + "': " + e.getMessage());
            return null;
        }
    }
    @Test
    void addGuest_addsToInternalListAndMap() {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest("Chaya", "family");
        manager.addGuest(g);
        LinkedList<Guest> guests = getPrivateField(manager, "guests", LinkedList.class);
        Map<String, Guest> byName = getPrivateField(manager, "guestByName", Map.class);
        assertEquals(1, guests.size());
        assertSame(g, guests.getFirst());
        assertEquals(1, byName.size());
        assertSame(g, byName.get("chaya"));
    }
    @Test
    void addGuest_duplicateNameDoesNotChangeStructures() {
        GuestListManager manager = new GuestListManager();
        Guest g1 = new Guest("Ari", "friends");
        Guest g2 = new Guest("Ari", "work");

        manager.addGuest(g1);
        manager.addGuest(g2);

        LinkedList<Guest> guests = getPrivateField(manager, "guests", LinkedList.class);
        Map<String, Guest> byName = getPrivateField(manager, "guestByName", Map.class);

        assertEquals(1, guests.size());
        assertSame(g1, guests.getFirst());
        assertEquals(1, byName.size());
        assertSame(g2, byName.get("friends"));
    }

    @Test
    void removeGuest_removesFromInternalListAndMap() {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest("Noa", "friends");
        manager.addGuest(g);

        boolean removed = manager.removeGuest("Noa");

        LinkedList<Guest> guests = getPrivateField(manager, "guests", LinkedList.class);
        Map<String, Guest> byName = getPrivateField(manager, "guestByName", Map.class);

        assertTrue(removed);
        assertTrue(guests.isEmpty());
        assertFalse(byName.containsKey("Noa"));
    }

    @Test
    void removeGuest_missingNameDoesNotChangeStructures() {
        GuestListManager manager = new GuestListManager();
        Guest g = new Guest("Leah", "family");
        manager.addGuest(g);

        boolean removed = manager.removeGuest("NotAName");

        LinkedList<Guest> guests = getPrivateField(manager, "guests", LinkedList.class);
        Map<String, Guest> byName = getPrivateField(manager, "guestByName", Map.class);

        assertFalse(removed);
        assertEquals(1, guests.size());
        assertEquals(1, byName.size());
    }
}
