package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TasksManager {
    protected int generatorId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();       // id task    - task
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>(); // id subtask - subtask
    protected final HashMap<Integer, Epic> epics = new HashMap<>();       // id epic    - epic
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
            prioritizedTasks.remove(tasks.get(idTask));
            historyManager.removeTask(idTask);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtask() {
        if (subtasks.isEmpty()) return;
        for (Integer idSubtask : subtasks.keySet()) {
            prioritizedTasks.remove(subtasks.get(idSubtask));
            historyManager.removeTask(idSubtask);
        }
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        if (epics.isEmpty()) return;
        deleteAllSubtask();
        Set<Integer> copy = new HashSet<>(epics.keySet());
        for (Integer idEpic : copy) {
            historyManager.removeTask(idEpic);
            deleteEpic(idEpic);
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

    @Override
    public Epic getEpic(int id) {
        final Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public int addNewTask(Task task) {
        if (task.getStartTime() != null && !isTaskOverlap(task)) {
            final int id = ++generatorId;
            task.setId(id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
            return id;
        } else
            return -1;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        // 1) Проверить существование Epic
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null || subtask.getStartTime() == null || isTaskOverlap(subtask)) {
            return -1;
        } else {
            final int id = ++generatorId;
            // 2) добавляем
            subtask.setId(id);
            epic.addSubtaskId(id);     // 1. добавляем в список у эпика id новой подзадачи
            subtasks.put(id, subtask); // 2. добавляем в HasMap subtasks новой подзадачи
            updateEpicStatus(epic);
            updateEpicTime(epic);
            prioritizedTasks.add(subtask);
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
        if (tasks.containsKey(task.getId()) && task.getStartTime() != null && !isTaskOverlap(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && subtask.getStartTime() != null && !isTaskOverlap(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
            updateEpicTime(epics.get(subtask.getEpicId()));
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
//            epics.get(newEpic.getId()).setName(newEpic.getName());
//            epics.get(newEpic.getId()).setDescription(newEpic.getDescription());
            epics.put(newEpic.getId(), newEpic);
        }
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteTask(int idTask) {
        if (tasks.containsKey(idTask)) {
            prioritizedTasks.remove(tasks.get(idTask));
            tasks.remove(idTask);
            historyManager.removeTask(idTask);
        }
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        // 1) удалить id в епике из subtaskIds 2) удалить сабтаск
        if (subtasks.containsKey(idSubtask)) {
            Subtask subtask = subtasks.get(idSubtask);
            Epic epic = epics.get(subtasks.get(idSubtask).getEpicId());
            epic.removeIdSubtask(idSubtask);
            prioritizedTasks.remove(subtasks.get(idSubtask));
            subtasks.remove(idSubtask);
            historyManager.removeTask(idSubtask);
            updateEpicStatus(epic);
            updateEpicTime(epic);
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
    public List<Subtask> getEpicSubtasks(int idEpic) {
        if (epics.get(idEpic) == null) return null;
        return epics.get(idEpic).getSubtaskIds()
                .stream()
                .map(subtasks::get)
                .toList();
    }

    //-------------------------------------------------------
    private void updateEpicStatus(Epic epic) {
        Set<Status> statusesSubtasks = new HashSet<>();
        for (Integer id : epic.getSubtaskIds()) {
            statusesSubtasks.add(subtasks.get(id).getStatus());
        }
        if (epic.getSubtaskIds().isEmpty()
                || (statusesSubtasks.size() == 1 && statusesSubtasks.contains(Status.NEW))) {
            /*если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.*/
            epic.setStatus(Status.NEW);
        } else if (statusesSubtasks.size() == 1 && statusesSubtasks.contains(Status.DONE)) {
            /*если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.*/
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;
        Duration epicDuration = Duration.ZERO;
        for (Integer id : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(id);
            LocalDateTime subTaskStartTime = subtask.getStartTime();
            LocalDateTime subTaskEndTime = subtask.getEndTime();
            if (subTaskStartTime != null && (epicStartTime == null || subTaskStartTime.isBefore(epicStartTime))) {
                epicStartTime = subTaskStartTime;
            }
            if (subTaskEndTime != null && (epicEndTime == null || subTaskEndTime.isAfter(epicEndTime))) {
                epicEndTime = subTaskEndTime;
            }
            epicDuration = epicDuration.plus(subtask.getDuration());
        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isTaskOverlap(Task task) {
        // true - есть пересечение задач
        if (getPrioritizedTasks().isEmpty()) return false;
        return getPrioritizedTasks().stream().filter(chekTask -> task.getId() != chekTask.getId())
                .anyMatch(checkTask -> task.getStartTime().isBefore(checkTask.getEndTime()) &&
                        task.getEndTime().isAfter(checkTask.getStartTime()));
    }

}
