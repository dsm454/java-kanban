import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import manager.*;

public class Main {

    public static void main(String[] args) {
        TasksManager manager = Managers.getDefault();
        // 1. Создайте две задачи,
        Task task1 = new Task("Задача 1!", "Описание задачи 1");
        Task task2 = new Task("Задача 2!", "Описание задачи 2");
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        //  - а также эпик с тремя подзадачами
        Epic epic1 = new Epic("epic 1", "epic 1 dsc");
        manager.addNewEpic(epic1);

        Subtask subtask1ForEpic1 = new Subtask("name subtask1", "descr1 subtask1 Epic 1", epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("name subtask2", "descr2 subtask2 Epic 1", epic1.getId());
        Subtask subtask3ForEpic1 = new Subtask("name subtask3", "descr3 subtask3 Epic 1", epic1.getId());
        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        manager.addNewSubtask(subtask3ForEpic1);

        // 2. Запросите созданные задачи несколько раз в разном порядке.
        // 3. После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1ForEpic1.getId());
        manager.getSubtask(subtask2ForEpic1.getId());
        manager.getSubtask(subtask3ForEpic1.getId());
        printHistory(manager);

        manager.getTask(task1.getId());
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1ForEpic1.getId());
        manager.getSubtask(subtask2ForEpic1.getId());
        manager.getSubtask(subtask3ForEpic1.getId());
        manager.getSubtask(subtask1ForEpic1.getId());
        manager.getSubtask(subtask2ForEpic1.getId());
        manager.getSubtask(subtask3ForEpic1.getId());
        System.out.println("Убедитесь, что в истории нет повторов");
        printHistory(manager);

        // 4. Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
        manager.deleteTask(task1.getId());
        System.out.println("Уадалена задача task1, проверить что ее нет в истории.");
        printHistory(manager);

        // 5. Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        manager.deleteEpic(epic1.getId());
        System.out.println("Удален эпик с тремя подзадачами, убедиться, что из истории удалился как сам эпик, так и все его подзадачи.");
        printHistory(manager);
    }

    private static void printAllTasks(TasksManager manager) {
        System.out.println(" ### Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println(" ### Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println(" ### Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
    }

    private static void printHistory(TasksManager manager) {
        System.out.println("-".repeat(40));
        System.out.println(" ### История:");
        for (int i = 0; i < manager.getHistory().size(); i++) {
            System.out.println((i + 1) + " - " + manager.getHistory().get(i));
        }
        System.out.println("Количество просмотров в истории " + manager.getHistory().size());
        System.out.println("Количество объектов в менеджере " + (manager.getSubtasks().size() + manager.getEpics().size() + manager.getTasks().size()));
        System.out.println("-".repeat(40));
    }
}
