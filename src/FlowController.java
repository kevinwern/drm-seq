public class FlowController {

    private boolean isLocked = true;

    boolean isLocked() {
        return isLocked;
    }

    void Lock() {
        isLocked = true;
    }

    void Unlock() {
        isLocked = false;
    }
}
