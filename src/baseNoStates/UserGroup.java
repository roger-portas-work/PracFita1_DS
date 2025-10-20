package baseNoStates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of users that share the same permissions and
 * authorised areas.  A group holds the set of actions it can perform,
 * the set of areas it can enter and a schedule that restricts when
 * requests are accepted.  Users belong to exactly one group and
 * delegate authorisation checks to it.
 */
public class UserGroup {
    private final String name;
    private final ArrayList<String> actions;
    private final ArrayList<Area> areas;
    private final Schedule schedule;
    private final ArrayList<User> users;

    public UserGroup(String name,
                     ArrayList<String> actions,
                     ArrayList<Area> areas,
                     Schedule schedule) {
        this.name = name;
        this.actions = actions;
        this.areas = areas;
        this.schedule = schedule;
        this.users = new ArrayList<>();
    }

    /** Registers a user to this group. */
    public void addUser(User user) {
        users.add(user);
    }

    /** Returns an unmodifiable view of the users in this group. */
    public List<User> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    /** Returns true if this group has permission to perform the given action. */
    public boolean hasPermission(String action) {
        return actions.contains(action);
    }

    /**
     * Returns true if this group can be in the given space.  The group
     * stores areas (which may be partitions or spaces) and delegates to
     * Area.getSpaces() to obtain the leaf spaces under each area.
     */
    public boolean canBeInSpace(Space space) {
        String spaceId = space.getId();
        for (Area area : areas) {
            for (Space s : area.getSpaces()) {
                if (s.getId().equals(spaceId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns true if the given timestamp is within this group's schedule. */
    public boolean isInWorkSchedule(LocalDateTime dateTime) {
        return schedule.isWithinSchedule(dateTime);
    }

    /**
     * Returns true if this group is authorised to perform the given action
     * in the specified space at the specified time.
     */
    public boolean isActionAuthorized(String action,
                                      Space space,
                                      LocalDateTime dateTime) {
        return hasPermission(action) &&
                canBeInSpace(space) &&
                isInWorkSchedule(dateTime);
    }

    /** Returns all the leaf spaces contained in this group's areas. */
    public ArrayList<Space> getSpaces() {
        ArrayList<Space> result = new ArrayList<>();
        for (Area area : areas) {
            result.addAll(area.getSpaces());
        }
        return result;
    }
}
