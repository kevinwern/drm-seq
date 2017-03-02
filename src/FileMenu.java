// FileMenu.java: main menu.

import javax.swing.*;
import java.awt.event.ActionListener;

class FileMenu extends JMenuBar {

    JMenu file, help;
    JMenuItem open, save, about, newFile;

    public FileMenu(){
        file = new JMenu("File");
        help  = new JMenu("Help");
        newFile = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        about = new JMenuItem("About");

        this.add(file);
        this.add(help);
        file.add(newFile);
        file.add(open);
        file.add(save);
        help.add(about);
    }

    public void addActionListener(ActionListener al) {
        file.addActionListener(al);
        open.addActionListener(al);
        help.addActionListener(al);
        save.addActionListener(al);
        about.addActionListener(al);
        newFile.addActionListener(al);
    }
}
