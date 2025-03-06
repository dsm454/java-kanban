package manager;

import java.io.File;

public class Managers {

    private Managers() {
    } // невозможно создать экземпляр для утилитарного класса

    public static TasksManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBacked() {
        return new FileBackedTaskManager(new File("./resources/tasks.csv"));
    }
}
