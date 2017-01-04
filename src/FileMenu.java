// FileMenu.java: main menu.

import javax.swing.*;
import java.awt.event.ActionListener;

class FileMenu extends JMenuBar {

    JMenu file, help;
    JMenuItem open, save;

    public FileMenu(){
        file = new JMenu("File");
        help  = new JMenu("Help");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");

        this.add(file);
        this.add(help);
        file.add(open);
        file.add(save);
    }

    public void addActionListener(ActionListener al) {
        file.addActionListener(al);
        open.addActionListener(al);
        help.addActionListener(al);
        save.addActionListener(al);
    }
}
