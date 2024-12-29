import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import manager.*;

public class Main {

    public static void main(String[] args) {
        TasksManager manager = new TasksManager();
        // Создайте - две задачи,
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        //  - а также эпик с двумя подзадачами
        Epic epic1 = new Epic("epic 1", "epic 1 dsc");
        manager.addNewEpic(epic1);
        Subtask subtask1ForEpic1 = new Subtask("name subtask1", "descr1 Epic 1", epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("name subtask2", "descr2 Epic 1", epic1.getId());
        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        //   - и эпик с одной подзадачей.
        Epic epic2 = new Epic("epic 2", "epic 2 dsc");
        manager.addNewEpic(epic2);
        Subtask subtask1ForEpic2 = new Subtask("name subtask1", "descr1 Epic2", epic2.getId());
        manager.addNewSubtask(subtask1ForEpic2);
        System.out.println("-".repeat(40));

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        System.out.println("1 *** Все Зпики: " + manager.getEpics());
        System.out.println("1 *** Все Задачи: " + manager.getTasks());
        System.out.println("1 *** Все Подзадачи: " + manager.getSubtasks());
        System.out.println("-".repeat(40));

        // Измените статусы созданных объектов, распечатайте их.
        // Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        manager.updateTask(new Task("Задача 1 v.2", "Описане задачи v.2", task1.getId(), Status.IN_PROGRESS));
        manager.updateSubtask(new Subtask("name subtask1 v.2", "descr1 Epic 1 v.2",
                                           subtask1ForEpic1.getId(), Status.IN_PROGRESS, subtask1ForEpic1.getEpicId()));
        System.out.println("2 *** Все Зпики: " + manager.getEpics());
        System.out.println("2 *** Все Задачи: " + manager.getTasks());
        System.out.println("2 *** Все Подзадачи: " + manager.getSubtasks());
        System.out.println("-".repeat(40));

        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.deleteTask(task1.getId());
        manager.deleteEpic(epic2.getId());
        System.out.println("3 *** Все Зпики: " + manager.getEpics());
        System.out.println("3 *** Все Задачи: " + manager.getTasks());
        System.out.println("3 *** Все Подзадачи: " + manager.getSubtasks());
        System.out.println("-".repeat(40));

    }
}
