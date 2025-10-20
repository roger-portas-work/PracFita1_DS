package baseNoStates;

import java.util.Timer;
import java.util.TimerTask;

/** State: UnlockedShortly. Temporarily unlocked (≈10 s) before re-locking or propped. */
public class DoorStateUnlockedShortly extends DoorState {
    public DoorStateUnlockedShortly(Door door) {
        super(door, "unlocked_shortly");
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (door.isClosed()) {
                    door.setState(new DoorStateLocked(door)); // closed → lock again
                } else {
                    door.setState(new DoorStatePropped(door)); // still open → propped
                }
            }
        }, 10_000); // 10 s (milisegundos)
    }

    @Override public void open()  { door.setClosed(false); }
    @Override public void close() { door.setClosed(true); }
    @Override public void lock()  { door.setState(new DoorStateLocked(door)); }
    @Override public void unlock() { door.setState(new DoorStateUnlocked(door)); }
}
