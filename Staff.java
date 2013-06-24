import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.LinkedList;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import javax.swing.Timer;
import java.net.MalformedURLException;

class Staff extends JPanel implements ActionListener{

    int vert,horiz,count;
    Timer time;
    LinkedList<Row> rowList;

    public Staff(){

        count = 0;
        vert = 3;
        horiz = 16;
        this.setBackground(Color.gray);
        rowList = new LinkedList<Row>();

        rowList.add(new Row("Samples/kick.wav",16));
//        rowList.add(new Row("snare.wav",16));
//        rowList.add(new Row("hihat.wav",16));
//        rowList.add(new Row("openhat.wav",16));

        this.setPreferredSize(new Dimension(400,400));
        this.setSize(400,400);

        for (int i = 0; i<rowList.size(); i++){
            this.add(rowList.get(i));
        }
    }
    
    public void actionPerformed(ActionEvent e){
    
    }
    
    public void light(int index){
        for (int i = 0; i < rowList.size(); i++){
            rowList.get(i).light(index);
        }
    }

    public void reset(){
        for (int i = 0; i < rowList.size(); i++){
            rowList.get(i).reset();
        }
    }   

    public void addSound(String fn){
        rowList.add(new Row(fn,16));
        this.add(rowList.get(rowList.size()-1));
        this.revalidate();
        //this.repaint();
    }
  
    public int getLength(){
        return rowList.get(0).getLength();
    }

}
