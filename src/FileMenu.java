// FileMenu.java: main menu.

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

class FileMenu extends JMenuBar {

    JMenuItem file, help;

    public FileMenu(){
        file = new JMenuItem("File");
        help  = new JMenuItem("Help");
        this.add(file);
        this.add(help);
    }
}
