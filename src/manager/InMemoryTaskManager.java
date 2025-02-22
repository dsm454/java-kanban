package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TasksManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();       // id task    - task
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>(); // id subtask - subtask
    private final HashMap<Integer, Epic> epics = new HashMap<>();       // id epic    - epic

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //	a. Получение списка всех задач.
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // b. Удаление всех задач.
    @Override
    public void deleteAllTask() {
        if (tasks.isEmpty()) return;
        for (Integer idTask : tasks.keySet()) {
            historyManager.removeTask(idTask);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtask() {
        if (subtasks.isEmpty()) return;
        for (Integer idSubtask : subtasks.keySet()) {
            historyManager.removeTask(idSubtask);
        }
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        if (epics.isEmpty()) return;
        deleteAllSubtask();
        for (Integer idEpic : epics.keySet()) {
            deleteEpic(idEpic);
            historyManager.removeTask(idEpic);
        }
        epics.clear();
    }

    //----------------------------
    // c. Получение по идентификатору.
    @Override
    public Task getTask(int id) {
        final Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        final Subtask subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    private Subtask getSubtaskWithoutHistory(int id) {
        final Subtask subtask = subtasks.get(id);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    private Epic getEpicWithoutHistory(int id) {
        final Epic epic = epics.get(id);
        return epic;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        // 1) Проверить существование Epic
        Epic epic = getEpicWithoutHistory(subtask.getEpicId());
        if (epic == null) {
            return -1;
        } else {
            final int id = ++generatorId;
            // 2) добавляем
            subtask.setId(id);
            epic.addSubtaskId(id);     // 1. добавляем в список у эпика id новой подзадачи
            subtasks.put(id, subtask); // 2. добавляем в HasMap subtasks новой подзадачи
            updateEpicStatus(epic);
            return id;
        }
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            epics.get(newEpic.getId()).setName(newEpic.getName());
            epics.get(newEpic.getId()).setDescription(newEpic.getDescription());
        }
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteTask(int idTask) {
        if (tasks.containsKey(idTask)) {
            tasks.remove(idTask);
            historyManager.removeTask(idTask);
        }
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        // 1) удалить id в епике из subtaskIds 2) удалить сабтаск
        // if(subtasks.isEmpty()) return;
        if (subtasks.containsKey(idSubtask)) {
            Subtask subtask = subtasks.get(idSubtask);
            Epic epic = epics.get(subtasks.get(idSubtask).getEpicId());
            epic.removeIdSubtask(idSubtask);
            subtasks.remove(idSubtask); // Ошибка при удалении последней подзадачи в эпике
            historyManager.removeTask(idSubtask);
            updateEpicStatus(epic);
            // Удаляемые подзадачи не должны хранить внутри себя старые id.
            subtask.setId(-1);
        }
    }

    @Override
    public void deleteEpic(int idEpic) {
        if (epics.containsKey(idEpic)) {
            HashMap<Integer, Subtask> copy = new HashMap<>(subtasks);
            for (Subtask subtask : copy.values()) {
                if (subtask.getEpicId() == idEpic) {
                    deleteSubtask(subtask.getId());
                    historyManager.removeTask(subtask.getId());
                }
            }
            epics.remove(idEpic);
            historyManager.removeTask(idEpic);
        }
    }

    // *** a. Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<Subtask> getEpicSubtasks(int idEpic) {
        ArrayList<Subtask> arrayListSubtasks = new ArrayList<>();
        Epic epic = epics.get(idEpic);
        if (epic == null) {
            return null;
        } else {
            for (int idSubtask : epic.getSubtaskIds()) {
                arrayListSubtasks.add(subtasks.get(idSubtask));
            }
            return arrayListSubtasks;
        }
    }

    //-------------------------------------------------------
//@Override
    public void updateEpicStatus(Epic epic) {
        Set<Status> statusesSubtasks = new HashSet<>();
        for (Integer id : epic.getSubtaskIds()) {
            statusesSubtasks.add(getSubtaskWithoutHistory(id).getStatus());
        }

        if (epic.getSubtaskIds().isEmpty()
                || (statusesSubtasks.size() == 1 && statusesSubtasks.contains(Status.NEW))) {
            /*если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.*/
            epic.setStatus(Status.NEW);
            return;
        } else if (statusesSubtasks.size() == 1 && statusesSubtasks.contains(Status.DONE)) {
            /*если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.*/
            epic.setStatus(Status.DONE);
            return;
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
