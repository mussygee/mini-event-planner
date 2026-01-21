package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.service.TaskManager;
import edu.course.eventplanner.service.VenueSelector;
import edu.course.eventplanner.service.SeatingPlanner;
import edu.course.eventplanner.util.Generators;

import java.util.List;
import java.util.Scanner;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        GuestListManager guestManager = new GuestListManager();
        TaskManager taskManager = new TaskManager();

        // State for venue selection
        List<Venue> venues = List.of();
        VenueSelector venueSelector = new VenueSelector(venues);
        Venue selectedVenue = null;

        while (true) {
            System.out.println("=== Event Planner Mini ===");
            System.out.println("1. Load sample data");
            System.out.println("2. Add guest");
            System.out.println("3. Remove guest");
            System.out.println("4. Select venue");
            System.out.println("5. Generate seating chart");
            System.out.println("6. Add preparation task");
            System.out.println("7. Execute next task");
            System.out.println("8. Undo last task");
            System.out.println("9. Print event summary");
            System.out.println("10. View available venues");
            System.out.println("11. View remaining tasks");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            String choice = in.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Goodbye!");
                break;
            }

            switch (choice) {
                case "1" -> {
                    venues = Generators.generateVenues();
                    venueSelector = new VenueSelector(venues);

                    List<Guest> sampleGuests = Generators.GenerateGuests(12);
                    for (Guest g : sampleGuests) {
                        guestManager.addGuest(g);
                    }

                    taskManager.addTask(new Task("Confirm venue contract"));
                    taskManager.addTask(new Task("Finalize guest list"));

                    selectedVenue = null;

                    System.out.println("Sample data loaded!");
                    System.out.println("Venues loaded: " + venues.size());
                    System.out.println("Guests loaded: " + guestManager.getGuestCount());
                    System.out.println("Tasks loaded: " + taskManager.remainingTaskCount());
                }

                case "2" -> {
                    System.out.print("Enter guest name: ");
                    String name = in.nextLine().trim();

                    System.out.print("Enter group tag (e.g., family, friends): ");
                    String tag = in.nextLine().trim();

                    guestManager.addGuest(new Guest(name, tag));
                    System.out.println("Added guest: " + name + " (" + tag + ")");
                    System.out.println("Total guests: " + guestManager.getGuestCount());
                }

                case "3" -> {
                    System.out.print("Enter guest name to remove: ");
                    String name = in.nextLine().trim();

                    boolean removed = guestManager.removeGuest(name);
                    if (removed) {
                        System.out.println("Removed guest: " + name);
                    } else {
                        System.out.println("Guest not found: " + name);
                    }
                    System.out.println("Total guests: " + guestManager.getGuestCount());
                }

                case "4" -> {
                    if (venues.isEmpty()) {
                        System.out.println("No venues loaded. Choose option 1 first.");
                        break;
                    }
                    System.out.print("Enter your budget (number): ");
                    double budget;
                    try {
                        budget = Double.parseDouble(in.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid budget. Please enter a valid number.");
                        break;
                    }
                    int guestCount = guestManager.getGuestCount();
                    if (guestCount == 0) {
                        System.out.println("No guests yet. Chose option 2 to add guests first.");
                        break;
                    }
                    Venue chosen = venueSelector.selectVenue(budget, guestCount);

                    if (chosen == null) {
                        selectedVenue = null;
                        System.out.println("No venues fit your budget and guest count");
                    } else {
                        selectedVenue = chosen;
                        System.out.println("Selected venue: " + selectedVenue.getName());
                        System.out.println("Cost: " + selectedVenue.getCost());
                        System.out.println("Capacity: " + selectedVenue.getCapacity());
                        System.out.println("Tables: " + selectedVenue.getTables() + ", Seats per table: " + selectedVenue.getSeatsPerTable());
                    }
                }

                case "5" -> {
                    if (selectedVenue == null) {
                        System.out.println("No venue selected yet. Choose option 1 first, then option 4.");
                        break;
                    }
                    if (guestManager.getGuestCount() == 0) {
                        System.out.println("No guests to seat. Choose option 2 first to add guests");
                        break;
                    }
                    SeatingPlanner planner = new SeatingPlanner(selectedVenue);
                    Map<Integer, List<Guest>> seating = planner.generateSeating(guestManager.getAllGuests());

                    System.out.println("==== Seating Chart for " + selectedVenue.getName() + " ====");
                    System.out.println("Tables: " + selectedVenue.getTables() + " | Seats per table: " + selectedVenue.getSeatsPerTable());
                    System.out.println();

                    int totalSeated = 0;

                    for (int tableNum =1; tableNum <= selectedVenue.getTables(); tableNum++) {
                        List<Guest> tableGuests = seating.get(tableNum);
                        if (tableGuests == null || tableGuests.isEmpty()) {
                            continue;
                        }

                        System.out.println("Table " + tableNum + ":");
                        int seatNumber = 1;

                        for (Guest g : tableGuests) {
                            System.out.println(" " + seatNumber +  ". " +g.getName() + " (" + g.getGroupTag() + ")");
                            seatNumber++;
                            totalSeated++;
                        }
                        System.out.println();
                    }
                    System.out.println("Total seated: " + totalSeated + " / " + guestManager.getGuestCount());
                }

                case "6" -> {
                    System.out.print("Enter task description: ");
                    String description = in.nextLine().trim();
                    if (description.isEmpty()) {
                        System.out.println("Task description cannot be empty.");
                        break;
                    }
                    Task t = new Task(description);
                    taskManager.addTask(t);

                    System.out.println("Task added: " + description);
                    System.out.println("Upcoming tasks remaining: " + taskManager.remainingTaskCount());
                }

                case "7" -> {
                    Task done = taskManager.executeNextTask();

                    if (done == null) {
                        System.out.println("No upcoming tasks to execute.");
                    } else {
                        System.out.println("Executed task: " + done.getDescription());
                        System.out.println("Upcoming tasks remaining: " + taskManager.remainingTaskCount());
                    }
                }
                case "8" -> {
                    Task undone = taskManager.undoLastTask();
                    if (undone == null) {
                        System.out.println("Nothing to undo.");
                    } else {
                        System.out.println("Undid task: " + undone.getDescription());
                        System.out.println("Upcoming tasks remaining: " + taskManager.remainingTaskCount());
                    }
                }

                case "9" -> {
                    System.out.println("=== Event Summary ===");

                    // Guests
                    System.out.println("Guests: " + guestManager.getGuestCount());
                    if (guestManager.getGuestCount() > 0) {
                        for (Guest g : guestManager.getAllGuests()) {
                            System.out.println(" - " + g.getName() + " (" + g.getGroupTag() + ")");
                        }
                    }

                    // Venue
                    if (selectedVenue == null) {
                        System.out.println("Venue: (none selected)");
                    } else {
                        System.out.println("Venue: " + selectedVenue.getName()
                                + " | Cost: " + selectedVenue.getCost()
                                + " | Capacity: " + selectedVenue.getCapacity());
                    }

                    // Tasks
                    List<Task> tasks = taskManager.getUpcomingTasks();
                    System.out.println("Remaining tasks: " + tasks.size());
                    if (!tasks.isEmpty()) {
                        int i = 1;
                        for (Task task : tasks) {
                            System.out.println(" " + i + ". " + task.getDescription());
                            i++;
                        }
                    }

                }

                case "10" -> {
                    if (venues.isEmpty()) {
                        System.out.println("No venues loaded. Choose option 1 first. ");
                        break;
                    }

                    System.out.println(" === Available Venues ===");
                    for (Venue v : venues) {
                        System.out.println(v.getName()
                                + " | Cost: " + v.getCost()
                                + " | Capacity: " + v.getCapacity()
                                + " | Tables: " + v.getTables()
                                + " | Seats/table: " + v.getSeatsPerTable());
                    }
                }

                case "11" -> {
                    List<Task> tasks = taskManager.getUpcomingTasks();
                    if (tasks.isEmpty()) {
                        System.out.println("No remaining tasks.");
                        break;
                    }
                    System.out.println("=== Remaining Tasks ===");
                    int i = 1;
                    for (Task t : tasks) {
                        System.out.println(i + ". " + t.getDescription());
                        i++;
                    }
                }

                default -> System.out.println("Implemented so far: 1 (load sample data), 9 (print summary).");
            }


            System.out.println();
        }

        in.close();
    }
}
