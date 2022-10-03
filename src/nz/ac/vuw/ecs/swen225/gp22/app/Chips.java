package nz.ac.vuw.ecs.swen225.gp22.app;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.Timer;

import javax.swing.ImageIcon;

import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

public class Chips extends JFrame{
//	State curState;
	Controller controller;
	Runnable closePhase = ()->{};
	
	public Chips(){
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
	    var welcome = new JLabel("<html>Bunny's<br>Challenge!</html>", SwingConstants.CENTER);
		var startLvl1 = new JButton(" Start! (Lvl 1) ");
		var startLvl2 = new JButton(" Start! (Lvl 2) ");
		var help = new JButton("Help");
		
		// Setting font and colour
		welcome.setFont(LoadedFont.PixeloidSans.getSize(70f));
		welcome.setForeground(Color.white);
	   
		// Set Bounds
	    startLvl1.setBounds(200, 400, 200, 50);
	    startLvl2.setBounds(600, 400, 200, 50);
	    help.setBounds(400, 500, 200, 50);
	    
	    // Add Text and Buttons
	    add(BorderLayout.CENTER, startLvl1);
	    add(BorderLayout.CENTER, startLvl2);
	    add(BorderLayout.CENTER, help);
	    add(BorderLayout.CENTER, welcome);
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	    
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(startLvl1);
	     remove(startLvl2);
	     remove(help);
	     remove(this);
	     };
	     
	     // Add listeners
	     startLvl1.addActionListener(e->phaseOne());
	     startLvl2.addActionListener(e->phaseTwo());
	     help.addActionListener(e->helpMenu());
	     
	     setPreferredSize(new Dimension(1000,600));
	     setResizable(false);
 
	     pack();
	}
	
	public void phaseOne(){
	    setPhase(()->phaseTwo(),()->deathMenu(), "level1.xml");
	}
		  
    public void phaseTwo() {
    	setPhase(()->victoryMenu(),()->deathMenu(), "level2.xml");
	}
	
    void setPhase(Runnable next, Runnable end, String fileName){
		System.out.println("Setting level");
		
		// Set up new Level
		Level level = Levels.loadLevel(next, end, fileName);
		
		//Create the recorder
		Recorder.recorder = new Recorder(level);
		
	    // Set up the viewport
	    LevelView view = new LevelView(level);
	    
	    // KeyListener to listen for controls from the user
	    view.addKeyListener(controller);
	    controller.newInstance(level.getPlayer());
	    
	    view.setFocusable(true);
	    
	    // New timer
	    Timer timer = new Timer(25,unused->{
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
		// Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new BorderLayout());
	    
		// Text and Buttons
		var pause = new JLabel("<html> You are currently in pause </html>");
		var resume = new JButton(" Resume ");
		
		// Setting position and size
		pause.setBounds(200, 50, 600, 200);
	    resume.setBounds(400, 400, 200, 50);
	      
	    // Setting font and colour
 		pause.setFont(LoadedFont.PixeloidSans.getSize(70f));
 		pause.setForeground(Color.white);
 	    
 	    // Add Text and Buttons
 	    add(BorderLayout.CENTER, resume);
 	    add(BorderLayout.CENTER, pause);
 	 
 	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	    setVisible(true);
 		   
 	    // closephase set up
 	    closePhase.run();
 	    closePhase=()->{
 	     remove(pause);
 	     remove(resume);
 	     remove(this);
 	     };
	 	     
 	     // Add listeners
 	     resume.addActionListener(e->phaseOne());
 	     
 	     setPreferredSize(new Dimension(1000,600));
 	     setResizable(false);
  
 	     pack();
	}
	
	/**
	 * Menu for if player has died during the game
	 */
	void deathMenu() {
		// Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new BorderLayout());
	    
		// Text and Buttons
		var deathText = new JLabel("<html> Oh no, you DIED </html>", SwingConstants.CENTER);
		var menu = new JButton(" Back to Menu ");
		
		// Setting position and size
		deathText.setBounds(200, 50, 600, 200);
	    menu.setBounds(400, 400, 200, 50);
	      
	    // Setting font and colour
 		deathText.setFont(LoadedFont.PixeloidSans.getSize(70f));
 		deathText.setForeground(Color.black);
 	    
 	    // Add Text and Buttons
 	    add(BorderLayout.CENTER, menu);
 	    add(BorderLayout.CENTER, deathText);
 	 
 	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	    setVisible(true);
 		   
 	    // closephase set up
 	    closePhase.run();
 	    closePhase=()->{
 	     remove(deathText);
 	     remove(menu);
 	     remove(this);
 	    };
	 	     
 	    // Add listeners
 	    menu.addActionListener(e->initialPhase());
 	    
 	    setPreferredSize(new Dimension(1000,600));
 	    setResizable(false);
 
	    pack();
	}
	
	/**
	 * Victory menu
	 */
	void victoryMenu() {
		// Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new BorderLayout());
	    
		// Text and Buttons
		var victoryText = new JLabel("<html> You have finished Bunny's Challenge <br> Congratulations </html>", SwingConstants.CENTER);
		var menu = new JButton(" Back to Menu ");
		
		// Setting position and size
		victoryText.setBounds(500, 50, 600, 200);
	    menu.setBounds(400, 400, 200, 50);
	      
	    // Setting font and colour
		victoryText.setFont(LoadedFont.PixeloidSans.getSize(30f));
		victoryText.setForeground(Color.white);
	    
	    // Add Text and Buttons
	    add(BorderLayout.CENTER, menu);
	    add(BorderLayout.CENTER, victoryText);
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
		   
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(victoryText);
	     remove(menu);
	     remove(this);
	    };
	 	     
	     // Add listeners
	     menu.addActionListener(e->initialPhase());
	     
	     setPreferredSize(new Dimension(1000,600));
	     setResizable(false);
 
	     pack();
	}
	
	void helpMenu() {
		// Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new BorderLayout());
		
		// Create text buttons
		var header = new JLabel("A little stuck?", SwingConstants.CENTER);
		var rules = new JLabel("The set of rules loren ispum etc blah blah blah", SwingConstants.CENTER);
		var menu = new JButton("Back to Menu");
		
		// Setting position and size
		header.setBounds(200, 50, 600, 200);
		menu.setBounds(400, 500, 200, 50);
	      
	    // Setting font and colour
 		header.setFont(LoadedFont.PixeloidSans.getSize(50f));
 		header.setForeground(Color.white);
 		
 		rules.setFont(new Font("Calibri", Font.BOLD, 16));
 		rules.setForeground(Color.white);
 		
 		// Add Text and Buttons
	    add(BorderLayout.CENTER, menu);
	    add(BorderLayout.CENTER, rules);
	    add(BorderLayout.NORTH, header);
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
		   
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(header);
	     remove(rules);
	     remove(menu);
	     remove(this);
	    };
	    
	    // Add listeners
 	    menu.addActionListener(e->initialPhase());
 	    
 	    setPreferredSize(new Dimension(1000,600));
 	    setResizable(false);
 
	    pack();
	}
}