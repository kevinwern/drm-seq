

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.util.LinkedList;
import java.awt.GridLayout;

class Presets extends JPanel implements ActionListener{

    LinkedList<Preset> presets;
    Staffs staffs;
    
    public Presets(Staffs staffs, int total){
        this.staffs = staffs;
        presets = new LinkedList<Preset>();
        this.setLayout(new GridLayout(4,2));
        for (int i = 0; i < total; i++){
            presets.add(new Preset(i+1));
            this.add(presets.get(i));
            presets.get(i).addActionListener(this);
        }
    }

    public void actionPerformed(ActionEvent e){
        int newId = ((Preset) e.getSource()).getId();
        this.staffs.ShowById(newId - 1);
    }
}
