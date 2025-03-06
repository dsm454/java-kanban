package manager;

import tasks.Subtask;
import tasks.Task;
import tasks.Epic;
import tasks.TaskType;
import tasks.Status;

/**
 * CSV task format
 * id,type,name,status,description,epic
 */

public class CSVTaskFormat {

    public static final String headLine = "id,type,name,status,description,epic";

    public static String toString(Task task) {
        return (task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() +
                ((task.getType() == TaskType.SUBTASK) ? ("," + ((Subtask) task).getEpicId()) : ""));
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        return switch (type) {
            case TaskType.SUBTASK -> new Subtask(name, description, id, status, Integer.parseInt(values[5]));
            case TaskType.TASK -> new Task(name, description, id, status);
            case TaskType.EPIC -> new Epic(name, description, id);
        };
    }
}
