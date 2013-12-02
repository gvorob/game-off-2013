
    
   /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hellboss;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
/**
 *
 * @author George
 */
public class Sound {

   
   public static Clip pShoot, eShoot, hit1, hit2, hit3, hit4, die, explode, wscream, mutate, shock;
   
   // Constructor to construct each element of the enum with its own sound file.
   private Clip load(String soundFileName) {
        Clip clip = null;
        try {
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(soundFileName)));
             // Get a clip resource.
             clip = AudioSystem.getClip();
             // Open audio clip and load samples from the audio input stream.
             clip.open(audioInputStream);
        } catch (Exception e)
        {
            Misc.prln(e.getLocalizedMessage());
          Misc.prln("ERROR LOADING SOUND");
        }
        return clip;
   }
   
   public static void init() {
       Sound s = new Sound();
       pShoot = s.load("/sound/shoot1.wav");
       eShoot = s.load("/sound/shoot2.wav");
       hit1 = s.load("/sound/hit1.wav");
       hit2 = s.load("/sound/hit2.wav");
       hit3 = s.load("/sound/hit3.wav");
       hit4 = s.load("/sound/hit4.wav");
       die = s.load("/sound/death.wav");
       mutate = s.load("/sound/mutate.wav");
       explode = s.load("/sound/explosion.wav");
       wscream = s.load("/sound/wilhelmscream.wav");
       shock = s.load("/sound/shock.wav");
   }
   
   // Play or Re-play the sound effect from the beginning, by rewinding.
   public static void play(Clip clip) {
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
   }
   
   // Optional static method to pre-load all the sound files.
}
