package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TasksManager {
    private int generatorId = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();       // id task    - task
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>(); // id subtask - subtask
    private final HashMap<Integer, Epic> epics = new HashMap<>();       // id epic    - epic

    //	a. Получение списка всех задач.
    public ArrayList<Task> getTasks() {
        return new ArrayList<> (tasks.values());
    }

    public ArrayList<Subtask> getSubtasks(){
        return new ArrayList<> (subtasks.values());
    }

    public ArrayList<Epic> getEpics(){
        return new ArrayList<> (epics.values());
    }

    // b. Удаление всех задач.
    public void deleteAllTask(){
        tasks.clear();
    }

    public void deleteAllSubtask(){
        for(Epic epic: epics.values()){
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void deleteAllEpic(){
        deleteAllSubtask();
        epics.clear();
    }

    // c. Получение по идентификатору.
    public Task getTask(int id){
        return tasks.get(id);
    }
    public Subtask getSubtask(int id){
        return subtasks.get(id);
    }
    public Epic getEpic(int id){
        return epics.get(id);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addNewSubtask(Subtask subtask){
        // 1) Проверить существование Epic
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            return -1;
        } else {
            final int id = ++generatorId;
            // 2) добавляем
            subtask.setId(id);
            epic.addSubtaskId(id);     // 1. добавляем в список у эпика id новой подзадчи
            subtasks.put(id, subtask); // 2. добавляем в HasMap subtasks новой подзадачи
            updateEpicStatus(epic);
            return id;
        }
    }

    public int addNewEpic(Epic epic){
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task){
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask){
        if (subtasks.containsKey(subtask.getId())){
            subtasks.put(subtask.getId(),subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateEpic(Epic newEpic){
        if(epics.containsKey(newEpic.getId())){
            epics.get(newEpic.getId()).setName(newEpic.getName());
            epics.get(newEpic.getId()).setDescription(newEpic.getDescription());
        } else {
            System.out.println("Нет такого Эпика: " + newEpic.getId());
        }
    }

    // f. Удаление по идентификатору.
    public void deleteTask(int idTask){
        if (tasks.containsKey(idTask)){
            tasks.remove(idTask);
        }
    }

    public void deleteSubtask(int idSubtask){
        // 1) удалить id в епике из subtaskIds 2) удалить сабтаск
        if (subtasks.containsKey(idSubtask)){
            Epic epic = epics.get(subtasks.get(idSubtask).getEpicId());
            epic.removeIdSubtask(idSubtask);
            subtasks.remove(idSubtask);
            updateEpicStatus(epic);
        }
    }

    public void deleteEpic(int idEpic){
        if(epics.containsKey(idEpic)){
            for(Subtask subtask : subtasks.values()){
                if (subtask.getEpicId() == idEpic) {
                    deleteSubtask(subtask.getId());
                }
            }
            epics.remove(idEpic);
        }
    }

    // *** a. Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getEpicSubtasks(int idEpic){
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
    public void updateEpicStatus(Epic epic) {
        Set<Status> statusesSubtasks = new HashSet<>();
        for (Integer id : epic.getSubtaskIds()){
            statusesSubtasks.add(getSubtask(id).getStatus());
        }

        if (epic.getSubtaskIds().isEmpty()
            || (statusesSubtasks.size()==1 && statusesSubtasks.contains(Status.NEW))) {
          /*если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.*/
            epic.setStatus(Status.NEW);
            return;
        } else if (statusesSubtasks.size()==1 && statusesSubtasks.contains(Status.DONE)) {
          /*если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.*/
            epic.setStatus(Status.DONE);
            return;
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
