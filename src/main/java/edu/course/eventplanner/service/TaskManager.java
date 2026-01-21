package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Task;
import java.util.*;

public class TaskManager {
    private final Queue<Task> upcoming = new LinkedList<>();
    private final Stack<Task> completed = new Stack<>();
    public void addTask(Task task) { if (task == null) {
        return;
    }
        upcoming.add(task); }
    public Task executeNextTask() { if (upcoming.isEmpty()) {
        return null;
    }

        Task task = upcoming.poll(); // FIFO
        completed.push(task);        // record for undo
        return task; }
    public Task undoLastTask() {
        if (completed.isEmpty()) {
            return null;
        }
        Task undone = completed.pop();

        if (upcoming instanceof java.util.Deque) {
            ((java.util.Deque<Task>) upcoming).addFirst(undone);
        } else {
            java.util.LinkedList<Task> tmp = new java.util.LinkedList<>();
            tmp.add(undone);
            tmp.addAll(upcoming);
            upcoming.clear();
            upcoming.addAll(tmp);
        }
        return undone;
    }

    public int remainingTaskCount() { return upcoming.size(); }

    public List<Task> getUpcomingTasks() {
        return new ArrayList<>(upcoming);
    }
}
