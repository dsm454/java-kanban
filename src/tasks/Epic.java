package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    // Каждый эпик знает, какие подзадачи в него входят.
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public Epic(String name, String description, Duration duration, LocalDateTime startTime, int id, Status status) {
        super(name, description, duration, startTime, id, status);
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    // Удаление подзадачи по её id
    public void removeIdSubtask(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    // Удаление всех подзадач из епика
    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration Epic='" + String.format("%02d:%02d", duration.toHoursPart(), duration.toMinutesPart()) +
                ", startTime='" + (startTime == null ? "" : startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))) + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
