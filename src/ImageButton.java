// ImageButton.java: interface for icon buttons

import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.*;

class ImageButton extends JButton{
    public static int BUTTON_SIDE_LENGTH = 20;

    public ImageButton(String filename){
        setIcon(new ImageIcon(filename));
        setSize(new Dimension(BUTTON_SIDE_LENGTH, BUTTON_SIDE_LENGTH));
    }
}
