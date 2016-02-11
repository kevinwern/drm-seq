/***--------------------------------------------------------------------------------------------*  
DRM SEQ:                                                                                        |
Author: Kevin Wern                                                                              |
---------------------------------------------------------------------------------------------***/

//Sequencer.java: contains the main window and options, and establishes upper hierarchy of editing window

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener {

    LinkedList<Staff> staffList;                                       // Each of the staves for the 8 preset banks
    JSpinner BPM,Groove;                                               // Groove and BPM spinners
    JTextField beatCt, basDur;                                         // Time signature
    CardLayout cardMan;
    JPanel center, topLabel,btmProperties;
    JComboBox fileChoose;                                              // File ComboBox
    JScrollPane scrollWindow;
    FileMenu menuBar;
    Presets pre;                                                       
    int playSelect,staffSelect;  // Integers for determining play times
    Metronome metronome;


    public Sequencer() {  //Default constructor
        Initialize();
    }

    public Sequencer(String fn){      // Constructor for new files
        File baseFile = new File(fn);
    }

    public void Initialize() {
        playSelect=0;                       /* Starts at beginning of sample with bank 1 selected. */
        staffSelect=0;
        staffList = new LinkedList<Staff>();
        metronome = new Metronome();

        for (int i = 0; i<8;i++){
            staffList.add(new Staff(this.metronome));   /* Initialize staves*/
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

        JButton loadSound = new JButton("Load Sound");                  /* Initialize Load Sound button */
        loadSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                int length = metronome.GetClicksPerCycle();
                staffList.get(staffSelect).addSound(fileChoose.getSelectedItem().toString(),length);
            }
        });

        ImageButton playButton = new ImageButton("Images/play.png");   //play, pause, and stop buttons
        playButton.setPreferredSize(new Dimension(20,20));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                metronome.Start();
            }
        });

        ImageButton pauseButton = new ImageButton("Images/pause.png");
        pauseButton.setPreferredSize(new Dimension(20,20));
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                metronome.Stop();
            }
        });


        ImageButton stopButton = new ImageButton("Images/stop.png");
        stopButton.setPreferredSize(new Dimension(20,20));
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                metronome.Stop();
                staffList.get(playSelect).reset();
            }
        });  

        File temp = new File("Samples");    /* Samples Folder (located in %CHOSEN_DIRECTORY%/Samples) */

        fileChoose = new JComboBox(temp.listFiles());
        
        cardMan = new CardLayout();
        center = new JPanel(cardMan);                      // Lays out 8 banks for user
        center.setPreferredSize(new Dimension(400,400));
        for (int i = 0; i <8; i++){
            center.add(staffList.get(i),""+i);
        }

        menuBar = new FileMenu();

        topLabel.add(bpmLab);
        topLabel.add(BPM);
        topLabel.add(grooveLab);
        topLabel.add(Groove);
        topLabel.add(timeSigLab);
        topLabel.add(beatCt);
        topLabel.add(slash);
        topLabel.add(basDur);

        btmProperties = new JPanel();
        btmProperties.add(playButton);
        btmProperties.add(pauseButton);
        btmProperties.add(stopButton);
        btmProperties.add(fileChoose);
        btmProperties.add(loadSound);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setJMenuBar(menuBar);
        this.setTitle("DRM SEQ");
        this.setLayout(new BorderLayout());
        this.add(center,BorderLayout.CENTER);
        this.add(topLabel,BorderLayout.NORTH);
        this.add(btmProperties,BorderLayout.SOUTH);
        this.add(pre,BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
        this.setFocusable(true); //Allow JPanel to be focused on (to avoid unfocusing for hotkeys)

        metronome.Start();
    }

    public void createTopLabel(){

    }

    public void createBottomLabel(){
        
    }

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("about")){  /* Menu actions */
            showDialog();
        }
        else if (e.getActionCommand().equals("save")){
            saveFile();
        }
        else if (e.getActionCommand().equals("open")){
            openFile();
        }
        else if (e.getActionCommand().equals("new")){
            startNew();
        }
    }

    public void startNew(){
        metronome.Stop();
        BPM.setValue(120);
        Groove.setValue(50);
        basDur.setText(""+4);
        beatCt.setText(""+4);
        for (int i = 0; i<8; i++){
            center.remove(staffList.get(i));
        }
        staffList.clear();
        for (int i = 0; i<8; i++){
            staffList.add(new Staff(this.metronome));
            center.add(staffList.get(i),""+i);
        }
        metronome.Start();
    }

    public void showDialog(){   /* Help menu */
        JOptionPane.showMessageDialog(this, 
        "Created January and July 2013\n\n"+
        "Some quick controls:\n" +
        "Spacebar:\t\t\tStart from first beat.\n"+
        "+/-:\t\t\tIncrease/Decrease BPM\n" +
        "Right Click (on any Row): Delete that row\n"+
        "1-8:                      Play corresponding bank from current point in measure\n\n"+
        "Other helpful button info:\n"+
        "Mute (M):                 Mute the given track\n"+
        "Solo (S):                 Play only the soloed track(s)\n"+
        "Play/Pause/Stop:          Seek through loop as desired\n",
        "About DRM SEQ",
        JOptionPane.INFORMATION_MESSAGE);
    }

    public void saveFile(){
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.addChoosableFileFilter(new FileNameExtensionFilter("drm","drm"));
        int result=saveDialog.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            writeFile(saveDialog.getSelectedFile());
        }
    }

    public void writeFile(File f){   /* save file */
        if (!f.exists()) {
            int beatsPerMinute = metronome.GetBeatsPerMinute();
            double swingFactor = metronome.GetSwingFactor();
            int beatCount = metronome.GetBeatCount();
            int basicDuration = metronome.GetBasicDuration();

            String toWrite = beatsPerMinute + " " + swingFactor + " " + beatCount + " " + basicDuration + "\n";
            for (int i = 0; i < staffList.size(); i++){
                toWrite+=i + "\n" + staffList.get(i).dumpString(); /* Dump string for each staff bank */
            }
            
            try
            {
                FileWriter o1 = new FileWriter(f);
                o1.write(toWrite);
                o1.flush();
                o1.close();
            }
            catch (IOException e) {}
        }

        else {
            JOptionPane.showMessageDialog(this,"File already exists. Stopped writing.", "File Exists",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openFile(){
        JFileChooser openDialog = new JFileChooser();
        openDialog.addChoosableFileFilter(new FileNameExtensionFilter("drm","drm"));
        int result = openDialog.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            readFile(openDialog.getSelectedFile());
        }
    }

    public void readFile(File f){
        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
            String buffer;
            String[] shards;

            for (int i = 0; i<8; i++){
                center.remove(staffList.get(i));
            }
            staffList.clear();
            for (int i = 0; i<8; i++){
                staffList.add(new Staff(this.metronome));
                center.add(staffList.get(i),""+i);
            }

            buffer = in.readLine();
            shards = buffer.split(" ");
            BPM.setValue(Integer.parseInt(shards[0]));
            Groove.setValue(Integer.parseInt(shards[1]));
            beatCt.setText(shards[2]);
            basDur.setText(shards[3]);

            metronome.Update(
                    Integer.parseInt(beatCt.getText()),
                    Integer.parseInt(basDur.getText()),
                    (Integer) BPM.getValue(),
                    (Float) Groove.getValue() / 100.0
            );

            int i = -1;
            while ((buffer = in.readLine()) != null){
                shards = buffer.split(" ");
                if (shards.length == 1) {
                   i++;
                   continue;
                }

                else if (shards.length == 3) {
                   staffList.get(i).addSound(shards[0], metronome.GetClicksPerCycle());
                   if (shards[1].contains("M")){
                       staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).toggleMute();
                   }
                   if (shards[1].contains("S")){
                       staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).toggleSolo();
                   }
                   for (int j = 0; j < metronome.GetClicksPerCycle(); j++){
                       if (shards[2].charAt(j)=='1')
                           staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).cells.get(j).toggleLight();
                   }
                }
            }
        } catch (IOException e) {}
    }

    public void keyPressed(KeyEvent k){  /* Hotkeys */

        switch (k.getKeyCode()){     /* Space: Restart loop */
            case KeyEvent.VK_SPACE:
                metronome.Stop();
                staffList.get(playSelect).reset();
                staffList.get(playSelect).light(0);
                metronome.Reset();
                break;
                
            case KeyEvent.VK_8:    /* 1-8: select bank */
                staffList.get(playSelect).reset();
                playSelect = 7;
                break;
            case KeyEvent.VK_7:
                staffList.get(playSelect).reset();
                playSelect = 6;
                break;
            case KeyEvent.VK_6:
                staffList.get(playSelect).reset();
                playSelect = 5;
                break;
            case KeyEvent.VK_5:
                staffList.get(playSelect).reset();
                playSelect = 4;
                break;
            case KeyEvent.VK_4:
                staffList.get(playSelect).reset();
                playSelect = 3;
                break;
            case KeyEvent.VK_3:
                staffList.get(playSelect).reset();
                playSelect = 2;
                break;
            case KeyEvent.VK_2:
                staffList.get(playSelect).reset();
                playSelect = 1;
                break;
            case KeyEvent.VK_1:
                staffList.get(playSelect).reset();
                playSelect = 0;
                break;
            case KeyEvent.VK_EQUALS:  /* +/- : increase/decrease BPM */
                BPM.setValue(Integer.parseInt(BPM.getValue().toString())+1);
                break;
            case KeyEvent.VK_MINUS:
                BPM.setValue(Integer.parseInt(BPM.getValue().toString())-1);
                break;
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

    public static void main(String[] args){  /* Initialize Sequencer */
        Sequencer seq = new Sequencer();
    }
}
