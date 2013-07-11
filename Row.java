import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Row extends JPanel implements ActionListener{

    LinkedList<Cell> cells;
    int numCells;
    JButton muteButton,soloButton;
    boolean isMuted=false, isSoloed=false;
    Sound sound;
    String file;

    public Row(String filename, int length) {
        
        file = filename;
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

//        this.setBackground(Color.black);

        this.setPreferredSize(new Dimension(400,20));
        this.add(soloButton);
        this.add(muteButton);
        this.add(tag);
        for (int i = 0; i<length; i++){
            this.add(cells.get(i));
        }
    }

    public void setLength(int length) {
        if (length>numCells){
            for (int i=cells.size();i<length; i++){
                Cell addCell = new Cell();
                this.add(addCell);
                cells.add(addCell);
            }
        }
        else if (length<numCells) {
            for (int i=cells.size();i>length; i--){
                this.remove(cells.get(cells.size()-1));
                cells.remove(cells.size()-1);
            }
        }
        this.revalidate();
        this.repaint();
        numCells = length;
    }

    public int getLength(){
        return numCells;
    }

    public Cell getCell(int index){
        return cells.get(index);
    }

    public void light(int index){
        cells.get(index).highlight();
        if (index==0){
            cells.get(numCells-1).reset();
        }
        else{
            cells.get(index-1).reset();
        }
        if (cells.get(index).isLit() && !isMuted ) play();
    }

    void reset(){
        for (int i = 0; i<numCells; i++)
        cells.get(i).reset();
    }

    public void play(){
        sound.play();
    }

    public void toggleMute(){
        isMuted = !isMuted;
        if (isMuted ){
            muteButton.setForeground(Color.RED);
//            muteButton.setBorder(new LineBorder(Color.RED,1));
        }
        else{
            muteButton.setForeground(Color.BLACK);
//            muteButton.setBorder(new LineBorder(Color.BLACK,1));
        }
    }

    public void toggleSolo(){
        isSoloed = !isSoloed;
        if (isSoloed)
            soloButton.setForeground(Color.YELLOW);
        else
            soloButton.setForeground(Color.BLACK);
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource().equals(muteButton))
            toggleMute();
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
        String dumpedString = "Samples/"+file;
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
