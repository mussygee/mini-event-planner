package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Guest;

import java.util.*;

public class GuestListManager {
    private final LinkedList<Guest> guests = new LinkedList<>();
    private final Map<String, Guest> guestByName = new HashMap<>();

    public void addGuest(Guest guest) {
        if (guest == null || guest.getName() == null) return;

        String name = guest.getName().trim();
        if (name.isEmpty()) return;

        String nameKey = name.toLowerCase();

        if (guestByName.containsKey(nameKey)) {
            Guest existing = guestByName.get(nameKey);

            guestByName.remove(nameKey);

            if (existing != null && existing.getGroupTag() != null) {
                String existingGroup = existing.getGroupTag().trim();
                if (!existingGroup.isEmpty()) {
                    guestByName.put(existingGroup.toLowerCase(), guest);
                }
            }
            return;
        }

        guests.add(guest);
        guestByName.put(nameKey, guest);

    }


    public boolean removeGuest(String guestName) {
        if (guestName == null) {
            return false;
        }

        String name = guestName.trim();
        if (name.isEmpty()) {
            return false;
        }
        String nameKey = name.toLowerCase();

        Guest removed = guestByName.remove(nameKey);
        if (removed == null) {
            return false;
        }

        guests.remove(removed);

        if (removed.getGroupTag() != null) {
            String group = removed.getGroupTag().trim();
            if (!group.isEmpty()) {
                String groupKey = group.toLowerCase();
                if (guestByName.get(groupKey) == removed) {
                    guestByName.remove(groupKey);
                }
            }
        }
        return true;
    }

    public Guest findGuest(String guestName) {
        if (guestName == null) {
            return null;
        }

        String name = guestName.trim();
        if (name.isEmpty()) {
            return null;
        }
        String key = name.toLowerCase();
        return guestByName.get(key);
    }


    public int getGuestCount() {
        return guests.size();
    }

    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }
}
