package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        private Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }

    private void removeNode(Node node){
        Node prevNode = node.prev;
        Node nextNode = node.next;
        if (prevNode == null && nextNode == null ){ // удаляем единственную ноду
            nodeMap.remove(node.task.getId());
            head = null;
            tail = null;
            node = null;
            return;
        }
        if (prevNode == null && nextNode != null){ // удаляем голову
            nextNode.prev = null;
            head = nextNode;
        } else if (prevNode != null && nextNode != null) { // внутренняя Нода
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        } else if (prevNode != null && nextNode == null) { // удаляем хвост
            prevNode.next = null;
            tail = prevNode;
        }
        nodeMap.remove(node.task.getId());
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        int idTask = task.getId();

        Node oldTail = tail;
        Node newNode = new Node(null, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            newNode.prev = oldTail;
            oldTail.next = newNode;
        }

        if(nodeMap.containsKey(idTask)) {
            removeNode(nodeMap.get(idTask));
        }

        nodeMap.put(idTask, newNode);

    }

    @Override
    public void removeTask(int id) {
        if(!nodeMap.containsKey(id)){
            return;
        } else {
            removeNode(nodeMap.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        final List<Task> tasks = new ArrayList<>();
        Node node = tail;
        while (node != null) {
            tasks.add(node.task);
            node = node.prev;
        }
        return tasks;
    }
}
