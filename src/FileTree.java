import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileFilter;

public class FileTree extends JTree {

    public static String[] supportedFileTypes = new String[]{".wav",};

    public FileTree(File dir) {
        super(createTree(new DefaultMutableTreeNode(dir), dir));
    }

    private static DefaultMutableTreeNode createTree(DefaultMutableTreeNode node, File dir) {
        if (dir.listFiles() == null)
            return null;
        for (File f: dir.listFiles()) {
            if (f.isFile()) {
                boolean validFile = false;
                for (String fileExt : supportedFileTypes) {
                    if (f.getName().toLowerCase().endsWith(fileExt)) {
                        validFile = true;
                        break;
                    }
                }
                if (!validFile) {
                    continue;
                }
            }
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(f);
            node.add(newNode);
            createTree(newNode, f);
        }
        return node;
    }
}