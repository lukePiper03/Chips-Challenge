package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp22.domain.Player;

public class Controller extends Keys {
	private HashMap<String, Integer> keyConfig = new HashMap<>();
	
	public Controller(){
		keyConfig.put("Up", KeyEvent.VK_W);
		keyConfig.put("Down", KeyEvent.VK_S);
		keyConfig.put("Left", KeyEvent.VK_A);
		keyConfig.put("Right", KeyEvent.VK_D);
	}
	
	public Controller newInstance(Player p){
		// if keyConfig is empty, set initial values
		setAction(keyConfig.getOrDefault("Up", KeyEvent.VK_W),p.set(Direction::up),p.set(Direction::unUp));
	    setAction(keyConfig.getOrDefault("Down", KeyEvent.VK_S),p.set(Direction::down),p.set(Direction::unDown));
	    setAction(keyConfig.getOrDefault("Left", KeyEvent.VK_A),p.set(Direction::left),p.set(Direction::unLeft));
	    setAction(keyConfig.getOrDefault("Right", KeyEvent.VK_D),p.set(Direction::right),p.set(Direction::unRight));
	    return this;
	}
	

	public void setKey(String name, int code) {
		keyConfig.put(name, code);
	}
	
	public HashMap<String, Integer> getKeyset(){
		return keyConfig;
	}
}
