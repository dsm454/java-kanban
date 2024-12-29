package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    // Каждый эпик знает, какие подзадачи в него входят.
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description){
        super(name, description);
    }

    public Epic(String name, String description, int id){
        super(name, description, id);
    }

    public void addSubtaskId(int id) { subtaskIds.add(id); }

    public ArrayList<Integer> getSubtaskIds() { return subtaskIds; }

    // Удаление подзадчи по её id
    public void removeIdSubtask(int id){ subtaskIds.remove(Integer.valueOf(id)); }

    // Удаление всех подзадач из епика
    public void clearSubtaskIds(){ subtaskIds.clear(); }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
