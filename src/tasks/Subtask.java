package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    //Для каждой подзадачи известно, в рамках какого эпика она выполняется.
    protected int epicId; //

    //При создании подзадачи должно быть известно, в рамках какого эпика она выполняется.
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, int epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Duration duration, LocalDateTime startTime, int id, int epicId) {
        super(name, description, duration, startTime, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Duration duration, LocalDateTime startTime, int id, Status status, int epicId) {
        super(name, description, duration, startTime, id, status);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + String.format("%02d:%02d", duration.toHoursPart(), duration.toMinutesPart()) +
                ", startTime='" + (startTime == null ? "" : startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))) + '\'' +
                ", status=" + status +
                '}';
    }
}
