package manager;

public class Managers {

    private Managers() {
    } // невозможно создать экземпляр для утилитарного класса

    public static TasksManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
