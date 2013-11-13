// ImageButton.java: interface for icon buttons

import javax.swing.JButton;
import javax.swing.ImageIcon;

class ImageButton extends JButton{
    public ImageButton(String filename){
        setIcon(new ImageIcon(filename));
    }
}
