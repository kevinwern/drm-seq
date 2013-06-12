import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class Cell extends JPanel implements MouseListener {

    boolean isLit;

    public Cell(){
        this.setBackground(Color.black);
        isLit = false;
        this.setSize(30,10);
        addMouseListener(this);
    }

    public void reset(){
        if(isLit)
            this.setBackground(Color.red);
        else
            this.setBackground(Color.black);
    }

    public void highlight(){
        if (isLit)
            this.setBackground(Color.yellow);
        else
            this.setBackground(Color.green);
    }

    public void mousePressed(MouseEvent e){

    }

    public void mouseReleased(MouseEvent e){
    
    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseExited(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){
        if (isLit)
            this.setBackground(Color.black);
        else
            this.setBackground(Color.red);
        isLit = !isLit;
    }

    public boolean isLit() {
        return isLit;
    }

}
