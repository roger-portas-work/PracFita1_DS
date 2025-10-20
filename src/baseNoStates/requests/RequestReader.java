package baseNoStates.requests;

import baseNoStates.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestReader implements Request {
    private final String credential; // who
    private final String action;     // what
    private final LocalDateTime now; // when
    private final String doorId;     // where
    private String userName;
    private boolean authorized;
    private final ArrayList<String> reasons; // why not authorized
    private String doorStateName;
    private boolean doorClosed;

    public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
        this.credential = credential;
        this.action = action;
        this.doorId = doorId;
        this.reasons = new ArrayList<>();
        this.now = now;
    }

    public void setDoorStateName(String name) {
        doorStateName = name;
    }

    public String getAction() {
        return action;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void addReason(String reason) {
        reasons.add(reason);
    }

    @Override
    public String toString() {
        if (userName == null) userName = "unknown";
        return "Request{credential=" + credential + ", userName=" + userName
                + ", action=" + action + ", now=" + now + ", doorID=" + doorId
                + ", closed=" + doorClosed + ", authorized=" + authorized
                + ", reasons=" + reasons + "}";
    }

    @Override
    public JSONObject answerToJson() {
        JSONObject json = new JSONObject();
        json.put("authorized", authorized);
        json.put("action", action);
        json.put("doorId", doorId);
        json.put("closed", doorClosed);
        json.put("state", doorStateName);
        json.put("reasons", new JSONArray(reasons));
        return json;
    }

    public void process() {
        User user = DirectoryUserGroups.findUserByCredential(credential);
        Door door = DirectoryDoors.findDoorById(doorId);
        assert door != null : "door " + doorId + " not found";

        authorize(user, door);      // sets authorized + reasons
        door.processRequest(this);  // executes action if authorized, sets stateName
        doorClosed = door.isClosed();
    }

    // Determine if the user is allowed to do the action in this space and time
    private void authorize(User user, Door door) {
        if (user == null) {
            authorized = false;
            userName = "unknown";
            addReason("User does not exist");
            return;
        }
        userName = user.getCredential();

        // --- Door has String ids: resolve to Space objects via DirectoryAreas ---
        String fromId = door.getFromSpace(); // String
        String toId   = door.getToSpace();   // String

        Area fromArea = DirectoryAreas.findAreaById(fromId);
        Area toArea   = DirectoryAreas.findAreaById(toId);

        if (!(fromArea instanceof Space) || !(toArea instanceof Space)) {
            authorized = false;
            addReason("Door spaces are not valid");
            return;
        }

        Space fromSpace = (Space) fromArea;
        Space toSpace   = (Space) toArea;

        // Within schedule?
        if (!user.canSendRequest(now)) {
            authorized = false;
            addReason("Not within work schedule");
            return;
        }
        // Allowed in both spaces?
        if (!user.canBeInSpace(fromSpace) || !user.canBeInSpace(toSpace)) {
            authorized = false;
            addReason("User isn't allowed in the space(s)");
            return;
        }
        // Allowed to do the action?
        if (!user.canDoAction(action, fromSpace, now)) {
            authorized = false;
            addReason("User isn't allowed to do the action");
            return;
        }
        authorized = true;
    }
}
