/***--------------------------------------------------------------------------------------------*  
DRM SEQ:                                                                                        |
Author: Kevin Wern                                                                              |
I'm a DJ.  I hate paying for software. I wrote this mainly for myself, but you can have it too. |
                                                                                                |
This is intended to be a "live sequencer"--it's just a compilation of ideas I had as I began to |
think of ways to make convenient music software for what I do.  I normally use it as a way to   |
take samples and use clips to transition/expand on a song.                                      |
                                                                                                |
These hotkeys are 100% written for the way I think, feel free to modify them.  If you have any  |
suggestions/fixes, let me know.                                                                 |
                                                                                                |
I think software like this lends very well to contributions.  It's fairly small, modular, and   |
anything I think of that wasn't convenient from a night before can be added in--I had a lot     |
of fun designing file formats, hotkey schemes, and fixing the other things I hated about open   |
software mixers.                                                                                |
                                                                                                |
---------------------------------------------------------------------------------------------***/

//Sequencer.java: contains the main window and options, as well as the basic "tree" of how things work

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.LinkedList;
import java.util.HashMap;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener{

    LinkedList<Staff> staffList;                                       // Each of the staves for the 8 preset banks
    JSpinner BPM,Groove;                                               // Groove and BPM spinners
    JTextField beatCt, basDur;                                         // Time signature
    CardLayout cardMan;
    JPanel center, topLabel,btmProperties;
    JComboBox fileChoose;                                              // File ComboBox
    JButton loadSound;                                                 // Load Sound button
    JScrollPane scrollWindow;
    fileMenu menuBar;
    Timer time;
    Presets pre;                                                       
    int basicDuration,count,beatCount,bpm,groove,playSelect,staffSelect;
//    HashMap<int,int[]> hotKeys;



    public Sequencer(){  //Default constructor
        Initialize();
    }

    public Sequencer(String fn){      //Constructor for new files
        File baseFile = new File(fn);
    }

    public void Initialize(){

        count = 0;                        /* Starts at beginning of sample with bank 1 selected. */
        playSelect=0;       
        staffSelect=0;
        staffList = new LinkedList<Staff>();

        for (int i = 0; i<8;i++){
            staffList.add(new Staff());   /* Initialize staves*/
        }
	topLabel = new JPanel();          /* Top UI */
	topLabel.setSize(100,100);
        JLabel bpmLab = new JLabel("BPM");
	JLabel timeSigLab = new JLabel("Time Signature");
        JLabel grooveLab = new JLabel("Groove (%):");
        SpinnerNumberModel spinNum = new SpinnerNumberModel(120,1,240,1);
        SpinnerNumberModel grooveNum = new SpinnerNumberModel(50,1,100,1);
        BPM = new JSpinner(spinNum);
        Groove = new JSpinner(grooveNum);
	JLabel slash= new JLabel("/");
        beatCt = new JTextField("4",1);
        basDur = new JTextField("4",1);

        pre = new Presets();  /*Right preset bank*/

        loadSound = new JButton("Load Sound");                  /* Initialize Load Sound button*/
        loadSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                staffList.get(staffSelect).addSound(fileChoose.getSelectedItem().toString(),staffList.get(staffSelect).getLength());
            }
        });
  
        File temp = new File("Samples");    /* Samples Folder (located in %CHOSEN_DIRECTOR%/Samples) */

        fileChoose = new JComboBox(temp.listFiles());
        bpm = Integer.parseInt(BPM.getValue().toString());
        time=new Timer(60000/480,this);
        time.start();
        
        cardMan = new CardLayout();
        center = new JPanel(cardMan);
        center.setPreferredSize(new Dimension(400,400));
        for (int i = 0; i <8; i++){
            center.add(staffList.get(i),""+i);
        }
