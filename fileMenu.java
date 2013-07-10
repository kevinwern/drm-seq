// fileMenu.java: simple main menu for all.

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


class fileMenu extends JMenuBar {

    JMenuItem holyFiles, holyHelp;

    public void FileMenu(){
        holyFiles = new JMenuItem("File");
        holyHelp  = new JMenuItem("Help");
        this.add(holyFiles);
        this.add(holyHelp);
    }
}
