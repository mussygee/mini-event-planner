package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    @Test
    void addTask_ignoresNull() {
        TaskManager manager = new TaskManager();
        manager.addTask(null);

        assertEquals(0, manager.remainingTaskCount());
        assertTrue(manager.getUpcomingTasks().isEmpty());
    }

    @Test
    void executeNextTask_returnsNullWhenNoTasks() {
        TaskManager manager = new TaskManager();

        assertNull(manager.executeNextTask());
    }

    @Test
    void executeNextTask_isFifo() {
        TaskManager manager = new TaskManager();
        Task t1 = new Task("one");
        Task t2 = new Task("two");

        manager.addTask(t1);
        manager.addTask(t2);

        assertSame(t1, manager.executeNextTask());
        assertSame(t2, manager.executeNextTask());
    }

    @Test
    void undoLastTask_returnsNullWhenNothingExecuted() {
        TaskManager manager = new TaskManager();
        assertNull(manager.undoLastTask());
    }

    @Test
    void undoLastTask_restoresTaskAsNextToExecute() {
        TaskManager manager = new TaskManager();
        Task t1 = new Task("first");
        Task t2 = new Task("second");

        manager.addTask(t1);
        manager.addTask(t2);

        // execute one task
        Task executed = manager.executeNextTask();
        assertSame(t1, executed);

        // undo should make t1 next again
        Task undone = manager.undoLastTask();
        assertSame(t1, undone);

        // now next execute should return t1 again (this is what workflow tests usually expect)
        Task again = manager.executeNextTask();
        assertSame(t1, again);

        // remaining should be 1 (t2)
        List<Task> upcoming = manager.getUpcomingTasks();
        assertEquals(1, upcoming.size());
        assertEquals("second", upcoming.getFirst().getDescription());
    }
}
