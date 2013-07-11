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
import javax.swing.Timer;
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

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener{

    LinkedList<Staff> staffList;                                       // Each of the staves for the 8 preset banks
    JSpinner BPM,Groove;                                               // Groove and BPM spinners
    JTextField beatCt, basDur;                                         // Time signature
    CardLayout cardMan;
    JPanel center, topLabel,btmProperties;
    JComboBox fileChoose;                                              // File ComboBox
    JScrollPane scrollWindow;
    FileMenu menuBar;
    Timer time;
    Presets pre;                                                       
    int basicDuration,count,beatCount,bpm,groove,playSelect,staffSelect;


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

        JButton loadSound = new JButton("Load Sound");                  /* Initialize Load Sound button*/
        loadSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                staffList.get(staffSelect).addSound(fileChoose.getSelectedItem().toString(),staffList.get(staffSelect).getLength());
            }
        });

        ImageButton playButton = new ImageButton("play.png");
        playButton.setPreferredSize(new Dimension(20,20));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                time.start();
            }
        });

        ImageButton pauseButton = new ImageButton("pause.png");
        pauseButton.setPreferredSize(new Dimension(20,20));
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                time.stop();
            }
        });


        ImageButton stopButton = new ImageButton("stop.png");
        stopButton.setPreferredSize(new Dimension(20,20));
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                time.stop();
                staffList.get(playSelect).reset();
                count = 0;
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

        menuBar = new FileMenu();
        menuBar.addListeners(this);

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
        this.setJMenuBar( menuBar);
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
    }

    public void createTopLabel(){

    }

    public void createBottomLabel(){
        
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(time)){
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

        else if (e.getActionCommand().equals("about")){
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
        time.stop();
        BPM.setValue(120);
        Groove.setValue(50);
        basDur.setText(""+4);
        beatCt.setText(""+4);
        for (int i = 0; i<8; i++){
            center.remove(staffList.get(i));
        }
        staffList.clear();
        for (int i = 0; i<8; i++){
            staffList.add(new Staff());
            center.add(staffList.get(i),""+i);
        }
        time.start();
    }

    public void showDialog(){
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

    public void writeFile(File f){
        if (!f.exists()){
            String toWrite = bpm+" "+groove+" "+beatCount+" "+basicDuration+"\n";
            for (int i = 0; i<staffList.size(); i++){
                toWrite+=i+"\n"+staffList.get(i).dumpString();
            }
            
            try{FileWriter o1 = new FileWriter(f);
            o1.write(toWrite);
            o1.flush();
            o1.close(); }
            catch (IOException e) {}
        }

        else{
            JOptionPane.showMessageDialog(this,"File already exists. Stopped writing.", "File Exists",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openFile(){
        JFileChooser openDialog = new JFileChooser();
        openDialog.addChoosableFileFilter(new FileNameExtensionFilter("drm","drm"));
        int result = openDialog.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
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
            staffList.add(new Staff());
            center.add(staffList.get(i),""+i);
        }

        buffer = in.readLine();
        shards = buffer.split(" ");
        BPM.setValue(Integer.parseInt(shards[0]));
        Groove.setValue(Integer.parseInt(shards[1]));
        beatCt.setText(shards[2]);
        basDur.setText(shards[3]);

        int length;
        
        if (basicDuration==8)
            length = 2*beatCount;
        else
            length = 4*beatCount;

        int i = -1;
        while ((buffer = in.readLine()) != null){
            shards = buffer.split(" ");
            if (shards.length == 1) {
               i++;
               continue;
            }

            else if (shards.length == 3) {
               staffList.get(i).addSound(shards[0],length);
               if (shards[1].contains("M")){
                   staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).toggleMute();
               }
               if (shards[1].contains("S")){
                   staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).toggleSolo();
               }
               for (int j = 0; j<length; j++){
                   if (shards[2].charAt(j)=='1')
                       staffList.get(i).rowList.get(staffList.get(i).rowList.size()-1).cells.get(j).toggleLight();
               }
            }
        }
        } catch (IOException e) {}
    }

    public void keyPressed(KeyEvent k){

        switch (k.getKeyCode()){
            case KeyEvent.VK_SPACE:
                time.stop();
                staffList.get(playSelect).reset();
                staffList.get(playSelect).light(0);
                count=1;
                time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(groove))));
                time.start();
                break;
                
            case KeyEvent.VK_8:
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

            case KeyEvent.VK_EQUALS:
                BPM.setValue(Integer.parseInt(BPM.getValue().toString())+1);
                break;
            case KeyEvent.VK_MINUS:
                BPM.setValue(Integer.parseInt(BPM.getValue().toString())-1);
                break;
            
            case KeyEvent.VK_A:
                break;
            case KeyEvent.VK_S:
                break;
            case KeyEvent.VK_D:
                break;
            case KeyEvent.VK_F:
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

    public static void main(String[] args){
        Sequencer seq = new Sequencer();
    }
}
