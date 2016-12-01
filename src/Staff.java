

import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.LinkedList;
import java.awt.Color;
import javax.swing.SwingUtilities;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class Staff extends JPanel implements MouseListener, MetronomeListener{

    LinkedList<Row> rowList;
    Metronome metronome;

    public Staff(Metronome metronome){
        this.metronome = metronome;
        this.setBackground(Color.gray);
        rowList = new LinkedList<Row>();
        this.setPreferredSize(new Dimension(400,400));
        this.setSize(400,400);

        for (int i = 0; i <rowList.size(); i++){
            this.add(rowList.get(i));
            rowList.get(i).addMouseListener(this);
        }
    }
    
    public void mousePressed(MouseEvent e){
        if (SwingUtilities.isRightMouseButton(e)){
            this.remove((Row) e.getSource());
            rowList.remove(e.getSource());
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
        rowList.add(new Row(fn, length));
        this.add(rowList.get(rowList.size()-1));
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

    public String dumpString(){
        String dumpString="";
        for (int i = 0; i<rowList.size(); i++){
            dumpString += rowList.get(i).dumpString() + "\n";
        }
        return dumpString;
    }

    public void trigger(int clickCount) {
        for (Row row: rowList) {
            row.play(clickCount);
        }
        for (Row row: rowList) {
            row.light(clickCount);
        }
    }

}
