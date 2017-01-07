/***--------------------------------------------------------------------------------------------*
DRM SEQ:                                                                                        |
Author: Kevin Wern                                                                              |
---------------------------------------------------------------------------------------------***/

//Sequencer.java: contains the main window and options, and establishes upper hierarchy of editing window

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.*;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener {

    public static final int TOTAL_PRESETS = 8;

    BpmControls bpmControls;
    Staffs staffs;
    Presets presets;
    JPanel btmProperties;
    FileMenu menuBar;
    Metronome metronome;
    JScrollPane scrollPane = new JScrollPane();
    FileTree tree;

    public Sequencer() {  //Default constructor
        Initialize();
    }

    public void Initialize() {
        metronome = new Metronome();
        staffs = new Staffs(metronome, TOTAL_PRESETS, 0);
        bpmControls = new BpmControls(this.metronome, this.staffs);
        presets = new Presets(staffs, TOTAL_PRESETS);

        ImageButton playButton = new ImageButton("Images/play.png");   //play, pause, and stop buttons
        playButton.setPreferredSize(new Dimension(20, 20));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Start();
            }
        });

        ImageButton pauseButton = new ImageButton("Images/pause.png");
        pauseButton.setPreferredSize(new Dimension(20, 20));
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Stop();
            }
        });


        ImageButton stopButton = new ImageButton("Images/stop.png");
        stopButton.setPreferredSize(new Dimension(20, 20));
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Stop();
                staffs.ResetCurrent();
            }
        });

        File sampleFile = new File("Samples");    /* Samples Folder (located in %CHOSEN_DIRECTORY%/Samples) */

        menuBar = new FileMenu();
        menuBar.addActionListener(this);

        btmProperties = new JPanel();
        btmProperties.add(playButton);
        btmProperties.add(pauseButton);
        btmProperties.add(stopButton);

        tree = new FileTree(sampleFile);
        tree.addMouseListener(this);

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setJMenuBar(menuBar);
        this.setTitle("DRM SEQ");
        this.setLayout(new BorderLayout());
        scrollPane.setViewportView(staffs);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(bpmControls, BorderLayout.NORTH);
        this.add(btmProperties, BorderLayout.SOUTH);
        this.add(tree, BorderLayout.WEST);
        this.add(presets, BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
        this.setFocusable(true); //Allow JPanel to be focused on (to avoid unfocusing for hotkeys)

        metronome.Start();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("About")) {  /* Menu actions */
            showDialog();
        } else if (e.getActionCommand().equals("Save")) {
            saveFile();
        } else if (e.getActionCommand().equals("Open")) {
            openFile();
        } else if (e.getActionCommand().equals("New")) {
            startNew();
        }
    }

    public void startNew() {
        metronome.Stop();
        staffs.Clear();
        metronome.Update(4, 4, 120, 0.5);
        metronome.Start();
    }

    public void showDialog() {   /* Help menu */
        JOptionPane.showMessageDialog(this,
                "Created January and July 2013\n\n" +
                        "Some quick controls:\n" +
                        "Spacebar:\t\t\tStart from first beat.\n" +
                        "+/-:\t\t\tIncrease/Decrease BPM\n" +
                        "Right Click (on any Row): Delete that row\n" +
                        "1-8:                      Play corresponding bank from current point in measure\n\n" +
                        "Other helpful button info:\n" +
                        "Mute (M):                 Mute the given track\n" +
                        "Solo (S):                 Play only the soloed track(s)\n" +
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

    public void writeFile(File f) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Staff staff : staffs.staffList) {
                oos.writeObject(staff);
            }
        } catch (IOException e) {
                System.out.println(e);
                return;
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

    public void readFile(File f) {
        staffs.Clear();
        int i = 0;
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream oos = new ObjectInputStream(fis);
            while (true) {
                Staff nextStaff = (Staff) oos.readObject();
                if (nextStaff == null)
                    break;
                else {
                    staffs.staffList.set(i, nextStaff);
                    staffs.add(nextStaff, Integer.toString(i));
                    metronome.RegisterObserver(nextStaff);
                    System.out.println(i);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        staffs.revalidate();
        staffs.repaint();
    }

    public void keyPressed(KeyEvent ke) {  /* Hotkeys */

        switch (ke.getKeyCode()) {     /* Space: Restart loop */
            case KeyEvent.VK_SPACE:
                metronome.Stop();
                staffs.ResetCurrent();
                metronome.Reset();
                break;

            case KeyEvent.VK_8:    /* 1-8: select bank */
            case KeyEvent.VK_7:
            case KeyEvent.VK_6:
            case KeyEvent.VK_5:
            case KeyEvent.VK_4:
            case KeyEvent.VK_3:
            case KeyEvent.VK_2:
            case KeyEvent.VK_1:
                int id = ke.getKeyCode() - KeyEvent.VK_1;
                staffs.PlayById(id);
                break;

            case KeyEvent.VK_EQUALS:  /* +/- : increase/decrease BPM */
                bpmControls.BpmAdd(1);
                break;
            case KeyEvent.VK_MINUS:
                bpmControls.BpmAdd(-1);
                break;
        }

    }

    public void keyReleased(KeyEvent k) {

    }

    public void keyTyped(KeyEvent k) {

    }

    public void mousePressed(MouseEvent e) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if(selRow != -1) {
            if(e.getClickCount() == 2) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getPathComponent(selPath.getPathCount() - 1);
                File soundFile = (File) node.getUserObject();
                staffs.GetCurrentShowing().addSound(soundFile.getAbsolutePath(), metronome.GetClicksPerCycle());
            }
        }
        requestFocus();
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }
}
