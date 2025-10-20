package baseNoStates;

import java.util.ArrayList;

public abstract class Area {
    protected final String id;
    protected final String description;
    protected final Partition parent;

    protected Area(String id, String description, Partition parent) {
        this.id = id;
        this.description = description;
        this.parent = parent;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public Partition getParent() { return parent; }

    /** Devuelve TODAS las puertas que dan acceso a cualquiera de los espacios bajo este área. */
    public abstract ArrayList<Door> getDoorsGivingAccess();

    /** Busca recursivamente un área por id (partition o space) y la devuelve o null si no existe. */
    public abstract Area findAreaById(String areaId);

    /** Devuelve la lista de espacios hoja bajo este área (útil para autorización por espacios). */
    public abstract ArrayList<Space> getSpaces();

    @Override public String toString() { return id; }
}
