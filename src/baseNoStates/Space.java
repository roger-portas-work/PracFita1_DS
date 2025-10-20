package baseNoStates;

import java.util.ArrayList;

public class Space extends Area {
    private final ArrayList<Door> doorsGivingAccess = new ArrayList<>();

    public Space(String id, String description, Partition parent) {
        super(id, description, parent);
    }

    /** Se llama desde DirectoryAreas cuando se asignan from/to a cada Door. */
    public void addDoorGivingAccess(Door d) {
        if (!doorsGivingAccess.contains(d)) doorsGivingAccess.add(d);
    }

    public ArrayList<Door> getDoors() { return new ArrayList<>(doorsGivingAccess); }

    @Override
    public ArrayList<Door> getDoorsGivingAccess() { return getDoors(); }

    @Override
    public Area findAreaById(String areaId) { return id.equals(areaId) ? this : null; }

    @Override
    public ArrayList<Space> getSpaces() {
        ArrayList<Space> res = new ArrayList<>();
        res.add(this);
        return res;
    }
}
