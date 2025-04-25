package manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {

    @Test
    @DisplayName("1 - сохранение и загрузку пустого файла")
    void shouldSaveLoadEmptyFile() throws IOException {
        File emptyFile = File.createTempFile("emptyFile", "csv");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(emptyFile);
        assertNotNull(manager, "Экземпляр FileBackedTaskManager не должен быть null");
    }

    @Test
    @DisplayName("2 - сохранение нескольких задач в файл")
    void shouldSaveSeveralTasksInFile() throws IOException {
        LocalDateTime testDateTime = LocalDateTime.parse("2025-04-25T00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        File file = File.createTempFile("file", "csv");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        //Создаем и добавляем задачи в менеджер
        Task task1 = new Task("Задача 1!", "Описание задачи 1", Duration.ofMinutes(5), testDateTime);
        Task task2 = new Task("Задача 2!", "Описание задачи 2", Duration.ofMinutes(5), testDateTime.plusMinutes(15));
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        Epic epic1 = new Epic("epic 1", "epic 1 dsc");
        manager.addNewEpic(epic1);
        Subtask subtask1ForEpic1 = new Subtask("Подзадача 1", "Описание подзадачи 1"
                , Duration.ofMinutes(5), testDateTime.plusMinutes(45), -1, Status.NEW, epic1.getId());

        Subtask subtask2ForEpic1 = new Subtask("Подзадача 2", "Описание подзадачи 2"
                , Duration.ofMinutes(5), testDateTime.plusMinutes(60), -1, Status.NEW, epic1.getId());

        Subtask subtask3ForEpic1 = new Subtask("Подзадача 3", "Описание подзадачи 3"
                , Duration.ofMinutes(5), testDateTime.plusMinutes(75), -1, Status.NEW, epic1.getId());

        manager.addNewSubtask(subtask1ForEpic1);
        manager.addNewSubtask(subtask2ForEpic1);
        manager.addNewSubtask(subtask3ForEpic1);

        // Задачи должны были записаться в файл. Из файла в список.
        ArrayList<String> line = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String s = fileReader.readLine();
                line.add(s);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }

        assertEquals(CSVTaskFormat.headLine, line.get(0), "1-ая строка файла должна быть заголовком");
        assertEquals("1,TASK,Задача 1!,NEW,Описание задачи 1,2025-04-25T00:00,PT5M", line.get(1)
                , "2-ая строка файла не соответствует загруженной задаче");
        assertEquals("2,TASK,Задача 2!,NEW,Описание задачи 2,2025-04-25T00:15,PT5M", line.get(2)
                , "3-ая строка файла не соответствует загруженной задаче");
        assertEquals("3,EPIC,epic 1,NEW,epic 1 dsc,2025-04-25T00:45,PT15M", line.get(3)
                , "4-ая строка файла не соответствует загруженной задаче");
        assertEquals("4,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1,2025-04-25T00:45,PT5M,3", line.get(4)
                , "5-ая строка файла не соответствует загруженной задаче");
        assertEquals("5,SUBTASK,Подзадача 2,NEW,Описание подзадачи 2,2025-04-25T01:00,PT5M,3", line.get(5)
                , "6-ая строка файла не соответствует загруженной задаче");
        assertEquals("6,SUBTASK,Подзадача 3,NEW,Описание подзадачи 3,2025-04-25T01:15,PT5M,3", line.get(6)
                , "7-ая строка файла не соответствует загруженной задаче");
    }

    @Test
    @DisplayName("3 - Загрузка нескольких задач из файла")
    void shouldLoadSeveralTasksFromFile() throws IOException {
        //Создаем файл и записываем задачи в виде строк
        File file = File.createTempFile("file", "csv");
        String[] arrayStrTask = {
                "id,type,name,status,description,starttime,duration,epic"
                , "1,TASK,Задача 1!,NEW,Описание задачи 1,2025-04-25T00:00,PT5M"
                , "2,TASK,Задача 2!,NEW,Описание задачи 2,2025-04-25T00:15,PT5M"
                , "3,EPIC,epic 1,NEW,epic 1 dsc,2025-04-25T00:45,PT15M"
                , "4,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1,2025-04-25T00:45,PT5M,3"
                , "5,SUBTASK,Подзадача 2,NEW,Описание подзадачи 2,2025-04-25T01:00,PT5M,3"
                , "6,SUBTASK,Подзадача 3,NEW,Описание подзадачи 3,2025-04-25T01:15,PT5M,3"
        };

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            for (String str : arrayStrTask) {
                bufferedWriter.write(str + "\n");
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи в файла.");
        }
        // создаем менеджер из файла
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        assertEquals("Задача 1!", manager.getTask(1).getName()
                , "Задача id=1 должна иметь имя 'Задача 1!'");

        assertEquals("epic 1", manager.getEpic(3).getName()
                , "Задача id=3 должна иметь имя 'epic 1'");

        assertEquals("Подзадача 3", manager.getSubtask(6).getName()
                , "Задача id=6 должна иметь имя 'Подзадача 3'");

        assertEquals(6, manager.tasks.size() + manager.subtasks.size() + manager.epics.size()
                , "Количество задач в менеджере не совпадает с ожидаемым значением");
    }

}
