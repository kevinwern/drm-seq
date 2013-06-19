import javax.swing.JButton;

class Preset extends JButton{

    int id;
    public Preset(int i){
        id = i;
        this.setSize(50,50);
        this.setText(i + "");
    }
    public int getId(){
        return id;
    }
}
