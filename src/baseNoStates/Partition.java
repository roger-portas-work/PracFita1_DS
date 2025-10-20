package baseNoStates;

import java.util.ArrayList;

public class Partition extends Area {
    private final ArrayList<Area> children = new ArrayList<>();

    public Partition(String id, String description, Partition parent) {
        super(id, description, parent);
    }

    public void addChild(Area a) { children.add(a); }

    @Override
    public ArrayList<Door> getDoorsGivingAccess() {
        ArrayList<Door> res = new ArrayList<>();
        for (Area c : children) res.addAll(c.getDoorsGivingAccess());
        return res;
    }

    @Override
    public Area findAreaById(String areaId) {
        if (id.equals(areaId)) return this;
        for (Area c : children) {
            Area found = c.findAreaById(areaId);
            if (found != null) return found;
        }
        return null;
    }

    @Override
    public ArrayList<Space> getSpaces() {
        ArrayList<Space> res = new ArrayList<>();
        for (Area c : children) res.addAll(c.getSpaces());
        return res;
    }
}
