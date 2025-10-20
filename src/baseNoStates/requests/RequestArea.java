package baseNoStates.requests;

import baseNoStates.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestArea implements Request {
    private final String credential;
    private final String action;
    private final String areaId;
    private final LocalDateTime requestDateTime;
    private final ArrayList<RequestReader> requests = new ArrayList<>();
    private boolean authorized = true;

    public RequestArea(String credential, String action, LocalDateTime dateTime, String areaId) {
        this.credential = credential;
        this.action = action;
        this.areaId = areaId;
        this.requestDateTime = dateTime;
    }

    public String getAction() {
        return action;
    }

    public JSONObject answerToJson() {
        JSONObject json = new JSONObject();
        json.put("authorized", authorized);
        json.put("action", action);
        json.put("areaId", areaId);

        JSONArray requestJson = new JSONArray();
        for (RequestReader rr : requests) {
            requestJson.put(rr.answerToJson());
        }
        json.put("requestsDoors", requestJson);
        return json;
    }

    @Override
    public String toString() {
        return "Request{" +
                "credential=" + credential +
                ", action=" + action +
                ", now=" + requestDateTime +
                ", areaId=" + areaId +
                ", requests=" + requests +
                "}";
    }

    public void process() {
        User user = DirectoryUserGroups.findUserByCredential(credential);
        // invalid action: only lock/unlock allowed for area requests
        String canonical = Actions.canonicalize(action);
        if (user == null || canonical == null ||
                !(Actions.LOCK.equals(canonical) || Actions.UNLOCK.equals(canonical))) {
            authorized = false;
            return;
        }
        if (!user.canSendRequest(requestDateTime)) {
            authorized = false;
            return;
        }

        Area area = DirectoryAreas.findAreaById(areaId);
        if (area == null) {
            authorized = false;
            return;
        }

        // Gather all doors whose to-space is in this area
        ArrayList<Door> doorsInArea = new ArrayList<>();
        for (Space space : area.getSpaces()) {
            doorsInArea.addAll(space.getDoorsGivingAccess());
        }

        if (doorsInArea.isEmpty()) {
            return; // nothing to do; remains authorized
        }

        for (Door door : doorsInArea) {
            RequestReader req = new RequestReader(credential, action, requestDateTime, door.getId());
            req.process();
            if (!req.isAuthorized()) {
                authorized = false;
            }
            requests.add(req);
        }
    }
}
