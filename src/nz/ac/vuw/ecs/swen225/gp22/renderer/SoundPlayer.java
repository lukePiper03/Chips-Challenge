package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.event.*;
import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

import javax.sound.sampled.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;

public class SoundPlayer
{
	Map<Sound, Clip> sounds;
	
	// THE USE OF THE SOUND PLAYER IS TO KEEP TRACK OF PLAYING AUDIO CLIPS 
	// BY KEEPING TRACK, WE CAN START AND STOPPING EXISTING SOUNDS and support looping
	
    public SoundPlayer()
    {
    	// load in all sounds
    	sounds = new HashMap<>();
    	sounds.putAll( Stream.of(Sound.values()).collect(Collectors.toMap(a->a, a->a.clip))    );
    	System.out.println(sounds);
    }
    
    public Clip getSound(Sound s) {
    	if(sounds.containsKey(s)) {
    		return sounds.get(s);
    	} else {
    		throw new IllegalArgumentException("Sound " + s + " does not exist.");
    	}
    }
    
    public void play(Sound s){
    	// take argument to play specific sound
    	Clip curClip = getSound(s);
    	curClip.setFramePosition(0);
    	curClip.start();
    	System.out.println("Playing sound " + s);
    }
    
    public void loop(Sound s){
    	getSound(s).loop(Clip.LOOP_CONTINUOUSLY);
    	System.out.println("Looping sound " + s);
    }
  
    public void stop(Sound s){
    	getSound(s).stop();
    	System.out.println("Stopping sound " + s);
    }
    
    public void stopAll(){
    	sounds.values().stream().forEach(clip -> clip.stop());
    }
}