//	center.setPreferredSize(new Dimension(200,200));
        topLabel.add(bpmLab);
	topLabel.add(BPM);
        topLabel.add(grooveLab);
        topLabel.add(Groove);
        topLabel.add(timeSigLab);
	topLabel.add(beatCt);
        topLabel.add(slash);
        topLabel.add(basDur);

        btmProperties = new JPanel();
        btmProperties.add(fileChoose);
        btmProperties.add(loadSound);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setTitle("DRM SEQ");
        this.setLayout(new BorderLayout());
        this.add(center,BorderLayout.CENTER);
	this.add(topLabel,BorderLayout.NORTH);
        this.add(btmProperties,BorderLayout.SOUTH);
        this.add(pre,BorderLayout.EAST);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
        this.setFocusable(true); //Allow JPanel to be focused on (to avoid unfocusing for hotkeys)
    }

    public void actionPerformed(ActionEvent e){
        if (bpm!= Integer.parseInt(BPM.getValue().toString())){
            bpm = Integer.parseInt(BPM.getValue().toString());
        }
        if (groove != Integer.parseInt(Groove.getValue().toString())){
            groove = Integer.parseInt(Groove.getValue().toString());
        }
        if (staffSelect != pre.getSelection()-1){
            staffSelect = pre.getSelection()-1;
            cardMan.show(center,staffSelect+"");
        }

        if (!beatCt.getText().isEmpty()){
            if  (Integer.parseInt( basDur.getText() ) == 4 || Integer.parseInt( basDur.getText() ) == 8){
                beatCount = Integer.parseInt(beatCt.getText());
                basicDuration = Integer.parseInt(basDur.getText());
                if (basicDuration == 4){
                    for (int i=0; i<8; i++){
                        staffList.get(i).setLength(beatCount*4);
                    }
                }
                else if (basicDuration == 8){
                    for (int i=0;i<8; i++){
                        staffList.get(i).setLength(beatCount*2);
                    }
                }
            }
            if (beatCount != Integer.parseInt( beatCt.getText() )){
                basDur.setText("");
                basDur.requestFocus();
            }
        }        

        staffList.get(playSelect).light(count);
        if (count >= staffList.get(playSelect).getLength()-1) count = 0;
        else count++;
        if (basicDuration == 4){
            if (count % 2 == 0){
                time.setDelay((int)((60000.0/(bpm*2.0))*(.01*groove)));
            }
            else
                 time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(100-groove))));
        }
        else if (basicDuration == 8) {
            if (count % 3 == 2){
                time.setDelay((int)((60000.0/(bpm*3))*(.01*groove)));
            }
            else
                 time.setDelay((int)((60000.0/(bpm*3))*(.01*(100-groove))));
        }
    }

    public void keyPressed(KeyEvent k){
        staffList.get(playSelect).reset();
        if (k.getKeyCode() == KeyEvent.VK_SPACE){
            time.stop();
            staffList.get(playSelect).light(0);
            count=1;
            time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(groove))));
            time.start();
        }
        
        if (k.getKeyCode() == KeyEvent.VK_8)
            playSelect = 7;
        if (k.getKeyCode() == KeyEvent.VK_7)
            playSelect = 6;
        if (k.getKeyCode() == KeyEvent.VK_6)
            playSelect = 5;
        if (k.getKeyCode() == KeyEvent.VK_5)
            playSelect = 4;
        if (k.getKeyCode() == KeyEvent.VK_4)
            playSelect = 3;
        if (k.getKeyCode() == KeyEvent.VK_3)
            playSelect = 2;
        if (k.getKeyCode() == KeyEvent.VK_2)
            playSelect = 1;
        if (k.getKeyCode() == KeyEvent.VK_1)
            playSelect = 0;

        if (k.getKeyCode() == KeyEvent.VK_EQUALS){
             BPM.setValue(Integer.parseInt(BPM.getValue().toString())+1);
        }

        if (k.getKeyCode() == KeyEvent.VK_MINUS){
            BPM.setValue(Integer.parseInt(BPM.getValue().toString())-1);
        }

    }

    public void keyReleased(KeyEvent k){

    }

    public void keyTyped(KeyEvent k){

    }

    public void mousePressed(MouseEvent e){
        requestFocus();
    }

    public void mouseReleased(MouseEvent e){

    }

    public void mouseEntered(MouseEvent e){
        requestFocus();
    }

    public void mouseExited(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){

    }

    public static void main(String[] args){
        Sequencer seq = new Sequencer();
    }
}
