/* Sound.java: simple interface for playing sounds
*  Kevin Wern DRM-SEQ
*
*/

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;

class Sound {

    Clip clip;
    AudioInputStream inputStream;

    public Sound (String fileName) {  /* create audio file handle */
        File file = new File(fileName);
        try {
            inputStream = AudioSystem.getAudioInputStream(file.toURI().toURL());
            Line.Info linfo = new Line.Info(Clip.class);
            Line line = AudioSystem.getLine(linfo);
            clip = (Clip) line;
            clip.open(inputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play(){ /* play sound */
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

}
