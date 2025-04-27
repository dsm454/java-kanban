package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TasksManager> {

    protected T manager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;


    //	a. Получение списка всех задач.
    @Test
    void shouldGetTasks() {
        List<Task> tasks = manager.getTasks();
        assertEquals(2, tasks.size(), "Должно быть два таска");
        assertTrue(tasks.contains(task1), "Список задач должен содержать task1");
        assertTrue(tasks.contains(task2), "Список задач должен содержать task2");
    }

    @Test
    void shouldGetSubtasks() {
        List<Subtask> subtasks = manager.getSubtasks();
        assertEquals(2, subtasks.size(), "Подзадачи должны быть добавлены в список");
        assertTrue(subtasks.contains(subtask1), "Список задач должен содержать subtask1");
        assertTrue(subtasks.contains(subtask2), "Список задач должен содержать subtask2");
    }

    @Test
    void shouldGetEpics() {
        List<Epic> epics = manager.getEpics();
        assertEquals(2, epics.size(), "Эпики должны быть добавлены в список");
        assertTrue(epics.contains(epic1), "Список задач должен содержать epic1");
        assertTrue(epics.contains(epic2), "Список задач должен содержать epic2");
    }


    // b. Удаление всех задач.
    @Test
    void shouldDeleteAllTask() {
        assertEquals(2, manager.getTasks().size(), "Должно быть два таска");
        manager.deleteAllTask();
        assertEquals(0, manager.getTasks().size(), "Список задач должен быть пуст");
        assertFalse(manager.getTasks().contains(task1), "Список задач не должен содержать task1");
        assertFalse(manager.getTasks().contains(task2), "Список задач не должен содержать task2");
    }

    @Test
    void shouldDeleteAllSubtask() {
        assertEquals(2, manager.getSubtasks().size(), "Должно быть две подзадачи");
        manager.deleteAllSubtask();
        assertEquals(0, manager.getSubtasks().size(), "Список задач должен быть пуст");
        assertFalse(manager.getSubtasks().contains(subtask1), "Список задач не должен содержать task1");
        assertFalse(manager.getSubtasks().contains(subtask2), "Список задач не должен содержать task2");

    }

    @Test
    void shouldDeleteAllEpic() {
        assertEquals(2, manager.getEpics().size(), "Должно быть два эпика");
        manager.deleteAllEpic();
        assertEquals(0, manager.getEpics().size(), "Список эпиков должен быть пуст");
        assertEquals(0, manager.getSubtasks().size(), "Список подзадач должен быть пуст");
        assertFalse(manager.getEpics().contains(epic1), "Список эпиков не должен содержать epic1");
        assertFalse(manager.getEpics().contains(epic2), "Список эпиков не должен содержать epic2");
    }

    // c. Получение по идентификатору.
    @Test
    void shouldGetTask() {
        int taskId = task1.getId();
        Task testTask = manager.getTask(taskId);
        assertEquals(task1, testTask, "Задачи должны быть эквивалентны");
    }

    @Test
    void shouldGetSubtask() {
        int subtaskId = subtask1.getId();
        Task testSubtask = manager.getSubtask(subtaskId);
        assertEquals(subtask1, testSubtask, "Подзадачи должны быть эквивалентны");
    }

    @Test
    void shouldGetEpic() {
        int epicId = epic1.getId();
        Task testEpic = manager.getEpic(epicId);
        assertEquals(epic1, testEpic, "Эпики должны быть эквивалентны");
    }


    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Test
    void shouldAddNewTask() {
        assertEquals(2, manager.getTasks().size(), "Список задач должен содержать 2 задачи");
        manager.addNewTask(new Task("Test", "Description", Duration.ofMinutes(5)
                , LocalDateTime.now().plusMinutes(20), 0, Status.IN_PROGRESS));
        assertEquals(3, manager.getTasks().size(), "Список задач должен содержать одну задачу");
    }

    @Test
    void shouldAddNewSubtask() {
        assertEquals(2, manager.getSubtasks().size(), "Список подзадач должен содержать 2 подзадачи");
        manager.addNewSubtask(new Subtask("Subtask Test 1-2", "Subtask Description 2"
                , Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(15), -1, Status.DONE, epic1.getId()));
        assertEquals(3, manager.getSubtasks().size(), "Список подзадач должен содержать 3 подзадачи");
    }

    @Test
    void shouldAddNewEpic() {
        assertEquals(2, manager.getEpics().size(), "Список эпиков должен содержать 2 эпика");
        manager.addNewEpic(new Epic("Epic Test 3", "Epic Description 3"));
        assertEquals(3, manager.getEpics().size(), "Список эпиков должен содержать 3 эпика");

    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Test
    void shouldUpdateTask() {
        assertEquals("Test task 1", task1.getName(), "У задачи не верное имя");
        task1.setName("New Test task 1");
        manager.updateTask(task1);
        assertEquals("New Test task 1", task1.getName(), "У задачи должно быть изменено имя");
    }

    @Test
    void shouldUpdateSubtask() {
        assertEquals("Subtask Test 1-1", subtask1.getName(), "У подзадачи не верное имя");
        subtask1.setName("New Subtask Test 1-1");
        manager.updateSubtask(subtask1);
        assertEquals("New Subtask Test 1-1", subtask1.getName(), "У подзадачи должно быть изменено имя");
    }

    @Test
    void shouldUpdateEpic() {
        assertEquals("Epic Test 1", epic1.getName(), "У эпика не верное имя");
        epic1.setName("New Epic Test 1");
        manager.updateEpic(epic1);
        assertEquals("New Epic Test 1", epic1.getName(), "У эпика должно быть изменено имя");
    }

    // f. Удаление по идентификатору.
    @Test
    void shouldDeleteTask() {
        assertEquals(2, manager.getTasks().size(), "Список задач должен содержать 2 задачи");
        manager.deleteTask(task1.getId());
        assertEquals(1, manager.getTasks().size(), "Список задач должен содержать 1 задачу");
    }

    @Test
    void shouldDeleteSubtask() {
        assertEquals(2, manager.getSubtasks().size(), "Список подзадач должен содержать 2 задачи");
        manager.deleteSubtask(subtask1.getId());
        assertEquals(1, manager.getSubtasks().size(), "Список подзадач должен содержать 1 задачу");
    }

    @Test
    void shouldDeleteEpic() {

        assertEquals(2, manager.getEpics().size(), "Список эпиков должен содержать 2 эпика");
        assertEquals(2, manager.getSubtasks().size(), "Список подзадач должен содержать 2 подзадачи");
        manager.deleteEpic(epic1.getId());
        assertEquals(1, manager.getEpics().size(), "Список эпиков должен содержать 1 эпик");
        assertEquals(0, manager.getSubtasks().size(), "Список подзадач должен содержать 1 подзадачу");
    }

    // *** a. Получение списка всех подзадач определённого эпика.
    @Test
    void shouldGetEpicSubtasks() {
        List<Subtask> subtaskArrayList = manager.getEpicSubtasks(epic1.getId());
        assertTrue(subtaskArrayList.contains(subtask1) && subtaskArrayList.contains(subtask2)
                , "Список подзадач эпика должен содержать 2 задачи");
    }

    @Test
    void shouldGetHistory() {
        assertTrue(manager.getHistory().isEmpty(), "История пуста");
        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        assertTrue(manager.getHistory().contains(task1)
                && manager.getHistory().contains(epic1)
                && manager.getHistory().contains(subtask1), "В истории должны быть добавлен задача" +
                ", эпик и подзадача");
    }

    @Test
    void testUpdateEpicStatus() {
        // удалим все задачи для чистоты теста
        manager.deleteAllTask();

        subtask1.setStatus(Status.NEW);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.NEW);
        manager.updateSubtask(subtask2);
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика должен быть NEW");

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);
        assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика должен быть DONE");

        subtask1.setStatus(Status.NEW);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS");

        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void testIsTaskOverlap() {
//        manager.deleteAllTask();
//        manager.deleteAllSubtask();
//        manager.deleteAllEpic();

        int countPrioritizedTasks = manager.getPrioritizedTasks().size();
        int countTasks = manager.getTasks().size();

        Task task0 = new Task("Test task 0", "Task description 0", Duration.ofMinutes(5)
                , task1.getStartTime().plusMinutes(1), 0, Status.NEW);
        manager.addNewTask(task0);
        assertEquals(countPrioritizedTasks, manager.getPrioritizedTasks().size(), "При пересеченни даты " +
                "старта задачи задача не добавлена");

        assertEquals(countTasks, manager.getTasks().size(), "При пересеченни даты " +
                "старта задачи задача не добавлена");

        Task task00 = new Task("Test task 00", "Task description 00", Duration.ofMinutes(5)
                , task1.getStartTime().plusMinutes(100), 0, Status.NEW);
        manager.addNewTask(task00);
        assertEquals(countPrioritizedTasks + 1, manager.getPrioritizedTasks().size(), "При не " +
                "пересеченни даты старта задача добавлена");

        assertEquals(countTasks + 1, manager.getTasks().size(), "При не пересеченни даты " +
                "старта задача добавлена");
    }

}
