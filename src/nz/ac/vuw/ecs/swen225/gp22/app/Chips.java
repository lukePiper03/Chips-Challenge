package nz.ac.vuw.ecs.swen225.gp22.app;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

import static javax.swing.JOptionPane.showMessageDialog;

public class Chips extends JFrame{
//	State curState;
	Controller controller;
	Level level;
	boolean inPause;
	
	boolean isSave1 = false;
	boolean isSave2 = false;
	boolean isSave3 = false;
		
	Runnable closePhase = ()->{};
	
	public Chips(){
	    assert SwingUtilities.isEventDispatchThread();
	    controller = new Controller(this);
	    initialPhase();
	    System.out.println("Initialising pane");
	    addWindowListener(new WindowAdapter(){
	      public void windowClosed(WindowEvent e){closePhase.run();}
	    });
	}
		
	/**
	 * Start menu of the game
	 */
	void initialPhase() {	 
		inPause = false;
	    // Set background
	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
		setLayout(new OverlayLayout(getContentPane()));
		setLocationRelativeTo(null);
	    
		// Create Text and Buttons
	    var welcome = new JLabel("<html>Bunny's<br>Challenge!</html>", SwingConstants.CENTER);
		var startLvl1 = new JButton("Start! (Lvl 1)");
		var startLvl2 = new JButton("Start! (Lvl 2)");
		var loadLevel = new JButton("Load saved Level");
		var help = new JButton("Help");
		
		// Setting font and colour
		welcome.setFont(LoadedFont.PixeloidSans.getSize(70f));
		welcome.setForeground(Color.white);
	   
		// Set Bounds
	   // startLvl1.setBounds(200, 600, 200, 50);
	    startLvl1.setAlignmentX(0.3f);
	   startLvl1.setAlignmentY(0.8f);
	    startLvl2.setBounds(600, 400, 200, 50);
	    loadLevel.setBounds(200, 475, 200, 50);
	    help.setBounds(600, 475, 200, 50);
	    
	    // Change cursor type on hover
	    setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    // Add Text and Buttons
	    add( startLvl1);
	    add(BorderLayout.CENTER, startLvl2);
	    add(BorderLayout.CENTER, loadLevel);
	    add(BorderLayout.CENTER, help);
	    add(BorderLayout.CENTER, welcome);
	    
	    addKeyListener(controller);
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
	    
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(startLvl1);
	     remove(startLvl2);
	     remove(loadLevel);
	     remove(help);
	     remove(this);
	     };
	     
	     // Add listeners
	     startLvl1.addActionListener(e->phaseOne());
	     startLvl2.addActionListener(e->phaseTwo());
	     loadLevel.addActionListener(e->loadMenu());
	     //e->setPhase(()->initialPhase(), ()->deathMenu(), "savedLevel.xml"
	     help.addActionListener(e->helpMenu());
	     
	     setPreferredSize(new Dimension(1000,600));
	     setResizable(true);
 
	     requestFocus();
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
		level = Levels.loadLevel(next, end, fileName);
		
		//Create the recorder
		Recorder.recorder = new Recorder(level);
		
	    // Set up the viewport
	    LevelView view = new LevelView(level);
	    
	    // KeyListener to listen for controls from the user
	    view.addKeyListener(controller);
	    controller.newInstance(level.getPlayer());
	    
	    view.setFocusable(true);
	    
