package edu.course.eventplanner.service;

import edu.course.eventplanner.model.*;
import java.util.*;

public class SeatingPlanner {
    private final Venue venue;

    private static class TableNode {
        int tableNumber;
        List<Guest> guests;
        TableNode left;
        TableNode right;

        TableNode(int tableNumber, List<Guest> guests) {
            this.tableNumber = tableNumber;
            this.guests = guests;
        }
    }

    private TableNode insert(TableNode root, int tableNumber, List<Guest> guests) {
        if (root == null) {
            return new TableNode(tableNumber, guests);
        }
        if (tableNumber < root.tableNumber) {
            root.left = insert(root.left, tableNumber, guests);
        } else if (tableNumber > root.tableNumber) {
            root.right = insert(root.right, tableNumber, guests);
        } else {
            root.guests = guests; // replace if same tableNumber
        }
        return root;
    }

    private void inorder(TableNode root, Map<Integer, List<Guest>> seating) {
        if (root == null) return;
        inorder(root.left, seating);
        seating.put(root.tableNumber, root.guests);
        inorder(root.right, seating);
    }


    public SeatingPlanner(Venue venue) {
        this.venue = venue;
    }

    public Map<Integer, List<Guest>> generateSeating(List<Guest> guests) {
        // Always return a non-null map
        Map<Integer, List<Guest>> seating = new LinkedHashMap<>();
        if (guests == null || guests.isEmpty()) {
            return seating;
        }

        // 1) Group guests by tag into queues (FIFO)
        Map<String, Queue<Guest>> groups = new HashMap<>();
        for (Guest g : guests) {
            String tag = g.getGroupTag();
            groups.putIfAbsent(tag, new LinkedList<>());
            groups.get(tag).add(g);
        }

        // Make group iteration deterministic (HashMap order is not)
        List<String> tags = new ArrayList<>(groups.keySet());
        Collections.sort(tags);

        // 2) Fill tables one at a time
        int tableNum = 1;
        int maxTables = venue.getTables();
        int seatsPerTable = venue.getSeatsPerTable();

        TableNode root = null;

        String currentTag = null;

        while (tableNum <= maxTables) {
            List<Guest> table = new ArrayList<>();

            while (table.size() < seatsPerTable) {
                // Keep seating the same group if it still has people
                if (currentTag == null || groups.get(currentTag).isEmpty()) {
                    currentTag = null;
                    for (String t : tags) {
                        if (!groups.get(t).isEmpty()) {
                            currentTag = t;
                            break;
                        }
                    }
                }

                // No one left to seat
                if (currentTag == null) {
                    break;
                }

                table.add(groups.get(currentTag).poll());
            }

            // If we didn't seat anyone at this table, we're done
            if (table.isEmpty()) {
                break;
            }

            root = insert(root, tableNum, table);
            tableNum++;
        }

        inorder(root, seating);
        return seating;
    }

}
