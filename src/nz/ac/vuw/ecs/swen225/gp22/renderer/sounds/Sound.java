package nz.ac.vuw.ecs.swen225.gp22.renderer.sounds;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public enum Sound{
	eightbitsong;
	public final Clip clip;
	  Sound(){clip=loadSound(this.name());}
	  static private Clip loadSound(String name){
		 URL soundPath = Sound.class.getResource(name+".wav");
	    try{
	    	AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundPath);
	    	Clip c = AudioSystem.getClip();
	    	c.open(audioIn);
	    	return c;
	    	
	    }
	    catch(Exception e) {
	    	System.out.println("Sound does not exist: " + name+".wav");
	    	e.printStackTrace();
	    	throw new Error(e); 
	    } 
	    

	  }
  
}
