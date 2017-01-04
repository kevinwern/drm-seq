// Metronome: an observable class that emits "beat" events, designed to
// encapsulate conversion steps between GUI control values and final
// interval values.
//
// Components will update this class's attributes and listen to the
// resulting pulse.

import javax.swing.Timer;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.IllegalArgumentException;

public class Metronome implements ActionListener, Serializable {
    public static final int  MAX_BPM = 240;
    public static final int  MIN_BPM = 30;
    public static final int  MAX_BEAT_COUNT = 12;
    public static final int  MIN_BEAT_COUNT = 1;
    public static final double MIN_SWING_FACTOR = 0.0;
    public static final double MAX_SWING_FACTOR = 1.0;
    public static final int BASIC_DURATION_EIGHT = 8;
    public static final int BASIC_DURATION_FOUR = 4;
    private static final int INITIAL_BPM = 120;
    private static final int INITIAL_CLICK = 0;
    private static final double INITIAL_SWING_FACTOR = .5;
    private static final int INITIAL_BASIC_DURATION = 4;
    private static final int INITIAL_BEAT_COUNT = 4;
    private static final int MILLISECONDS_PER_MINUTE = 60000;

    /* The current click in the cycle */
    private int currentClick;
    /* Beats per cycle ('3' in 3/4 time) */
    private int beatCount;
    /* Length of each beat ('4' in 3/4 time) */
    private int basicDuration;
    /* Beats per minute */
    private int beatsPerMinute;
    /* End number of clicks in a loop */
    private int clicksPerCycle;
    /* For every two clicks, the fraction of time taken by the first click */
    private double swingFactor;
    /* The two alternating interval lengths */
    private TimerIntervalGroup clickIntervals;
    private MultiIntervalTimer timer;
    private ArrayList<MetronomeListener> registeredObservers = new ArrayList<MetronomeListener>();

    public Metronome() {
        beatCount = INITIAL_BEAT_COUNT;
        basicDuration = INITIAL_BASIC_DURATION;
        beatsPerMinute = INITIAL_BPM;
        swingFactor = INITIAL_SWING_FACTOR;
        currentClick = INITIAL_CLICK;
        clickIntervals = CalculateIntervals();
        clicksPerCycle = CalculateClicksPerCycle();
        timer = new MultiIntervalTimer(clickIntervals.GetIntervals(), this);
    }

    public int GetBeatCount() {
        return this.beatCount;
    }

    public int GetBeatsPerMinute() {
        return this.beatsPerMinute;
    }

    public int GetBasicDuration() {
        return this.basicDuration;
    }

    public double GetSwingFactor() {
        return this.swingFactor;
    }

    public int GetClicksPerCycle(){
        return this.clicksPerCycle;
    }

    public void actionPerformed(ActionEvent e) {
        NotifyObservers();
        currentClick++;
        if (currentClick >= clicksPerCycle) {
            currentClick = INITIAL_CLICK;
        }
    }

    public void Update(int beatCount, int basicDuration, int beatsPerMinute,
            double swingFactor) {
        if (beatCount > MAX_BEAT_COUNT || beatCount < MIN_BEAT_COUNT) {
            throw new IllegalArgumentException(String.format("beatCount must be in range [1,12]. Got %d",
                    beatCount));
        }
        if (basicDuration != BASIC_DURATION_FOUR && basicDuration != BASIC_DURATION_EIGHT) {
            throw new IllegalArgumentException(String.format("basicDuration must be 4 or 8. Got %d",
                    basicDuration));
        }
        if (beatsPerMinute > MAX_BPM || beatsPerMinute < MIN_BPM) {
            throw new IllegalArgumentException(String.format("beatsPerMinute must be in range [1,255]. Got %d",
                   beatsPerMinute));
        }
        if (swingFactor > MAX_SWING_FACTOR || swingFactor < MIN_SWING_FACTOR) {
            throw new IllegalArgumentException(String.format("swingFactor must be in range [0,1]. Got %f",
                   swingFactor));
        }
    
        this.beatCount = beatCount;
        this.basicDuration = basicDuration;
        this.beatsPerMinute = beatsPerMinute;
        this.swingFactor = swingFactor;
        this.clicksPerCycle = CalculateClicksPerCycle();
        this.clickIntervals = CalculateIntervals();
        this.timer.updateIntervals(this.clickIntervals.GetIntervals());
    }

    public void RegisterObserver(MetronomeListener observer) {
        registeredObservers.add(observer);
    }

    public void UnregisterObserver(MetronomeListener observer) {
        registeredObservers.remove(observer);
    }

    public void NotifyObservers() {
        for (MetronomeListener observer : registeredObservers){
            observer.trigger(currentClick);
        }
    }

    public void Start() {
        timer.start();
    }

    public void Stop() {
        timer.stop();
    }

    public void Reset() {
        timer.stop();
        timer.reset();
        currentClick = INITIAL_CLICK;
        timer.start();
    }

    private TimerIntervalGroup CalculateIntervals() {
        int beatLengthMilliseconds = MILLISECONDS_PER_MINUTE/beatsPerMinute;
        int baseInterval = beatLengthMilliseconds;
        if (basicDuration == BASIC_DURATION_FOUR) {
            baseInterval = beatLengthMilliseconds/2;
        }
        else if (basicDuration == BASIC_DURATION_EIGHT) {
            baseInterval = beatLengthMilliseconds/4;
        }
        else {
            // Should never happen
            throw new IllegalArgumentException(String.format("basicDuration must be 4 or 8. Got %d",
                    basicDuration));
        }
        return new TimerIntervalGroup(
                (int) (baseInterval * swingFactor),
                (int) (baseInterval * (1 - swingFactor))
        );
    }

    private int CalculateClicksPerCycle() {
        if (basicDuration == BASIC_DURATION_FOUR) {
            return beatCount * 4;
        }
        else if (basicDuration == BASIC_DURATION_EIGHT) {
            return beatCount * 2;
        }
        else {
            // Should never happen
            throw new IllegalArgumentException(String.format("basicDuration must be 4 or 8. Got %d",
                    basicDuration));
        }
    }

    // Represents the alternating intervals, taking swing factor into account.
    private class TimerIntervalGroup implements Serializable {
        int intervalOnBeat;
        int intervalOffBeat;

        private TimerIntervalGroup(int intervalOnBeat, int intervalOffBeat){
            this.intervalOnBeat = intervalOnBeat;
            this.intervalOffBeat = intervalOffBeat;
        }

        int[] GetIntervals() { return new int[] {intervalOnBeat, intervalOffBeat}; }
    }
}
