import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Row extends JPanel implements ActionListener {
    LinkedList<Cell> cells;
    int numCells;
    int litCell = -1;
    JButton muteButton, soloButton;
    boolean isMuted = false, isSoloed = false;
    Sound sound;
    String file;
    Staff parent;

    public Row(String filename, int length, Staff parent) {

        file = filename;
        this.parent = parent;
        cells = new LinkedList<Cell>();
        for (int i = 0; i<length; i++){
            cells.add(new Cell());
        }

        numCells = length;
        sound = new Sound(filename);
        String[] strFragments = filename.split("/");
        JLabel tag = new JLabel(strFragments[strFragments.length-1],JLabel.CENTER);
        tag.setPreferredSize(new Dimension(100,10));
        muteButton = new JButton("M");
        muteButton.setPreferredSize(new Dimension(15,15));
        muteButton.setFocusable(false);
        muteButton.addActionListener(this);
        soloButton = new JButton("S");
        soloButton.setPreferredSize(new Dimension(15,15));
        soloButton.setFocusable(false);
        soloButton.addActionListener(this);

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.setMinimumSize(new Dimension(400,20));
        this.add(soloButton);
        this.add(muteButton);
        this.add(tag);
        for (int i = 0; i<length; i++){
            this.add(cells.get(i));
        }
    }

    public void setLength(int length) {
        if (length>numCells){
            for (int i = numCells; i<length; i++){
                if (i < cells.size()) {
                    cells.get(i).activate();
                }
                else {
                    Cell addCell = new Cell();
                    this.add(addCell);
                    cells.add(addCell);
                }
            }
        }
        else if (length<numCells) {
            boolean activeDataExists = false;
            for (int i=cells.size() - 1;i >= length; i--){
                if (cells.get(i).isLit()) {
                    activeDataExists = true;
                }
                if (activeDataExists) {
                    cells.get(i).deactivate();
                }
                else {
                    this.remove(cells.get(i));
                    cells.remove(i);
                }
            }
        }
        this.revalidate();
        this.repaint();
        numCells = length;
    }

    public int getLength(){
        return numCells;
    }

    public void light(int index){
        if (index >= cells.size()) {
            return;
        }
        cells.get(index).highlight();
        if (litCell >= 0) {
            cells.get(litCell).reset();
        }
        litCell = index;;
    }

    void reset(){
        for (int i = 0; i<numCells; i++)
            cells.get(i).reset();
        litCell = -1;
    }

    public void play(int index) {
        if (index < cells.size() && cells.get(index).isLit() && !isMuted)
            sound.play();
    }

    public void toggleMute(){
        isMuted = !isMuted;
        if (isMuted){
            parent.addMutedTrack(this);
            muteButton.setForeground(Color.RED);
        }
        else{
            parent.removeMutedTrack(this);
            muteButton.setForeground(Color.BLACK);
        }
    }

    public void toggleSolo(){
        isSoloed = !isSoloed;
        if (isSoloed) {
            parent.addSoloTrack(this);
            soloButton.setForeground(Color.YELLOW);
        }
        else {
            parent.removeSoloTrack(this);
            soloButton.setForeground(Color.BLACK);
        }
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource().equals(muteButton)) {
            toggleMute();
        }
        if (e.getSource().equals(soloButton))
            toggleSolo();
    }

    public boolean isMuted(){
        return isMuted;
    }

    public boolean isSoloed(){
        return isSoloed;
    }

    public String dumpString(){
        String dumpedString = file;
        if (isMuted) dumpedString += " M";
        else dumpedString += " m";

        if (isSoloed) dumpedString += "S ";
        else dumpedString += "s ";

        for (int i = 0; i<numCells; i++){
            if (cells.get(i).isLit())
                dumpedString +="1";
            else
                dumpedString +="0";
        }
        return dumpedString;
    }
}
