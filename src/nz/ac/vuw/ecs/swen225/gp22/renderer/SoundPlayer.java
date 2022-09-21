package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.awt.event.*;
import javax.swing.*;

import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

import javax.sound.sampled.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;

public class SoundPlayer
{
	Sound s1;
	
    public SoundPlayer()
    {
    	s1 = loadSound(); 
        //URL file = new URL("file:./flyby1.wav");
        //URL file = new URL("file:c:/users/netro/java/flyby1.wav");
//        URL file;
//		try {
//			file = new URL("https://www.wavsource.com/snds_2020-10-01_3728627494378403/sfx/air_raid.wav");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
//        Clip clip = AudioSystem.getClip();
//        clip.open(ais);
    }
    
    public Sound loadSound() {
    	return Sound.eightbitsong;
    }
    
    public void playSound(){
    	System.out.println("Playing song " + s1);
    	s1.clip.start();
    }
}