import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Color;

class Row extends JPanel  {

    LinkedList<Cell> cells;
    JLabel tag;
    int numCells;
    Sound sound;

    public Row(String filename, int length) {
        
        cells = new LinkedList<Cell>();
        for (int i = 0; i<length; i++){
            cells.add(new Cell());
        }

        numCells = length;
        sound = new Sound(filename);
        tag = new JLabel(filename);
        tag.setPreferredSize(new Dimension(100,10));
//        this.setBackground(Color.black);

        this.setPreferredSize(new Dimension(400,20));
        this.add(tag);
        for (int i = 0; i<length; i++){
            this.add(cells.get(i));
        }
    }

    public void setLength(int length) {
        if (length>numCells){
            for (int i=cells.size();i<length; i++){
                cells.add(new Cell());
            }
        }
        else if (length<numCells) {
            for (int i=cells.size();i<length; i++){
                cells.remove(cells.size()-1);
            }
        }
        numCells = length;
    }

    public int getLength(){
        return numCells;
    }

    public Cell getCell(int index){
        return cells.get(index);
    }

    public void light(int index){
        if (index==0){
            cells.get(index).highlight();
            cells.get(numCells-1).reset();
        }
        else{
            cells.get(index).highlight();
            cells.get(index-1).reset();
        }
        if (cells.get(index).isLit()) play();
    }

    public void play(){
        sound.play();
    }
}
