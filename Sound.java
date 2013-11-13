/* Sound.java: simple interface for playing sounds
*  Kevin Wern DRM-SEQ
*
*/

import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;

class Sound {

    AudioClip ac;

    public Sound (String fileName) {  /* create audio file handle */
        try {
            File file = new File(fileName);
            ac = Applet.newAudioClip(file.toURI().toURL());
        }
        catch (MalformedURLException mue){
            throw new RuntimeException("bleh");
        }
    }

    public void play(){ /* play sound */
        ac.play();
    }

}
