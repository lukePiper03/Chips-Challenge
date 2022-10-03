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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;

import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

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
	    // Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new BorderLayout());
	   
		// Create Text and Buttons
	    var welcome = new JLabel("<html>Bunny's<br/>Challenge!</html>", SwingConstants.CENTER);
		var startLvl1 = new JButton(" Start! (Lvl 1) ");
		var startLvl2 = new JButton(" Start! (Lvl 2) ");
		
		// Setting font and colour
		welcome.setFont(LoadedFont.PixeloidSans.getSize(70f));
		welcome.setForeground(Color.white);
	   
		// Set Bounds
	    startLvl1.setBounds(200, 400, 200, 50);
	    startLvl2.setBounds(600, 400, 200, 50);
	    
	    // Add Text and Buttons
	    add(BorderLayout.CENTER, startLvl1);
	    add(BorderLayout.CENTER, startLvl2);
	    add(BorderLayout.CENTER, welcome);
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
		   
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(startLvl1);
	     remove(startLvl2);
	     remove(this);
	     };
	     
	     // Add listeners
	     startLvl1.addActionListener(e->setPhase());
	     startLvl2.addActionListener(e->phaseTwo());
	     
	     setPreferredSize(new Dimension(1000,600));
 
	     pack();
	}
	
	private void phaseOne(){
//	    setPhase("level1.xml");
	}
		  
    private void phaseTwo() {
	  System.out.println("Coming soon");
	}
	
    void setPhase(){
		System.out.println("Setting level");
		// Set up new Level
		
		Level level = Levels.loadLevel(()-> initialPhase(), "level1.xml");
		
		//Create the recorder
		Recorder.recorder = new Recorder(level);
		
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