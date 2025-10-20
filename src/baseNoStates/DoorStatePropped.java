package baseNoStates;

/** State: Propped. Door left open too long. Closes → Locked. */
public class DoorStatePropped extends DoorState {
    public DoorStatePropped(Door door) {
        super(door, "propped");
    }

    @Override
    public void open() {
        // already open; no change
    }

    @Override
    public void close() {
        // when someone closes it → back to Locked
        door.setClosed(true);
        door.setState(new DoorStateLocked(door));
    }

    @Override
    public void lock() {
        // ignore; will lock when closed
    }

    @Override
    public void unlock() {
        door.setState(new DoorStateUnlocked(door));
    }
}
