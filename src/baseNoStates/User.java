package baseNoStates;

import java.time.LocalDateTime;

/**
 * Represents a user in the system, identified by their name and credential.
 * A user belongs to a UserGroup, which determines their permissions and schedule.
 */
public class User {
    private final String name;
    private final String credential;
    private final UserGroup userGroup;

    public User(String name, String credential, UserGroup userGroup) {
        this.name = name;
        this.credential = credential;
        this.userGroup = userGroup;
    }

    public String getCredential() {
        return credential;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public boolean canDoAction(String action, Space space, LocalDateTime requestDateTime) {
        return userGroup.isActionAuthorized(action, space, requestDateTime);
    }

    public boolean canBeInSpace(Space space) {
        return userGroup.canBeInSpace(space);
    }

    public boolean canSendRequest(LocalDateTime requestDateTime) {
        return userGroup.isInWorkSchedule(requestDateTime);
    }

    @Override
    public String toString() {
        return "User{name=" + name + ", credential=" + credential + "}";
    }
}
