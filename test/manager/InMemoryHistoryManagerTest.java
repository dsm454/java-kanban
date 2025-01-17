package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TasksManager manager;

    @BeforeEach
    void createManager() {
        manager = Managers.getDefault();
    }

    @Test
    void testAddGetHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        assertTrue(manager.getHistory().isEmpty(), "До вызова getTask() история просмотров должна быть пустой");

        manager.getTask(1);
        manager.getTask(2);

        assertEquals(2,manager.getHistory().size(), "История просмотров должна" +
                " соответствовать кол-ву вызовов getTask()");
    }
}