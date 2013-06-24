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
import java.io.File;
import java.util.LinkedList;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener{

    LinkedList<Staff> staffList;
    JSpinner BPM,Groove;
    JTextField beatCt, basDur;
    CardLayout cardMan;
    JPanel center, topLabel,btmProperties;
    JComboBox fileChoose;
    JButton loadSound;
    JScrollPane scrollWindow;
    Timer time;
    Presets pre;
    int timeSpacing,count,bpm,groove,print,playSelect, staffSelect;


    public Sequencer(){
        Initialize();
    }

    public void Initialize(){

        count = 0;
        playSelect=0;
        staffSelect=0;
        staffList = new LinkedList<Staff>();

        for (int i = 0; i<8;i++){
            staffList.add(new Staff());
        }
	topLabel = new JPanel();
	topLabel.setSize(100,100);
        JLabel bpmLab = new JLabel("BPM");
	JLabel timeSigLab = new JLabel("Time Signature");
        JLabel grooveLab = new JLabel("Groove (%):");
        SpinnerNumberModel spinNum = new SpinnerNumberModel(120,1,240,1);
        SpinnerNumberModel grooveNum = new SpinnerNumberModel(50,1,100,1);
        BPM = new JSpinner(spinNum);
        Groove = new JSpinner(grooveNum);
	JLabel slash= new JLabel("/");
        beatCt = new JTextField("4");
        basDur = new JTextField("4");
        btmProperties = new JPanel();
        
        pre = new Presets();
        JButton j = new JButton();

        loadSound = new JButton("Load Sound");
        loadSound.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E){
                staffList.get(staffSelect).addSound(fileChoose.getSelectedItem().toString());
            }
        });
        File temp = new File("Samples");

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
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setFocusable(true);    
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
        
        staffList.get(playSelect).light(count);
        if (count >= staffList.get(playSelect).getLength()-1) count = 0;
        else count++;
        if (count % 2 == 0){
            time.setDelay((int)((60000.0/(bpm*2.0))*(.01*groove)));
        }
        else
             time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(100-groove))));
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
        if (k.getKeyCode() == KeyEvent.VK_1){
            playSelect = 0;
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

    }

    public void mouseExited(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){

    }

    public static void main(String[] args){
        Sequencer seq = new Sequencer();
    }
}
