# Event Planner Mini

This project demonstrates practical use of data structures:
linked lists, stacks, queues, maps, trees, sorting, and searching.

## What You Must Do
- Implement all TODO methods
- Write JUnit 5 tests for core logic
- Pass instructor autograding tests
- Explain your design choices in this README

See Canvas assignment for full requirements.

## Data Structures & Algorithms Used

### GuestListManager
- **LinkedList<Guest> (master guest list / source of truth):**
  Stores all invited guests. This is the authoritative list used to derive other views (like seating).
- **HashMap<String, Guest> (fast lookup):**
  Maps guest name → Guest for quick `findGuest`, and to keep add/remove consistent.

**Complexity**
- Finding a guest: **O(1)** average (HashMap lookup), **O(n)** worst-case in pathological hashing.
- Adding a guest: **O(1)** average (append to LinkedList + put in HashMap).
- Removing a guest: **O(n)** because removing from LinkedList by object requires a scan (map removal is O(1) average).

### VenueSelector
- **Filtering + Sorting (Comparator):**
  We filter venues that fit `(cost <= budget && capacity >= guestCount)` and then sort by:
    1) lowest cost
    2) if tie, smallest capacity that still fits

**Algorithms**
- Sorting: Java's built-in sort (comparison-based).

**Complexity**
- Selecting a venue: **O(m log m)** where m = number of valid venues (filter O(n), sort O(m log m)).

### SeatingPlanner
- **HashMap<String, Queue<Guest>> groups:**
  Group guests by `groupTag` and keep FIFO ordering inside each group.
- **Queue (LinkedList as Queue):**
  Ensures fair seating order within each group.
- **Binary Search Tree (custom TableNode BST):**
  Stores tables keyed by table number; inorder traversal outputs tables in ascending order.

**Complexity**
- Grouping guests: **O(n)**
- Seating guests into tables: **O(n)**
- BST inserts: **O(t log t)** average, where t = number of tables used (worst-case O(t^2) if unbalanced)
- Generating seating overall: **O(n + t log t)** average

### TaskManager
- **Queue<Task> upcoming (FIFO):**
  Upcoming preparation tasks executed in order.
- **Stack<Task> completed (LIFO undo):**
  Completed tasks stored so the last executed task can be undone.

**Complexity**
- Add task: **O(1)**
- Execute next task: **O(1)**
- Undo last task: **O(1)**


