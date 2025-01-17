package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    final int HISTORY_MAX_CAPACITY = 10;
    private final List<Task> tasksHistory = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        if (tasksHistory.size() >= HISTORY_MAX_CAPACITY) {
            tasksHistory.removeFirst();
        }
        tasksHistory.add(task);
    }
}
