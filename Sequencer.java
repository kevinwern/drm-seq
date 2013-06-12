import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener{

    Staff stf;
    JSpinner BPM,Groove;
    JTextField beatCt, basDur;
    JPanel topLabel,btmProperties;
    Timer time;
    int timeSpacing,count,bpm,groove;

    public Sequencer(){
        Initialize();
    }

    public void Initialize(){

        count = 0;
        stf = new Staff();
       // count = stf.getLength();
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

        bpm = Integer.parseInt(BPM.getValue().toString());
        time=new Timer(60000/480,this);
        time.start();
        
        JScrollPane scrollWindow = new JScrollPane(stf);
	scrollWindow.setPreferredSize(new Dimension(200,200));
        topLabel.add(bpmLab);
	topLabel.add(BPM);
        topLabel.add(grooveLab);
        topLabel.add(Groove);
        topLabel.add(timeSigLab);
	topLabel.add(beatCt);
        topLabel.add(slash);
        topLabel.add(basDur);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setTitle("DRM SEQ");
        this.setLayout(new BorderLayout());
        this.add(stf,BorderLayout.CENTER);
	this.add(topLabel,BorderLayout.NORTH);
        this.add(btmProperties,BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setFocusable(true);
    }

    public void actionPerformed(ActionEvent e){
        stf.light(count);
        if (count >= stf.getLength()-1) count = 0;
        else count++;
        if (bpm!= Integer.parseInt(BPM.getValue().toString())){
            bpm = Integer.parseInt(BPM.getValue().toString());
        }
        if (groove != Integer.parseInt(Groove.getValue().toString())){
            groove = Integer.parseInt(Groove.getValue().toString());
        }
        if (count % 2 == 0){
            time.setDelay((int)((60000.0/(bpm*2.0))*(.01*groove)));
        }
        else
             time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(100-groove))));
    }

    public void keyPressed(KeyEvent k){
        if (k.getKeyCode() == KeyEvent.VK_SPACE){
            time.stop();
            stf.light(0);
            count=1;
            time.setDelay((int)((60000.0/(bpm*2.0))*(.01*(groove))));
            time.start();
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
