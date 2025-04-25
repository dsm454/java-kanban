import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import manager.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {

    public static void main(String[] args) {
        File file = new File("./resources/tasks.csv");
        printFromFile(file, "*********** Начальное Содержимое файла *********** ");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        printAllTasks(manager, "*********** Менеджер после загрузки задач из файла ************");

        manager.deleteAllEpic();
        manager.deleteAllTask();
        System.out.println();
        printAllTasks(manager, "*********** Менеджер после удаления всех задач *************");
        printFromFile(file, "*********** Содержимое файла после удаления всех задач и эпиков *********** ");
        System.out.println("----".repeat(5));
        System.out.println(manager.getPrioritizedTasks());

        // проверить сохранение нескольких задач
        // 1. Создайте две задачи,
        Task task1 = new Task("Задача 1!", "Описание задачи 1", Duration.ofMinutes(20), LocalDateTime.of(2025, 04, 20, 10, 15));
        Task task2 = new Task("Задача 2!", "Описание задачи 2");
        Task task3 = new Task("Задача 3!", "Описание задачи 3", Duration.ofMinutes(7), LocalDateTime.of(2099, 12, 20, 10, 15));
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        manager.addNewTask(task3);
        manager.deleteTask(task1.getId());
        //  - а также эпик с тремя подзадачами
        Epic epic1 = new Epic("epic 1", "epic 1 dsc");
        manager.addNewEpic(epic1);

        Subtask subtask1ForEpic1 = new Subtask("name subtask1", "descr1 subtask1 Epic 1", Duration.ofMinutes(120), LocalDateTime.of(2025, 04, 10, 12, 10), epic1.getId());
        Subtask subtask2ForEpic1 = new Subtask("name subtask2", "descr2 subtask2 Epic 1", Duration.ofMinutes(27), LocalDateTime.of(2025, 04, 10, 12, 10), epic1.getId());
        Subtask subtask3ForEpic1 = new Subtask("name subtask3", "descr3 subtask3 Epic 1", Duration.ofMinutes(10), LocalDateTime.of(2025, 07, 10, 12, 10), epic1.getId());

        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        manager.addNewSubtask(subtask3ForEpic1);

        System.out.println(manager.getEpicSubtasks(epic1.getId()));
        System.out.println(manager.getEpicSubtasks(-1));


        System.out.println("EndTime epic1 = " + epic1.getEndTime());
        System.out.println("EndTime task1 = " + task1.getEndTime());
        System.out.println("EndTime subTask1 = " + subtask1ForEpic1.getEndTime());


        printAllTasks(manager, "*********** Менеджер после добавления задач и эпиков *************");
        printFromFile(file, "*********** Содержимое файла после добавления задач и эпиков *********** ");

        System.out.println("++".repeat(5));
        System.out.println(manager.getPrioritizedTasks());

    }

    private static void printAllTasks(TasksManager manager, String message) {
        System.out.println(message);
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
        System.out.println("#".repeat(40));
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

    private static void printFromFile(File file, String message) {
        System.out.println(message);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                System.out.println(fileReader.readLine());
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        System.out.println();
    }
}
