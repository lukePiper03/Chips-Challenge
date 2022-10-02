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
import java.awt.Font;
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

import main.Phase;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;

public class Chips extends JFrame{
//	State curState;
	Controller controller;
	Runnable closePhase = ()->{};
	
	Chips(){
	    assert SwingUtilities.isEventDispatchThread();
	    controller = new Controller();
	    initialPhase();
	    System.out.println("Initialising pane");
	    addWindowListener(new WindowAdapter(){
	      public void windowClosed(WindowEvent e){closePhase.run();}
	    });
	}
	
	/**
	 * Start menu of the game
	 */
	private void initialPhase() {
		// Make JFrame
		JFrame frame = new JFrame("Chips Challenge");
	    frame.setSize(1000, 600);
	      
	    frame.setResizable(false);
	    frame.setLayout(null);
	      
	    // Create Display
	    var welcome = new JLabel(" Welcome to Chips Challenge! ");
		var startLvl1 = new JButton(" Start! (Lvl 1) ");
		var startLvl2 = new JButton(" Start! (Lvl 2) ");
		welcome.setFont(new Font("Calibri", Font.PLAIN, 40));
	 
	    // Setting position and size
		welcome.setBounds(200, 50, 600, 200);
	    startLvl1.setBounds(200, 350, 200, 50);
	    startLvl2.setBounds(600, 350, 200, 50);
	      
	    // Add display
	    frame.add(welcome);
	    frame.add(startLvl1);
	    frame.add(startLvl2);
	      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
		   
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(startLvl1);
	     remove(startLvl2);
	     };
	     
	     // Add listeners
	     startLvl1.addActionListener(e->phaseOne());
	     startLvl2.addActionListener(e->phaseTwo());
 
	     pack();
	}
	
	private void phaseOne(){
	    setPhase("level1.xml");
	}
		  
    private void phaseTwo() {
	  System.out.println("Coming soon");
	}
	
	void setPhase(String fileName){
		System.out.println("Setting level");
		// Set up new Level
		Level level = Levels.loadLevel(fileName);
			
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
	
	
	/**
	 * Pause menu displaying info for user
	 */
	void pauseMenu() {
		// Make JFrame
		JFrame frame = new JFrame("Chips Challenge");
		frame.setSize(1000, 600);
			      
		frame.setResizable(false);
	    frame.setLayout(null);
	    
		// Text and Buttons
		var pause = new JLabel(" You are currently in pause ");
		var resume = new JButton(" Resume ");
		resume.setFont(new Font("Calibri", Font.PLAIN, 40));
		
		// Setting position and size
		pause.setBounds(200, 50, 600, 200);
	    resume.setBounds(400, 350, 200, 50);
	      
	    // Add display
	    frame.add(pause);
	    frame.add(resume);
	      
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
		
		// closePhase set up
		closePhase.run();
	    closePhase=()->{
	     remove(pause);
	     remove(resume);
	     };
	     
		 // Add listeners to button
	     //resume.addActionListener(e->setPhase());
	     
	     pack();
	}
}
