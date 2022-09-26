package nz.ac.vuw.ecs.swen225.gp22.app;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;

public class Chips extends JFrame{
//	State curState;
	SoundPlayer sound;
	Controller controller;
	Runnable closePhase = ()->{};
	
	Chips(){
		sound = new SoundPlayer();
	    assert SwingUtilities.isEventDispatchThread();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    controller = new Controller();
	    initialPhase();
	    System.out.println("Initialising pane");
	    setVisible(true);
	    addWindowListener(new WindowAdapter(){
	      public void windowClosed(WindowEvent e){closePhase.run();}
	    });
	}
	
	/**
	 * Start menu of the game
	 */
	private void initialPhase() {
		// Text and Buttons
		var welcome = new JLabel(" Welcome to Chips Challenge! ");
	    var startLvl1 = new JButton(" Start! (Lvl 1) ");
	    var startLvl2 = new JButton(" Start! (Lvl 2) ");
	    
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(startLvl1);
	     remove(startLvl2);
	     };
	     
	     // Draw buttons
	     add(BorderLayout.CENTER, welcome);
	     add(BorderLayout.SOUTH,startLvl1);
	     add(BorderLayout.NORTH,startLvl2);
	     
	     // Add listeners
	     startLvl1.addActionListener(e->setPhase());
	     startLvl2.addActionListener(e->setPhase());
	     
	     // Set size of window
	     setPreferredSize(new Dimension(1000,600));
	     pack();
	}
	
	/**
	 * Pause menu displaying info for user
	 */
	void pauseMenu() {
		// Text and Buttons
		var pause = new JLabel(" You are currently in pause ");
		var resume = new JButton(" Resume ");
		
		// closePhase set up
		closePhase.run();
	    closePhase=()->{
	     remove(pause);
	     remove(resume);
	     };
	     
	     
		// Display buttons
		add(BorderLayout.CENTER, pause);
		add(BorderLayout.SOUTH, resume);
		
		 // Add listeners to button
	     resume.addActionListener(e->setPhase());
	     
	     // Set size of window
	     setPreferredSize(new Dimension(1000,600));
	     pack();
	}
	
	void setPhase(){
		System.out.println("Setting level");
		// Set up new Level
		Level level = Levels.loadLevel(()-> initalPhase(), sound, "level1.xml");
			
	    // Set up the viewport
	    LevelView view = new LevelView(level);
	    
	    // KeyListener to listen for controls from the user
	    view.addKeyListener(controller);
	    controller.newInstance(level.getPlayer());
	    
	    view.setFocusable(true);
	    // New timer
	    Timer timer = new Timer(34,unused->{
	      assert SwingUtilities.isEventDispatchThread();
	      level.tick();
	      view.repaint();
	    });
	    closePhase.run();//close phase before adding any element of the new phase
	    closePhase=()->{ timer.stop(); remove(view);};
	    add(BorderLayout.CENTER,view);//add the new phase viewport
	    setPreferredSize(getSize());//to keep the current size
	    pack();                     //after pack
	    view.requestFocus();//need to be after pack
	    timer.start();
	   
	  }
}
