

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class Staff extends JPanel implements MouseListener, MetronomeListener{

    LinkedList<Row> rowList;
    Metronome metronome;
    GridBagConstraints constraints;

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
        rowList.add(new Row(fn, length));
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
