package baseNoStates;

public class Main {
    public static void main(String[] args) {
        DirectoryDoors.makeDoors();
        DirectoryAreas.makeAreas();      // ← construye el árbol y registra puertas en el “to”
        DirectoryUserGroups.makeUserGroups(); // ← en vez de DirectoryUsers.makeUsers()
        new WebServer();

        // (Paso 2 – cuando lo hagamos): Clock.getInstance().startClock();
    }
}
