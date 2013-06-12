import java.applet.AudioClip;
import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;

class Sound {

    AudioClip ac;

    public Sound (String fileName) {
        try {
            File file = new File("Samples/"+fileName);
            ac = Applet.newAudioClip(file.toURI().toURL());
        }
        catch (MalformedURLException mue){
            throw new RuntimeException("bleh");
        }
    }

    public void play(){
        ac.play();
    }

}
