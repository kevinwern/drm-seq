import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.util.ArrayList;

class Staffs extends JPanel {

    public static final int PANEL_LENGTH = 400;
    public static final int PANEL_HEIGHT = 400;
    ArrayList<Staff> staffList;
    Staff playingStaff, showingStaff;
    CardLayout cardLayout;
    Metronome metronome;

    Staffs(Metronome metronome, int total, int selected) {
        this.metronome = metronome;
        cardLayout = new CardLayout();
        this.setPreferredSize(new Dimension(PANEL_LENGTH, PANEL_HEIGHT));
        this.setLayout(cardLayout);
        this.staffList = new ArrayList<Staff>();
        Initialize(total);
        ShowById(selected);
        PlayById(selected);
    }

    public void ShowById(int id) {
        this.cardLayout.show(this, Integer.toString(id));
        this.showingStaff = this.staffList.get(id);
    }

    public void PlayById(int id) {
        Staff newPlayingStaff = staffList.get(id);
        if (playingStaff != null) {
            playingStaff.selectToStop();
            ResetCurrent();
        }
        playingStaff = newPlayingStaff;
        playingStaff.selectToPlay();
    }

    public void ResetCurrent() {
        playingStaff.reset();
    }

    public Staff GetCurrentPlaying() {
        return playingStaff;
    }

    public Staff GetCurrentShowing() {
        return showingStaff;
    }

    public void Clear() {
        int total = staffList.size();
        ResetCurrent();
        this.removeAll();
        this.staffList.clear();
        Initialize(total);
        ShowById(0);
        PlayById(0);
    }

    public void Initialize(int total) {
        for (int i = 0; i < total; i++){
            Staff newStaff = new Staff(this.metronome);
            this.staffList.add(newStaff);
            this.add(newStaff, Integer.toString(i));
        }
    }
}
