package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TasksManager manager;

    @BeforeEach
    void createManager() {
        manager = Managers.getDefault();
    }

    @Test
    //проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
    //проверьте, что объект Subtask нельзя сделать своим же эпиком
    public void shouldNotEpicContainItselfAsSubtask() {
        int idEpicAndSubtask = 100;

        Epic epic1 = new Epic("epic 1", "epic 1 dsc", idEpicAndSubtask);
        manager.addNewEpic(epic1);

        // создаем Subtask где id Epic совпадет с id Subtask
        Subtask subtask = new Subtask("name subtask1", "descr Subtask 1", idEpicAndSubtask, idEpicAndSubtask);
        manager.addNewSubtask(subtask);

        assertFalse(epic1.getId() == subtask.getId() , "Эпик не должен содержать самого себя как подзадачу.");

    }

    @Test
    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    public void shouldInMemoryTaskManagerAddTasksDifferentCanFindThemById(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        assertNotNull(manager.getTask(1), "Нет задачи по заданному id");
        assertNotNull(manager.getTask(2), "Нет задачи по заданному id");

        Epic epic1 = new Epic("Эпик 1", "Эпик 1 Описание");
        manager.addNewEpic(epic1);
        assertNotNull(manager.getEpic(3), "Нет Эпика по заданному id");

        Subtask subtask1ForEpic1 = new Subtask("Подзадача 1", "Подзадача 1 для Epic 1", epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("Подзадача 2", "Подзадача 2 для Epic 1", epic1.getId());
        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        assertNotNull(manager.getSubtask(4), "Нет подзадачи по заданному id");
        assertNotNull(manager.getSubtask(5), "Нет подзадачи по заданному id");

        // Нет задач по несуществующим id
        assertNull(manager.getTask(-1), "Не должно существовать задачи по заданному id" );
        assertNull(manager.getEpic(-1), "Не должно существовать эпика по заданному id" );
        assertNull(manager.getSubtask(-1), "Не должно существовать подзадачи по заданному id" );
    }

    @Test
    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    public void shouldNotConflictTasksWithSpecifiedIdGeneratedId(){
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2", 1, Status.NEW);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        assertEquals(2, manager.getTasks().size(), "Количество задач должно быть 2");
    }

    @Test
    void immutabilityTaskAcrossAllFieldsAfterAddingInManager(){
        Task task = new Task("Задача", "Описание задачи", 1, Status.NEW);
        manager.addNewTask(task);
        Task taskFromManager = manager.getTask(1);

        assertEquals(task.getName(), taskFromManager.getName(), "Имена задач должны совпадать");
        assertEquals(task.getDescription(), taskFromManager.getDescription(), "Описание задач должны совпадать");
        assertEquals(task.getStatus(), taskFromManager.getStatus(), "Статусы задач должны совпадать");
    }

    //Внутри эпиков не должно оставаться неактуальных id подзадач.
    @Test
    void shouldInEpicNoIdNotActualSubtasks(){
        Epic epic1 = new Epic("Эпик 1", "Эпик 1 Описание");
        manager.addNewEpic(epic1);
        Subtask subtask1ForEpic1 = new Subtask("Подзадача 1", "Подзадача 1 для Epic 1", epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("Подзадача 2", "Подзадача 2 для Epic 1", epic1.getId());
        Subtask subtask3ForEpic1 = new Subtask("Подзадача 3", "Подзадача 3 для Epic 1", epic1.getId());
        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        manager.addNewSubtask(subtask3ForEpic1);

        int idSubtask1ForEpic1 = subtask1ForEpic1.getId();
        int idSubtask2ForEpic1 = subtask2ForEpic1.getId();

        manager.deleteSubtask(idSubtask1ForEpic1);
        manager.deleteSubtask(idSubtask2ForEpic1);
        // Список id подзадач эпика после удаления 2-х подзадач
        ArrayList<Integer>  arrayListIdsSubtasks =  manager.getEpic(epic1.getId()).getSubtaskIds();
        assertTrue(arrayListIdsSubtasks.contains(subtask3ForEpic1.getId()), "Подзадача 3 должна быть в списке подзадач эпика.");
        assertFalse(arrayListIdsSubtasks.contains(subtask1ForEpic1.getId()), "Подзадачи 1 не должно быть в списке подзадач эпика.");
        assertFalse(arrayListIdsSubtasks.contains(subtask2ForEpic1.getId()), "Подзадачи 2 не должно быть в списке подзадач эпика.");
    }
}