package baseNoStates.requests;

import baseNoStates.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class RequestArea implements Request {
    private final String credential;
    private final String action;           // "lock" o "unlock"
    private final LocalDateTime now;
    private final String areaId;

    // Resultado agregado
    private boolean authorized = true;     // true si TODAS autorizadas; útil si luego lo usas
    private final JSONArray requestsDoors = new JSONArray();

    public RequestArea(String credential, String action, LocalDateTime now, String areaId) {
        this.credential = credential;
        this.action = action;
        this.now = now;
        this.areaId = areaId;
    }

    @Override
    public void process() {
        // Validación básica de acción de área
        if (!Actions.LOCK.equals(Actions.canonicalize(action)) &&
                !Actions.UNLOCK.equals(Actions.canonicalize(action))) {
            // Ignoramos acciones no válidas en áreas; podrías añadir "reasons"
            return;
        }

        Area area = DirectoryAreas.findAreaById(areaId);
        if (area == null) return;

        for (Door d : area.getDoorsGivingAccess()) {
            RequestReader rr = new RequestReader(credential, action, now, d.getId());
            rr.process();
            JSONObject jr = rr.answerToJson();
            // Si quieres coherencia estricta: authorized global = AND de todas las puertas
            if (!jr.optBoolean("authorized", false)) authorized = false;
            requestsDoors.put(jr);
        }
    }

    @Override
    public JSONObject answerToJson() {
        JSONObject j = new JSONObject();
        j.put("areaId", areaId);
        j.put("action", action);
        j.put("authorized", authorized);
        j.put("requestsDoors", requestsDoors);
        return j;
    }

    @Override
    public String toString() {
        return "Request area\nuser credential " + credential + " action " + action +
                " datetime " + now + "\nareaId " + areaId + "\nauthorized " + authorized;
    }
}
