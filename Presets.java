import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.util.LinkedList;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

class Presets extends JPanel implements ActionListener{

    LinkedList<Preset> presets;
    int selection;
    int current;
    int num;
    
    public Presets(){
        Initialize();
    }
    public void Initialize(){
        num = 8;
        presets = new LinkedList<Preset>();
        this.setLayout(new GridLayout(4,2));
        selection = 1;
        for (int i = 0; i<num; i++){
            presets.add(new Preset(i+1));
            this.add(presets.get(i));
            presets.get(i).addActionListener(this);
        }
        
    }
    public void actionPerformed(ActionEvent e){
        int newId = ((Preset) e.getSource()).getId();
        System.out.println(newId);
        selection = newId;
    }

    public int getSelection(){
        return selection;
    }
}
