package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void ShouldReturnInitializedInstancesManager() {
        TasksManager manager = Managers.getDefault();
        assertNotNull(manager, "Экземпляр TasksManager должен быть проинициализирован.");
    }

    @Test
    void ShouldReturnInitializedInstancesHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Экземпляр HistoryManager должен быть проинициализирован.");
    }
}