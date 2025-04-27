package manager;

import tasks.Subtask;
import tasks.Task;
import tasks.Epic;
import tasks.TaskType;
import tasks.Status;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * CSV task format
 * id,type,name,status,description,starttime,duration,epic
 */

public class CSVTaskFormat {

    public static final String headLine = "id,type,name,status,description,starttime,duration,epic";

    public static String toString(Task task) {
        return (task.getId() + "," +
                task.getType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getStartTime() + "," +
                task.getDuration() +
                ((task.getType() == TaskType.SUBTASK) ? ("," + ((Subtask) task).getEpicId()) : ""));
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        final LocalDateTime startTime = values[5].equals("null") ? null : LocalDateTime.parse(values[5]);
        final Duration duration = Duration.parse(values[6]);
        return switch (type) {
            case TaskType.SUBTASK ->
                    new Subtask(name, description, duration, startTime, id, status, Integer.parseInt(values[7]));
            case TaskType.TASK -> new Task(name, description, duration, startTime, id, status);
            case TaskType.EPIC -> new Epic(name, description, duration, startTime, id, status);
        };
    }
}
