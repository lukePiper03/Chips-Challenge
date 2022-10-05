package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import javax.swing.SwingUtilities;

class Keys implements KeyListener {
  private Map<Integer,Runnable> actionsPressed = new HashMap<>();
  private Map<Integer,Runnable> actionsReleased = new HashMap<>();
  Chips chip;
  public void setAction(int keyCode,Runnable onPressed,Runnable onReleased){
    actionsPressed.put(keyCode,onPressed);
    actionsReleased.put(keyCode,onReleased);
    }
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
	  if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_1) {
		  System.out.println("Starting Game from Level 1");
		  chip.phaseOne();
		  
	  } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_2) {
		  System.out.println("Starting Game from Level 2");
		  chip.phaseTwo();
		  
	  } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
		  System.out.println("Exiting");
		  chip.initialPhase();
		
	  } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		  System.out.println("Pausing");
		  if(chip.inPause == false) {
			  chip.pauseMenu();
		  }
		  
	  } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
		  System.out.println("Saving");
		  //Levels.saveLevel(chip.getCurrentLevel(), "savedLevel.xml");
		  chip.saveMenu();
		  
	  } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R) {
		  System.out.println("Resuming Saved Level");
		  chip.loadMenu();
	  } else {
    assert SwingUtilities.isEventDispatchThread();
    actionsPressed.getOrDefault(e.getKeyCode(),()->{}).run();}
  }
 
  public void keyReleased(KeyEvent e){
    assert SwingUtilities.isEventDispatchThread();
    actionsReleased.getOrDefault(e.getKeyCode(),()->{}).run();
  }
}