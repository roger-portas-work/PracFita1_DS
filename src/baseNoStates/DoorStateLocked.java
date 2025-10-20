package baseNoStates;

/** State: Locked. Can't open until unlocked. Closing is allowed (idempotent). */
public class DoorStateLocked extends DoorState {
    public DoorStateLocked(Door door) {
        super(door, "locked");
    }

    @Override
    public void open() {
        // Can't open while locked (no changes)
    }

    @Override
    public void close() {
        // Ensure physically closed
        door.setClosed(true);
    }

    @Override
    public void lock() {
        // Already locked; nothing to do
    }

    @Override
    public void unlock() {
        door.setState(new DoorStateUnlocked(door));
    }
}
