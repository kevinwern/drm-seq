/* Sound.java: simple interface for playing sounds */


import javax.sound.sampled.*;
import java.io.File;

class Sound {

    Clip clip;
    String fileName;

    public Sound (String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info	info = new DataLine.Info(Clip.class, format);
            this.clip = (Clip) AudioSystem.getLine(info);
            this.clip.open(audioInputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.stop();
        clip.flush();
        clip.setFramePosition(0);
        clip.start();
    }
}
