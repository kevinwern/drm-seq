// Kevin Wern  DRM-SEQ
// Cell.java
// Defines list of features for each clickable cell

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

class Cell extends JPanel implements MouseListener {

    boolean isLit;    /* Defines if a cell is selected or not */

    public Cell(){
        this.setBackground(Color.black);
        isLit = false;
        this.setSize(30,10);
        addMouseListener(this);
    }

    public void reset(){  /* Set cell color back to unplayed color */
        if(isLit)
            this.setBackground(Color.red);
        else
            this.setBackground(Color.black);
    }

    public void highlight(){   /* Set cell color to played color */
        if (isLit)
            this.setBackground(Color.yellow);
        else
            this.setBackground(Color.green);
    }

    public void deactivate() {
        if (isLit) {
            this.setBackground(Color.lightGray);
        }
        else {
            this.setBackground(Color.darkGray);
        }
    }

    public void activate() {
        if (isLit) {
            this.setBackground(Color.red);
        }
        else {
            this.setBackground(Color.black);
        }
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
        toggleLight();
    }

    public void toggleLight(){  /* Toggle between clicked and unclicked states */
        if (isLit)
            this.setBackground(Color.black);
        else
            this.setBackground(Color.red);
        isLit = !isLit;
    }

    public boolean isLit() { /* Helper function to return whether a cell is clicked */
        return isLit;
    }
}
