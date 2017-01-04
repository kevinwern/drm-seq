import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class MultiIntervalTimer implements ActionListener, Serializable {

    int intervals[];
    int intervalCount = 0;
    Timer timer;
    ActionListener listener;

    public MultiIntervalTimer(int[] intervals, ActionListener listener) {
        this.intervals = intervals;
        this.timer = new Timer(0, this);
        this.listener = listener;
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void reset() {
        intervalCount = 0;
    }

    public void updateIntervals(int... intervals) {
        this.intervals = intervals;
    }

    private int yieldInterval() {
        int interval = intervals[intervalCount];
        intervalCount = (intervalCount + 1) % intervals.length;
        return interval;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int interval = yieldInterval();
        timer.stop();
        timer.setInitialDelay(interval);
        timer.start();
        this.listener.actionPerformed(e);
    }
}