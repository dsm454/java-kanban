package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;        // Название, описывающее задачу (например, «Переезд»).
    protected String description; // Описание
    protected int id;             // id задачи, по которому её можно будет найти.
    protected Status status;
    protected Duration duration = Duration.ofMinutes(0);  // продолжительность задачи, сколько времени она займёт в минутах
    protected LocalDateTime startTime;// = LocalDateTime.now();

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this(name, description);
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, int id) {
        this(name, description);
        this.id = id;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime, int id) {
        this(name, description, duration, startTime);
        this.id = id;
    }

    public Task(String name, String description, int id, Status status) {
        this(name, description, id);
        this.status = status;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime, int id, Status status) {
        this(name, description, duration, startTime, id);
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) return null;
        return startTime.plus(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration='" + String.format("%02d:%02d", duration.toHoursPart(), duration.toMinutesPart()) + '\'' +
                ", startTime='" + (startTime == null ? "" : startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))) + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