	    // New timer
	    Timer timer = new Timer(33,unused->{
	      assert SwingUtilities.isEventDispatchThread();
	      if(inPause == false) {
	    	  level.tick();
	    	  view.repaint();
	      }
	    });
	    closePhase.run();//close phase before adding any element of the new phase
	    closePhase=()->{ System.out.println("Removing level"); timer.stop(); remove(view);};
	    add(view);//add the new phase viewport
	    setPreferredSize(getSize());//to keep the current size
	    pack();                     //after pack
	    view.requestFocus();//need to be after pack
	    timer.start();
	    
	   
		
	  }
    
    public Level getCurrentLevel() {
    	return level;
    }
    
    void loadMenu() {
    	setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
 		setLayout(new BorderLayout());
 		
 		// Text and Buttons
 		var header = new JLabel("<html> Save Slots </html>", SwingConstants.CENTER);
 		var save1 = new JLabel("Save 1 Slot");
 		var save2 = new JLabel("Save 2 Slot");
 		var save3 = new JLabel("Save 3 SLot");
 		var save1Button = new JButton("Load Save 1");
 		var save2Button = new JButton("Load Save 2");
 		var save3Button = new JButton("Load Save 3");
 		var text = new JLabel("");
 		var menu = new JButton("Back to Menu");
 		
 		
 		// Setting position and size
 		//header.setBounds(200, 50, 600, 200);
 		save1.setBounds(150, 150, 200, 100);
 		save2.setBounds(400, 150, 200, 100);
 		save3.setBounds(650, 150, 200, 100);
 		
 		save1Button.setBounds(150, 250, 200, 100);
 		save2Button.setBounds(400, 250, 200, 100);
 		save3Button.setBounds(650, 250, 200, 100);
 		
 	    menu.setBounds(400, 500, 200, 50);
 	      
 	    // Setting font and colour
  		header.setFont(LoadedFont.PixeloidSans.getSize(70f));
  		header.setForeground(Color.white);
  		
  		save1.setFont(new Font("Calibri", Font.BOLD, 30));
 		save1.setForeground(Color.white);
 		save2.setFont(new Font("Calibri", Font.BOLD, 30));
 		save2.setForeground(Color.white);
 		save3.setFont(new Font("Calibri", Font.BOLD, 30));
 		save3.setForeground(Color.white);
  	    
  	    // Add Text and Buttons
  	    add(menu);
  	    add(save1);
  	    add(save2);
  	    add(save3);
  	    add(save1Button);
  	    add(save2Button);
  	    add(save3Button);
  	    add(BorderLayout.CENTER, text);
  	    add(BorderLayout.NORTH, header);
  	 
  	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	    setVisible(true);
  		   
  	    // closephase set up
  	    closePhase.run();
  	    closePhase=()->{
  	     remove(header);
  	     remove(save1);
  	     remove(save2);
  	     remove(save3);
  	     remove(save1Button);
  	     remove(save2Button);
  	     remove(save3Button);
  	     remove(text);
  	     remove(menu);
  	     remove(this);
  	    };
 	 	     
  	    // Add listeners
  	    if(isSave1 == true) {
  	    	save1Button.addActionListener(e->setPhase(()->initialPhase(), ()->deathMenu(), "savedLevel1.xml"));
  	    } else {
  	    	save1Button.addActionListener(e->showMessageDialog(null, "There is no saved level in this slot"));
  	    }
  	    
  	    if(isSave2 == true) {
  	    	save2Button.addActionListener(e->setPhase(()->initialPhase(),()->deathMenu(), "savedLevel2.xml"));
  	    } else {
  	    	save2Button.addActionListener(e->showMessageDialog(null, "There is no saved level in this slot"));
  	    }
  	    
  	    if(isSave3 == true) {
  	    	save3Button.addActionListener(e->setPhase(()->initialPhase(), ()->deathMenu(), "savedLevel3.xml"));
  	    } else {
  	    	save3Button.addActionListener(e->showMessageDialog(null, "There is no saved level in this slot"));
  	    }

  	    menu.addActionListener(e->initialPhase());
  	    
  	    setPreferredSize(new Dimension(1000,600));
  	    setResizable(false);
  
 	    pack();
    }
    
    void saveMenu() {
    	setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
 		setLayout(new BorderLayout());
 		
 		// Text and Buttons
 		var header = new JLabel("<html> Save Slots </html>", SwingConstants.CENTER);
 		var save1 = new JLabel("Save 1 Slot");
 		var save2 = new JLabel("Save 2 Slot");
 		var save3 = new JLabel("Save 3 SLot");
 		var save1Button = new JButton("Save to Slot 1");
 		var save2Button = new JButton("Save to Slot 2");
 		var save3Button = new JButton("Save to Slot 3");
 		var text = new JLabel("");
 		var resume = new JButton("Resume Level");
 		var menu = new JButton(" Back to Menu ");
 		
 		// Setting position and size
 		//header.setBounds(200, 50, 600, 200);
 		save1.setBounds(150, 150, 200, 100);
 		save2.setBounds(400, 150, 200, 100);
 		save3.setBounds(650, 150, 200, 100);
 		
 		save1Button.setBounds(150, 250, 200, 100);
 		save2Button.setBounds(400, 250, 200, 100);
 		save3Button.setBounds(650, 250, 200, 100);
 		
 		resume.setBounds(400, 425, 200, 50);
 	    menu.setBounds(400, 500, 200, 50);
 	      
 	    // Setting font and colour
  		header.setFont(LoadedFont.PixeloidSans.getSize(70f));
  		header.setForeground(Color.white);
  		
  		save1.setFont(new Font("Calibri", Font.BOLD, 30));
 		save1.setForeground(Color.white);
 		save2.setFont(new Font("Calibri", Font.BOLD, 30));
 		save2.setForeground(Color.white);
 		save3.setFont(new Font("Calibri", Font.BOLD, 30));
 		save3.setForeground(Color.white);
  	    
  	    // Add Text and Buttons
 		add(resume);
  	    add(menu);
  	    add(save1);
  	    add(save2);
  	    add(save3);
  	    add(save1Button);
  	    add(save2Button);
  	    add(save3Button);
  	    add(BorderLayout.CENTER, text);
  	    add(BorderLayout.NORTH, header);
  	 
  	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  	    setVisible(true);
  		   
  	    // closephase set up
  	    closePhase.run();
  	    closePhase=()->{
  	     remove(header);
  	     remove(save1);
  	     remove(save2);
  	     remove(save3);
  	     remove(save1Button);
  	     remove(save2Button);
  	     remove(save3Button);
  	     remove(text);
  	     remove(menu);
  	     remove(this);
  	    };
 	 	     
  	    // Add listeners
  	    save1Button.addActionListener(e->Levels.saveLevel(getCurrentLevel(), "savedLevel1.xml"));
  	    save1Button.addActionListener(e->isSave1 = true);
  	    save2Button.addActionListener(e->Levels.saveLevel(getCurrentLevel(), "savedLevel2.xml"));
  	    save2Button.addActionListener(e->isSave2 = true);
  	    save3Button.addActionListener(e->Levels.saveLevel(getCurrentLevel(), "savedLevel3.xml"));
  	    save3Button.addActionListener(e->isSave3 = true);
  	    menu.addActionListener(e->initialPhase());
  	    
  	    setPreferredSize(new Dimension(1000,600));
  	    setResizable(false);
  
 	    pack();
    }
	
	/**
	 * Pause menu displaying info for user
	 */
	void pauseMenu() {
		inPause = true;
		
		//JFrame frame = new JFrame("Pause Menu");
		//frame.setLayout(null);
		
		// Set background
//	    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
//		setLayout(new OverlayLayout(frame));
		//frame.setDefaultCloseOperation(JFrame.inPause = false);
		
		JPanel popup = new JPanel();
//		popup.setBounds(200, 300, 300, 300);
		popup.setMaximumSize(new Dimension(600, 400));
		popup.setOpaque(true);
		
		popup.setAlignmentX(0.1f);
		//popup.setAlignmentY(0.3f);
		popup.setBackground(new Color(120, 131, 84));
		popup.setBorder(BorderFactory.createLineBorder(Color.white, 10));
		
		// Text and Buttons
		var pause = new JLabel("<html> Pause Menu </html>", SwingConstants.CENTER);
		var resume = new JButton("Resume");
		var exit = new JButton("Exit");
		var saveLevel = new JButton("Save Level");
		var loadLevel = new JButton("Load level");
		var help = new JButton("Help");
		var restart = new JButton("Restart");
		
		// Setting position and size
		pause.setAlignmentX(0.3f);
	    resume.setBounds(400, 400, 200, 50);
		//resume.setAlignmentX(0.5f);
	    // Setting font and colour
 		pause.setFont(LoadedFont.PixeloidSans.getSize(50f));
 		pause.setForeground(Color.white);
 	    
 	    // Add Text and Buttons
 		popup.add(BorderLayout.NORTH, pause);
 		
 		popup.add(exit);
 		popup.add(saveLevel);
 		popup.add(loadLevel);
 		popup.add(help);
 		popup.add(restart);
 		popup.add(resume);
 		
// 		dialog.setUndecorated(true);
 		popup.setVisible(true);
 		
 		add(popup);
 	 
 		   
 	    // closephase set up
// 	    closePhase.run();
// 	    unPause=()->{
// 	     frame.remove(pause);
// 	     frame.remove(resume);
// 	     frame.remove(this);
// 	    };
        resume.setCursor(new Cursor(Cursor.HAND_CURSOR));  
        
//        // Add listeners
        resume.addActionListener(e->remove(popup));
        resume.addActionListener(e->inPause = false);
 	     
// 	   setSize(new Dimension(1000,600));
// 	    setResizable(false);
 	     
 	    setVisible(true);
  
 	   // pack();
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
		var rules = new JLabel("<html>Movement: Use the WASD keys<br><br>Goal: To collect all the apples and go through the exit<br><br>Tips: Collect pickaxes to break rocks to access apples<br>DON'T get caught by the monster<br><br>For extra help look for sign posts around the map</html>", SwingConstants.CENTER);
		var menu = new JButton("Back to Menu");
		
		// Setting position and size
		header.setBounds(200, 50, 600, 200);
		menu.setBounds(400, 500, 200, 50);
	      
	    // Setting font and colour
 		header.setFont(LoadedFont.PixeloidSans.getSize(50f));
 		header.setForeground(Color.white);
 		
 		rules.setFont(new Font("Calibri", Font.BOLD, 20));
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