/* Preset.java: Define right hand preset buttons
*  Kevin Wern DRM-SEQ
*/

import javax.swing.JButton;

class Preset extends JButton{

    int id;
    public Preset(int i){     // Create presets
        id = i;
        this.setSize(50,50);
        this.setText(i + "");
        this.setFocusable(false);
    }
    public int getId(){ // Return button's ID
        return id;
    }
}
