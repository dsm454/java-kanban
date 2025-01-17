package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void shouldBeSubTasksEqualIfTheirIdEqual() {
        Subtask subtask1 = new Subtask("Подзадача  1","Описание подзадачи  1", 1, 1);
        Subtask subtask2 = new Subtask("Подзадача  2","Описание подзадачи  2", 1, 2);
        assertEquals(subtask1, subtask2, "Подзадачи должны быть равны если равны их id");
    }

    @Test
    void shouldBeSubTasksNotEqualIfTheirIdNotEqual() {
        Subtask subtask1 = new Subtask("Подзадача  1","Описание подзадачи  1", 1, 1);
        Subtask subtask2 = new Subtask("Подзадача  1","Описание подзадачи  1", 2, 1);
        assertNotEquals(subtask1, subtask2, "Подзадачи не должны быть равны если не равны их id");
    }

}