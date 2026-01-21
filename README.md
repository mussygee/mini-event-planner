# Event Planner Mini System (Java)

A Java-based event planning system that demonstrates practical use of core data structures and algorithms to manage guests, venues, seating arrangements, and event tasks. The project emphasizes clean design, efficiency analysis, and real-world problem modeling. 

## Project Overview
This project simulates key components of an event planning workflow:
- Managing and searching guest lists efficiently
- Selecting optimal venues based on constraints
- Assigning guests to tables using grouping and ordering logic
- Tracking preparation tasks with undo functionality
The system is designed to showcase data-structure selection tradeoffs, algorithmic complexity, and modular object-oriented design in Java. 

## Data Structures & Design Decisions

### GuestListManager
Purpose: Manage invited guests with fast lookup and consistent state.

**Data Structures Used:**
- **LinkedList<Guest>** - maintains the authoritative list of guests
- **HashMap<String, Guest>** - enables fast lookup by guest name

**Why this design?**
- The linked list preserves insertion order and serves as a single source of truth.
- The hash map enables constant-time average lookup without scanning the list.

**Time Complexity**
- Finding a guest: **O(1)** average
- Adding a guest: **O(1)** average
- Removing a guest: **O(n)** (linked list removal requires traversal)

### VenueSelector
Purpose: Choose the best venue that fits budget and capacity constraints.
**Approach:**
1. Filter venues that satisfy:
   - cost <= budget
   - capacity >= guest count
2. Sort remaining venues by:
   - Lowest cost
   - Smallest sufficient capacity (tie-breaker)

**Algorithms**
- Filtering
- Comparator-based sorting (Java built-in sort)

**Time Complexity**
- Filtering: O(n)
- Sorting valid venues: O(m log m)
  (m = number of valid venues)

### SeatingPlanner
Purpose: Assign guests to tables while preserving group order and table order. 

**Data Structures Used:**
- **HashMap<String, Queue<Guest>> groups** - group guests by `groupTag` with FIFO ordering
- **Queue (LinkedList as Queue)** - ensures fair seating within groups
- **Custom Binary Search Tree (TableNode)** - stores tables by table number

**Why this design?**
- Queues preserve arrival within each group.
- The BST enables sorting output of tables without aditional sorting. 

**Time Complexity**
- Grouping guests: **O(n)**
- Seating guests: **O(n)**
- Table insertin (BST): **O(t log t)** average
- Overall: **O(n + t log t)** average

### TaskManager
Purpose: Track event preparations tasks with undo capability. 

**Data Structures Used:**
- **Queue<Task> upcoming (FIFO)** - upcoming tasks (FIFO execution)
- **Stack<Task> completed (LIFO undo)** - completed tasks (LIFO undo)

**Why this design?** 
- Queues naturally model task execution order.
- Stacks efficiently support undoing the most recent action.

**Time Complexity**
- Add task: **O(1)**
- Execute task: **O(1)**
- Undo task: **O(1)**


## Technologies Used
- Java
- Object-Oriented Design
- Java Collections Framework
- Custom Data Structures
- Algorithmic Complexity Analysis

## Notes
This project was originally developed as part of an academic data structures curriculum and later refactored and documented for portfolio presentation.

## Why This Project Matters
- This project demonstrates:
- Strong understanding of data structures & algorithms
- Ability to justify design decisions
- Clean separation of concerns
- Real-world problem modeling in Java

