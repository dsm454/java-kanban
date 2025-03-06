package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Статический метод который без создания объекта типа FileBackedTaskManager,
    // генерирует его из файла (аналог конструктора реализованный через статический метод)
    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManagerFromFile = new FileBackedTaskManager(file);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            int countLine = 0;
            fileReader.readLine();
            while (fileReader.ready()) {
                line = fileReader.readLine();
                Task task = CSVTaskFormat.taskFromString(line);
                switch (task.getType()) {
                    case TASK -> taskManagerFromFile.tasks.put(task.getId(), task);
                    case SUBTASK -> taskManagerFromFile.subtasks.put(task.getId(), (Subtask) task);
                    case EPIC -> taskManagerFromFile.epics.put(task.getId(), (Epic) task);
                }
                if (task.getId() > taskManagerFromFile.generatorId) {
                    taskManagerFromFile.generatorId = task.getId();
                }
            }
            if (!taskManagerFromFile.subtasks.isEmpty()) {
                for (Subtask subtask : taskManagerFromFile.subtasks.values()) {
                    Epic epic = taskManagerFromFile.epics.get(subtask.getEpicId());
                    epic.addSubtaskId(subtask.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
        return taskManagerFromFile;
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskFormat.headLine + "\n");
            for (Task task : super.getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : super.getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : super.getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "\n");
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Не удалось сохранить данные в файл.");
        }
    }


    // b. Удаление всех задач.
    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask.getEpicId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    // e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteTask(int idTask) {
        super.deleteTask(idTask);
        save();
    }

    @Override
    public void deleteSubtask(int idSubtask) {
        super.deleteSubtask(idSubtask);
        save();
    }

    @Override
    public void deleteEpic(int idEpic) {
        super.deleteEpic(idEpic);
        save();
    }

}
