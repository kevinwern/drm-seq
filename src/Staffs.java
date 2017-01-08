import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

class Staffs extends JPanel implements XmlSerializable {

    public static final int PANEL_LENGTH = 400;
    public static final int PANEL_HEIGHT = 400;
    int totalStaffs = 0;
    ArrayList<Staff> staffList;
    Staff playingStaff, showingStaff;
    CardLayout cardLayout;
    Metronome metronome;

    Staffs(Metronome metronome, int total, int selected) {
        this.metronome = metronome;
        this.totalStaffs = total;
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.staffList = new ArrayList<Staff>();
        Initialize();
        if (selected >= 0) {
            ShowById(selected);
            PlayById(selected);
        }
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
        this.removeAll();
        this.staffList.clear();
    }

    public void Initialize() {
        for (int i = 0; i < totalStaffs; i++){
            Staff newStaff = new Staff(this.metronome);
            this.staffList.add(newStaff);
            this.add(newStaff, Integer.toString(i));
        }
    }

    public void addStaff(Staff staff) {
        this.add(staff, Integer.toString(staffList.size()));
        staffList.add(staff);
    }

    public void showDefaultStaff() {
        ShowById(0);
        PlayById(0);
    }

    @Override
    public Element toXmlElement(Document document) {
        Element element = null;
        element = document.createElement("staffs");
        for (Staff s: staffList) {
            element.appendChild(s.toXmlElement(document));
        }
        return element;
    }

    public static Staffs fromXmlElement(Element element, Metronome metronome) {
        NodeList staffElements = element.getElementsByTagName("staff");

        Staffs staffs = new Staffs(metronome, 0, -1);
        staffs.totalStaffs = staffElements.getLength();
        for (int i = 0; i < staffElements.getLength(); i++) {
            staffs.addStaff(Staff.fromXmlElement((Element) staffElements.item(i), metronome));
        }
        return staffs;
    }
}
