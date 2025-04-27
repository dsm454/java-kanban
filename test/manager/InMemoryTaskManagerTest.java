package manager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    // TasksManager manager;

    @BeforeEach
    public void setUp() {
        manager = (InMemoryTaskManager) Managers.getDefault();

        task1 = new Task("Test task 1", "Task description 1", Duration.ofMinutes(5)
                , LocalDateTime.now(), 1, Status.IN_PROGRESS);
        task2 = new Task("Test task 2", "Task description 2", Duration.ofMinutes(5)
                , LocalDateTime.now().plusMinutes(10), 2, Status.IN_PROGRESS);
        manager.addNewTask(task1);
        manager.addNewTask(task2);

        epic1 = new Epic("Epic Test 1", "Epic Description 1");
        epic2 = new Epic("Epic Test 2", "Epic Description 2");
        manager.addNewEpic(epic1);
        manager.addNewEpic(epic2);

        subtask1 = new Subtask("Subtask Test 1-1", "Subtask Description 1"
                , Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(45), -1, Status.NEW, epic1.getId());
        subtask2 = new Subtask("Subtask Test 1-2", "Subtask Description 2"
                , Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(60), -1, Status.NEW, epic1.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);

    }

}
