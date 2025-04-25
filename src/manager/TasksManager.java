package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TasksManager {
    //	a. Получение списка всех задач.
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    // b. Удаление всех задач.
    void deleteAllTask();

    void deleteAllSubtask();

    void deleteAllEpic();

    // c. Получение по идентификатору.
    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    int addNewTask(Task task);

    Integer addNewSubtask(Subtask subtask);

    int addNewEpic(Epic epic);

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic newEpic);

    // f. Удаление по идентификатору.
    void deleteTask(int idTask);

    void deleteSubtask(int idSubtask);

    void deleteEpic(int idEpic);

    // *** a. Получение списка всех подзадач определённого эпика.
    List<Subtask> getEpicSubtasks(int idEpic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
