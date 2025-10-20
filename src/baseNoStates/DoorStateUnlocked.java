package baseNoStates;

/** State: Unlocked. Allows opening/closing. Can lock only if door is physically closed. */
public class DoorStateUnlocked extends DoorState {
    public DoorStateUnlocked(Door door) {
        super(door, "unlocked");
    }

    @Override
    public void open() {
        // Unlocked -> allow opening (physically)
        door.setClosed(false);
    }

    @Override
    public void close() {
        // Close physically
        door.setClosed(true);
    }

    @Override
    public void lock() {
        // Policy: only lock if door is physically closed
        if (door.isClosed()) {
            door.setState(new DoorStateLocked(door));
        }
        // If it's open, do nothing (podríamos cambiar la política si quisieras)
    }

    @Override
    public void unlock() {
        // Already unlocked; nothing to do
    }
}
