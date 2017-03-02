/***--------------------------------------------------------------------------------------------*
DRM SEQ:                                                                                        |
Author: Kevin Wern                                                                              |
---------------------------------------------------------------------------------------------***/

//Sequencer.java: contains the main window and options, and establishes upper hierarchy of editing window

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.*;

public class Sequencer extends JFrame implements ActionListener,KeyListener,MouseListener, XmlSerializable {

    public static final int TOTAL_PRESETS = 8;

    BpmControls bpmControls;
    Staffs staffs;
    Presets presets;
    JPanel btmProperties;
    FileMenu menuBar;
    Metronome metronome;
    JScrollPane scrollPane;
    FileTree tree;

    public Sequencer() {  //Default constructor
        Initialize();
    }

    public void Initialize() {

        menuBar = new FileMenu();
        menuBar.addActionListener(this);
        this.setJMenuBar(menuBar);
        metronome = new Metronome();

        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setTitle("DRM SEQ");
        this.setLayout(new BorderLayout());

        createAndSetupFileTree();
        createAndSetupStaffs();
        createAndSetupPresets();
        createAndSetupBpmControls();
        createAndSetupTransport();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);
        this.setFocusable(true); //Allow JPanel to be focused on (to avoid unfocusing for hotkeys)
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

    public void createAndSetupTransport() {
        ImageButton playButton = new ImageButton("Images/play.png");   //play, pause, and stop buttons
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Start();
            }
        });

        ImageButton pauseButton = new ImageButton("Images/pause.png");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Stop();
            }
        });


        ImageButton stopButton = new ImageButton("Images/stop.png");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent E) {
                metronome.Stop();
                metronome.Reset();
                staffs.ResetCurrent();
            }
        });

        btmProperties = new JPanel();
        btmProperties.add(playButton);
        btmProperties.add(pauseButton);
        btmProperties.add(stopButton);

        this.add(btmProperties, BorderLayout.SOUTH);
    }

    public void createAndSetupBpmControls() {
        bpmControls = new BpmControls(this.metronome, this.staffs);
        this.add(bpmControls, BorderLayout.NORTH);
    }

    public void createAndSetupFileTree() {
        File sampleFile = new File("Samples");    /* Samples Folder (located in %CHOSEN_DIRECTORY%/Samples) */
        tree = new FileTree(sampleFile);
        tree.addMouseListener(this);
        this.add(tree, BorderLayout.WEST);
    }

    public void createAndSetupStaffs() {
        staffs = new Staffs(metronome, TOTAL_PRESETS, 0);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(staffs);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void createAndSetupPresets() {
        presets = new Presets(staffs, TOTAL_PRESETS);
        this.add(presets, BorderLayout.EAST);
    }

    public void startNew() {
        metronome.Stop();
        metronome.ClearObservers();
        metronome.ResetToDefaults();

        staffs.Clear();
        staffs.Initialize();
        staffs.showDefaultStaff();
        update(metronome, staffs);
    }


    // TODO: make this less garbagey by actually having some modular display, not a hard-coded string
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
        FileNameExtensionFilter ff = new FileNameExtensionFilter("DRM-SEQ files","drm");
        saveDialog.addChoosableFileFilter(ff);
        saveDialog.setFileFilter(ff);
        int result=saveDialog.showSaveDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            try {
                writeFile(saveDialog.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not write file.",
                        "File error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void writeFile(File f) throws FileNotFoundException, ParserConfigurationException, TransformerException {
        FileOutputStream fos;
        fos = new FileOutputStream(f);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Element element = null;
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.newDocument();
        element = toXmlElement(document);
        document.appendChild(element);
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
         // send DOM to file
        tr.transform(new DOMSource(document), new StreamResult(fos));
    }


    public void openFile(){
        JFileChooser openDialog = new JFileChooser();
        FileNameExtensionFilter ff = new FileNameExtensionFilter("DRM-SEQ files","drm");
        openDialog.addChoosableFileFilter(ff);
        openDialog.setFileFilter(ff);
        int result = openDialog.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                readFile(openDialog.getSelectedFile());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not open file.",
                        "File error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void readFile(File file) throws IOException, ParserConfigurationException, SAXException {
        FileInputStream fis = new FileInputStream(file);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(fis);
        Node result = dom.getDocumentElement();
        Element element = (Element) result;
        fromXmlElement(element);
    }

    public void keyPressed(KeyEvent ke) {  /* Hotkeys */

        switch (ke.getKeyCode()) {     /* Space: Restart loop */
            case KeyEvent.VK_SPACE:
                metronome.Stop();
                staffs.ResetCurrent();
                metronome.Reset();
                metronome.Start();
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
                staffs.GetCurrentShowing().addSound(soundFile.getPath(), metronome.GetClicksPerCycle());
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

    public void update(Metronome metronome, Staffs staffs) {

        if (this.staffs != staffs) {
            this.staffs.removeAll();
            this.remove(this.scrollPane);
            this.scrollPane = new JScrollPane();
            this.staffs = staffs;
            scrollPane.setViewportView(staffs);
            staffs.showDefaultStaff();
            presets.setStaffs(staffs);
            this.add(scrollPane, BorderLayout.CENTER);
        }

        this.remove(bpmControls);
        this.bpmControls = new BpmControls(metronome, staffs);
        this.add(bpmControls, BorderLayout.NORTH);
        bpmControls.Update();
    }

    @Override
    public Element toXmlElement(Document document) {
        Element element = null;
        element = document.createElement("settings");
        element.appendChild(staffs.toXmlElement(document));
        element.appendChild(metronome.toXmlElement(document));
        return element;
    }

    public void fromXmlElement(Element element) {
        Element bpmControlsElement = (Element) element.getElementsByTagName("bpmcontrols").item(0);
        Metronome newMetronome = Metronome.fromXmlElement(this.metronome, bpmControlsElement);
        Element staffsElement = (Element) element.getElementsByTagName("staffs").item(0);
        Staffs newStaffs = Staffs.fromXmlElement(staffsElement, this.metronome);
        update(newMetronome, newStaffs);
    }
}
