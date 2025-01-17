package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void shouldBeEpicEqualIfTheirIdEqual() {
        Epic epic1 = new Epic("Эпик  1","Описание Эпика 1", 1);
        Epic epic2 = new Epic("Эпик  2","Описание Эпика 2", 1);
        assertEquals(epic1, epic2, "Эпики должны быть равны если равны их id");
    }

    @Test
    void shouldBeEpicNotEqualIfTheirIdNotEqual() {
        Epic epic1 = new Epic("Эпик  1","Описание Эпика 1", 1);
        Epic epic2 = new Epic("Эпик  2","Описание Эпика 2", 2);
        assertNotEquals(epic1, epic2, "Эпики не должны быть равны если не равны их id");
    }

}