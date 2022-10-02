package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp22.domain.Player;

/**
 * Controller for all movements/functionality of the user in game
 *
 */
public class Controller extends Keys {
	private HashMap<String, Integer> keyConfig = new HashMap<>();
	
	/**
	 * Add initial keyBinds to keyConfig
	 */
	public Controller(){
		keyConfig.put("Up", KeyEvent.VK_W);
		keyConfig.put("Down", KeyEvent.VK_S);
		keyConfig.put("Left", KeyEvent.VK_A);
		keyConfig.put("Right", KeyEvent.VK_D);
//		keyConfig.put("Exit Game", KeyEvent.CTRL_DOWN_MASK  KeyEvent.VK_X);
//		keyConfig.put("Save Game", KeyEvent.CTRL_DOWN_MASK + KeyEvent.VK_S);
//		keyConfig.put("Resume Game", KeyEvent.CTRL_DOWN_MASK + KeyEvent.VK_R);
//		keyConfig.put("Start New Game (at Lvl 1)", KeyEvent.CTRL_DOWN_MASK + KeyEvent.VK_1);
//		keyConfig.put("Start New Game (at Lvl 2)", KeyEvent.CTRL_DOWN_MASK + KeyEvent.VK_2);
		keyConfig.put("Pause", KeyEvent.VK_PAUSE);
		keyConfig.put("Resume Paused Game", KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @param p
	 * @return Controller
	 */
	public Controller newInstance(Player p){
		// if keyConfig is empty, set initial values
		// Basic movement keyBinds
		setAction(keyConfig.getOrDefault("Up", KeyEvent.VK_W),p.set(Direction::up),p.set(Direction::unUp));
	    setAction(keyConfig.getOrDefault("Down", KeyEvent.VK_S),p.set(Direction::down),p.set(Direction::unDown));
	    setAction(keyConfig.getOrDefault("Left", KeyEvent.VK_A),p.set(Direction::left),p.set(Direction::unLeft));
	    setAction(keyConfig.getOrDefault("Right", KeyEvent.VK_D),p.set(Direction::right),p.set(Direction::unRight));
	    // Advanced keyBinds
	    
	    return this;
	}
	

	/**
	 * @param name
	 * @param code
	 */
	public void setKey(String name, int code) {
		keyConfig.put(name, code);
	}
	
	/**
	 * @return keyConfig hashMap
	 */
	public HashMap<String, Integer> getKeyset(){
		return keyConfig;
	}
}
