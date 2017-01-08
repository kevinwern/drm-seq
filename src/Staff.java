

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Set;

class Staff extends JPanel implements MouseListener, MetronomeListener, XmlSerializable {

    LinkedList<Row> rowList;
    Set<Row> soloRows = new HashSet<Row>();
    Set<Row> mutedRows = new HashSet<Row>();

    Metronome metronome;

    public Staff(Metronome metronome){
        this.metronome = metronome;
        this.setBackground(Color.gray);
        rowList = new LinkedList<Row>();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        for (int i = 0; i <rowList.size(); i++){
            JPanel pane = new JPanel();
            pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            pane.setMinimumSize(new Dimension(400,25));
            pane.add(rowList.get(i));
            this.add(pane);
            rowList.get(i).addMouseListener(this);
        }
    }
    
    public void mousePressed(MouseEvent e){
        if (SwingUtilities.isRightMouseButton(e) && e.getSource() instanceof Row){
            Row source = (Row) e.getSource();
            this.remove(source.getParent());
            rowList.remove(source);
            this.revalidate();
            this.repaint();
        }
    }

    public void mouseReleased(MouseEvent e){
    }

    public void mouseEntered(MouseEvent e){
    }

    public void mouseExited(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){

    }

    public void selectToPlay() {
        metronome.RegisterObserver(this);
    }

    public void selectToStop() {
        metronome.UnregisterObserver(this);
    }
    
    public void reset(){
        for (int i = 0; i < rowList.size(); i++){
            rowList.get(i).reset();
        }
    }   

    public void addSound(String fn,int length){
        Row row = new Row(fn, length, this);
        rowList.add(new Row(fn, length, this));
        JPanel pane = new JPanel();
        pane.setBackground(Color.gray);
        pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        pane.setMinimumSize(new Dimension(400,25));
        pane.add(rowList.get(rowList.size()-1));
        this.add(pane);
        rowList.get(rowList.size()-1).addMouseListener(this);
        this.revalidate();
        this.repaint();
    }

    public void addRow(Row row) {
        rowList.add(row);
        JPanel pane = new JPanel();
        pane.setBackground(Color.gray);
        pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        pane.setMinimumSize(new Dimension(400,25));
        pane.add(rowList.get(rowList.size()-1));
        this.add(pane);
        rowList.get(rowList.size()-1).addMouseListener(this);
        this.revalidate();
        this.repaint();
    }
  
    public void setLength(int newLength){
        for (int i = 0; i<rowList.size(); i++){
            rowList.get(i).setLength(newLength);
        }
    }

    public int getLength(){
        if (rowList.size() != 0)
            return rowList.get(0).getLength();
        else
            return 0;
    }

    public void Clear() {
        this.removeAll();
        soloRows.clear();
        mutedRows.clear();
        rowList.clear();
    }

    public void trigger(int clickCount) {
        Iterable<Row> rowsToPlay = soloRows.isEmpty() ? rowList : soloRows;
        for (Row row: rowsToPlay) {
            row.play(clickCount);
        }
        for (Row row: rowList) {
            row.light(clickCount);
        }
    }

    public void addSoloTrack(Row row) {
        soloRows.add(row);
    }

    public void removeSoloTrack(Row row) {
        soloRows.remove(row);
    }

    public void addMutedTrack(Row row) {
        mutedRows.add(row);
    }

    public void removeMutedTrack(Row row) {
        mutedRows.remove(row);
    }

    @Override
    public Element toXmlElement(Document document) {
        Element element = null;
        element = document.createElement("staff");
        for (Row r : rowList) {
            element.appendChild(r.toXmlElement(document));
        }
        return element;
    }

    public static Staff fromXmlElement(Element element, Metronome metronome) {
        Staff staff = new Staff(metronome);

        NodeList rowNodes = element.getElementsByTagName("row");
        for (int i = 0; i < rowNodes.getLength(); i++) {
            Row newRow = Row.fromXmlElement((Element) rowNodes.item(i), metronome.GetClicksPerCycle(), staff);
            staff.addRow(newRow);
        }

        return staff;
    }
}