package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void shouldBeTasksEqualIfTheirIdEqual() {
        Task task1 = new Task("Задача  1","Описание задачи  1", 1);
        Task task2 = new Task("Задача 01","Описание задачи 01", 1);
        assertEquals(task1, task2, "Задачи должны быть равны если равны их id");
    }

    @Test
    void shouldBeTasksNotEqualIfTheirIdNotEqual() {
        Task task1 = new Task("Задача  1","Описание задачи  1", 1);
        Task task2 = new Task("Задача  1","Описание задачи  1", 2);
        assertNotEquals(task1, task2, "Задачи не должны быть равны, если не равны их id");
    }





}