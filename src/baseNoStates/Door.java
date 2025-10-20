package baseNoStates;

import org.json.JSONObject;

/**
 * Door domain object using the State pattern.
 * - Estados lógicos: "unlocked", "locked", "unlocked_shortly", "propped"
 * - Atributo físico: closed (true/false)
 * - fromSpace / toSpace: espacios origen/destino (para áreas y autorización futura)
 */
public class Door {
    private final String id;
    private boolean closed;

    // Estado lógico (State Pattern)
    private DoorState state;

    // Espacios (se rellenan desde DirectoryAreas.init())
    private String fromSpace;   // dónde está el lector (origen)
    private String toSpace;     // a dónde accedes (destino)

    public Door(String id) {
        this.id = id;
        this.closed = true;                       // físicamente cerrada por defecto
        this.state = new DoorStateUnlocked(this); // estado lógico inicial
        // Valores provisionales; se asignarán correctamente desde DirectoryAreas.init()
        this.fromSpace = "hall";
        this.toSpace   = "office";
    }

    // ------------------- Getters / Setters básicos -------------------
    public String getId() { return id; }

    public boolean isClosed() { return closed; }
    public void setClosed(boolean closed) { this.closed = closed; }

    /** Nombre del estado lógico para el JSON que usa el simulador. */
    public String getStateName() { return state.getName(); }

    public void setState(DoorState newState) { this.state = newState; }

    public String getFromSpace() { return fromSpace; }
    public String getToSpace()   { return toSpace; }
    public void setFromSpace(String s) { this.fromSpace = s; }
    public void setToSpace(String s)   { this.toSpace = s; }

    // ------------------- Ejecución de acciones -------------------
    /**
     * Ejecuta una acción recibida como String (desde la request web).
     * Acciones válidas: open, close, lock, unlock, unlock_shortly
     *
     * Nota: "unlock_shortly" entra en un estado temporal que, tras ~10s, cambia a:
     *   - "locked" si la puerta sigue cerrada
     *   - "propped" si la puerta sigue abierta
     */
    public void doAction(String action) {
        String a = Actions.canonicalize(action);
        if (a == null) return;

        if (Actions.OPEN.equals(a)) {
            state.open();
        } else if (Actions.CLOSE.equals(a)) {
            state.close();
        } else if (Actions.LOCK.equals(a)) {
            state.lock();
        } else if (Actions.UNLOCK.equals(a)) {
            state.unlock();
        } else if (Actions.UNLOCK_SHORTLY.equals(a)) {
            // Entramos al estado temporal directamente
            setState(new DoorStateUnlockedShortly(this));
        }
    }

    /**
     * Integración con RequestReader:
     * - Canonicaliza la acción
     * - Si no autorizado, NO ejecuta (pero reporta estado actual)
     * - Si acción desconocida, añade reason y NO ejecuta
     * - Actualiza en la request el nombre del estado resultante
     */
    public void processRequest(baseNoStates.requests.RequestReader req) {
        String canonical = Actions.canonicalize(req.getAction());
        if (canonical == null) {
            req.addReason("Not allowed action");
            req.setDoorStateName(getStateName());
            return;
        }
        if (!req.isAuthorized()) {
            req.setDoorStateName(getStateName());
            return;
        }

        doAction(canonical);
        req.setDoorStateName(getStateName());
    }

    // ------------------- Salida JSON para /refresh -------------------
    /** JSON usado por RequestRefresh (lo pinta el simulador). */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("closed", isClosed());
        json.put("state", getStateName());
        json.put("fromSpace", getFromSpace());
        json.put("toSpace", getToSpace());
        return json;
    }

    @Override
    public String toString() {
        return "Door{ id='" + id + "', closed=" + closed + ", state=" + getStateName()
                + ", fromSpace='" + fromSpace + "', toSpace='" + toSpace + "' }";
    }
}
