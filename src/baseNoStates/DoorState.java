package baseNoStates;

/**
 * Abstract class representing the state of a door.
 * Each door can have different states like Locked, Unlocked, or later Propped/UnlockedShortly.
 * The State pattern lets us change door behaviour depending on its current state.
 */
public abstract class DoorState {
    protected final Door door;
    private final String name;

    protected DoorState(Door door, String name) {
        this.door = door;
        this.name = name;
    }

    /** State name for JSON: "unlocked", "locked" (luego "unlocked_shortly", "propped") */
    public final String getName() { return name; }

    public abstract void open();
    public abstract void close();
    public abstract void lock();
    public abstract void unlock();
}
