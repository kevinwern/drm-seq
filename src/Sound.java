/* Sound.java: simple interface for playing sounds
*/

import javafx.scene.media.AudioClip;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;

class Sound {

    AudioClip clip;
    String fileName;

    public Sound (String fileName) {  /* create audio file handle */
        this.fileName = fileName;
        File file = new File(fileName);
        try {
            this.clip = new AudioClip(file.toURI().toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() { /* play sound */
        if (clip.isPlaying())
            clip.stop();
        clip.play();
    }
}
