package baseNoStates;

public final class DirectoryAreas {

    private DirectoryAreas() {}

    private static Partition rootArea;
    public static Partition getRootArea() { return rootArea; }

    public static void makeAreas() {
        // Particions (nivells)
        Partition building    = new Partition("building", "Building", null);
        Partition basement    = new Partition("basement", "Basement", building);
        Partition groundFloor = new Partition("ground_floor", "Ground floor", building);
        Partition floor1      = new Partition("floor1", "First floor", building);

        // Espais generals
        Space exterior = new Space("exterior", "Exterior", building);

        // Escales per planta (com a ESPAIS)
        Space stairsB  = new Space("stairs_basement", "Stairs (basement)", basement);
        Space stairsG  = new Space("stairs_ground",   "Stairs (ground)",   groundFloor);
        Space stairsF1 = new Space("stairs_floor1",   "Stairs (floor1)",   floor1);

        // Sòtan
        Space parking  = new Space("parking", "Parking", basement);

        // Planta baixa
        Space hall      = new Space("hall",      "Hall",      groundFloor);
        Space room1     = new Space("room1",     "Room 1",    groundFloor);
        Space room2     = new Space("room2",     "Room 2",    groundFloor);
        Space restRoomG = new Space("rest_room", "Rest room", groundFloor); // opcional

        // Planta 1
        Space corridor = new Space("corridor", "Corridor", floor1);
        Space room3    = new Space("room3",    "Room 3",   floor1);
        Space it       = new Space("IT",       "IT",       floor1);

        // Arbre
        building.addChild(basement);
        building.addChild(groundFloor);
        building.addChild(floor1);
        building.addChild(exterior); // per poder buscar-lo per id

        basement.addChild(parking);
        basement.addChild(stairsB);

        groundFloor.addChild(hall);
        groundFloor.addChild(room1);
        groundFloor.addChild(room2);
        groundFloor.addChild(restRoomG);
        groundFloor.addChild(stairsG);

        floor1.addChild(corridor);
        floor1.addChild(room3);
        floor1.addChild(it);
        floor1.addChild(stairsF1);

        // LINKS D1..D9  (REGISTRAR SEMPRE EN EL DESTÍ "to")
        link("D1", exterior, parking);   // exterior -> parking
        link("D2", stairsB,  parking);   // stairs_basement -> parking
        link("D3", exterior, hall);      // exterior -> hall
        link("D4", stairsG,  hall);      // stairs_ground   -> hall
        link("D5", hall,     room1);     // hall -> room1
        link("D6", hall,     room2);     // hall -> room2
        link("D7", stairsF1, corridor);  // stairs_floor1   -> corridor
        link("D8", corridor, room3);     // corridor -> room3  (F1)
        link("D9", corridor, it);        // corridor -> IT     (F1)

        rootArea = building;
    }

    public static Area findAreaById(String id) {
        if (rootArea == null || id == null) return null;
        return rootArea.findAreaById(id);
    }

    // Helper: assigna from/to i REGISTRA al DESTÍ
    private static void link(String doorId, Space from, Space to) {
        Door d = DirectoryDoors.findDoorById(doorId);
        if (d == null) throw new IllegalStateException("Puerta no encontrada: " + doorId);

        d.setFromSpace(from.getId());
        d.setToSpace(to.getId());

        // CLAU: la porta forma part de l'ÀREA del seu destí "to"
        to.addDoorGivingAccess(d);
    }
}
