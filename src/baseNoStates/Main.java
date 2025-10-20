package baseNoStates;

public class Main {
    public static void main(String[] args) {
        DirectoryDoors.makeDoors();
        DirectoryAreas.makeAreas();      // construye el árbol y registra las puertas en el espacio destino
        DirectoryUserGroups.makeUserGroups(); // crea usuarios y grupos con permisos y horarios
        new WebServer();
        // Si implementas el temporizador de unlock_shortly más adelante:
        // Clock.getInstance().startClock();
    }
}
