package tasks;

public class Subtask extends Task {
    //Для каждой подзадачи известно, в рамках какого эпика она выполняется.
    protected int epicId; //

    //При создании подзадачи должно быть известно, в рамках какого эпика она выполняется.
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
    public Subtask(String name, String description, int id, int epicId ) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, int epicId ) {
        super(name, description, id, status );
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}