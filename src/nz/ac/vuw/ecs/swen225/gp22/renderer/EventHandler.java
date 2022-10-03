package nz.ac.vuw.ecs.swen225.gp22.renderer;


import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;

public class EventHandler{
	List<Subject> activeListeners = new ArrayList<>();
	SoundPlayer s;
	
	public EventHandler(Level l, SoundPlayer s){
		this.s = s;
		// add beep sound to all entity interactions
		l.getEntites().forEach(a -> {
			a.attach(()->s.play(Sound.beep));
			activeListeners.add(a);
		});
//		l.getPlayer().attach(()->s.play(Sound.beep)); activeListeners.add(l.getPlayer());
		
	}
	
	public void attach(Subject s, Observer o) {
		s.attach(o);
		activeListeners.add(s);
	}
	
	public void detatchAll() {
		activeListeners.forEach(a -> a.detatch());
		activeListeners.clear();
	}

}
