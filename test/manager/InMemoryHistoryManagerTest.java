package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TasksManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void createManagerAndTasks() {
        manager = Managers.getDefault();
        task1 = new Task("Задача 1", "Описание задачи 1");
        task2 = new Task("Задача 2", "Описание задачи 2");
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        epic1 = new Epic("Эпик 1", "Описане эпика 1");
        manager.addNewEpic(epic1);
        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
    }

    @Test
    void testAddGetHistory() {

        assertTrue(manager.getHistory().isEmpty(), "До вызова getTask() история просмотров должна быть пустой");

        manager.getTask(task1.getId());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getSubtask(subtask2.getId());

        assertEquals(5, manager.getHistory().size(), "История просмотров не должна" +
                " содержать дублей просмотров (вызовов getTask() )");

        assertEquals("Подзадача 2", manager.getHistory().get(0).getName(),
                "Порядок задач в истории должен соответствовать обратному порядку просмотров");

        assertEquals("Эпик 1", manager.getHistory().get(1).getName(),
                "Порядок задач в истории должен соответствовать обратному порядку просмотров");

        assertEquals("Подзадача 1", manager.getHistory().get(2).getName(),
                "Порядок задач в истории должен соответствовать обратному порядку просмотров");

        assertEquals("Задача 2", manager.getHistory().get(3).getName(),
                "Порядок задач в истории должен соответствовать обратному порядку просмотров");

        assertEquals("Задача 1", manager.getHistory().get(4).getName(),
                "Порядок задач в истории должен соответствовать обратному порядку просмотров");
    }

    @Test
    void shouldClearHistoryAfterDeleteAllTasks() {
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getSubtask(subtask1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask2.getId());

        manager.deleteAllTask();
        manager.deleteAllEpic();
        manager.deleteAllSubtask();

        assertTrue(manager.getHistory().isEmpty(), "История после удаления всех задач должна быть пуста.");
    }

    @Test
    void shouldDeleteAndAddTasksCorrectHistory() {
        manager.getTask(task1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());
        manager.getEpic(epic1.getId());
        assertEquals(4, manager.getHistory().size(), "Количество просмотров должно быть равно 4.");
        manager.deleteTask(task1.getId());
        assertEquals(3, manager.getHistory().size(), "Количество просмотров должно быть равно 3.");
        manager.deleteSubtask(subtask1.getId());
        assertEquals(2, manager.getHistory().size(), "Количество просмотров должно быть равно 2.");
        manager.deleteEpic(epic1.getId()); // после удаления эпика удаляются все его подзадачи
        assertEquals(0, manager.getHistory().size(), "Количество просмотров должно быть равно 0.");
    }
}
