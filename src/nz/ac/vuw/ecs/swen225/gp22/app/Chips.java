package nz.ac.vuw.ecs.swen225.gp22.app;



import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import org.jdom2.JDOMException;

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
	boolean inLevel = false;
	
	boolean isSave1 = false;
	boolean isSave2 = false;
	boolean isSave3 = false;
	boolean isSave = false;
	
	JPanel popup;
		
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
	    setLayout(new BorderLayout());
	    
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
	    startLvl1.setBounds(200, 400, 200, 50);

	    startLvl2.setBounds(600, 400, 200, 50);
	    loadLevel.setBounds(200, 475, 200, 50);
	    help.setBounds(600, 475, 200, 50);
	    
	    // Change cursor type on hover
	    //setCursor(new Cursor(Cursor.HAND_CURSOR));
	    
	    // Add Text and Buttons
	    add(BorderLayout.CENTER, startLvl1);
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
	     setLocationRelativeTo(null);
	}
	
	public void phaseOne(){
	    setPhase(()->phaseTwo(),()->deathMenu(), "level1");
	}
		  
    public void phaseTwo() {
    	setPhase(()->victoryMenu(),()->deathMenu(), "level2");
	}
	
    void setPhase(Runnable next, Runnable end, String fileName){
		System.out.println("Setting level");
		inPause = false;
		
		setLayout(new OverlayLayout(getContentPane()));
		setLocationRelativeTo(null);
		
		// Set up new Level
		try {
			level = Levels.loadLevel(next, end, fileName);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		//Create the recorder
		Recorder.recorder = new Recorder(level);
		
	    // Set up the viewport
	    LevelView view = new LevelView(level);
	    
	    // KeyListener to listen for controls from the user
	    view.addKeyListener(controller);
	    controller.newInstance(level.getPlayer());
	    
	    view.setFocusable(true);
	    
	    var pause = new JButton("Pause");
	    //add(BorderLayout.NORTH, pause);
	    
	    // New timer
	    Timer timer = new Timer(33,unused->{
	      assert SwingUtilities.isEventDispatchThread();
	      if(inPause == false) {
	    	  //remove(pause);
	    	  level.tick();
	    	  view.repaint();
	    	  //add(BorderLayout.NORTH, pause);
	      }
	    });
	    closePhase.run();//close phase before adding any element of the new phase
	    closePhase=()->{ timer.stop(); remove(view);};
	    add(view);//add the new phase viewport
	    setPreferredSize(getSize());//to keep the current size
	    pack();                     //after pack
	    view.requestFocus();//need to be after pack
	    timer.start();
	  }
    
    public Level getCurrentLevel() {
    	return level;
    }
    
    public boolean getPause() {
    	return inPause;
    }
    void loadMenu() {
    	setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
 		setLayout(new BorderLayout());
 		//addKeyListener(controller);
 		
 		// Text and Buttons
 		var header = new JLabel("<html> Save Slots </html>", SwingConstants.CENTER);
 		var save1 = new JLabel("Save 1 Slot");
 		var save2 = new JLabel("Save 2 Slot");
 		var save3 = new JLabel("Save 3 SLot");
 		
 		var timeLeft = new JLabel("Time Left: ");
 		var level = new JLabel("Level: ");
 		var treasuresToCollect = new JLabel("Treasure To Collect: ");
 		
 		var save1Button = new JButton("Load Save 1");
 		var save2Button = new JButton("Load Save 2");
 		var save3Button = new JButton("Load Save 3");
 		var text = new JLabel("");
 		var menu = new JButton("Back to Menu");
 		
 		
 		// Setting position and size
 		save1.setBounds(150, 100, 200, 100);
 		save2.setBounds(400, 100, 200, 100);
 		save3.setBounds(650, 100, 200, 100);
 		
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
  	     remove(this);
  	    };
 	 	     
  	    // Add listeners
  	    if(isSave1 == true) {
  	    	save1Button.addActionListener(e->setPhase(()->initialPhase(), ()->deathMenu(), "savedLevel1"));
  	    } else {
  	    	save1Button.addActionListener(e->showMessageDialog(null, "There is no saved level in this slot"));
  	    }
  	    
  	    if(isSave2 == true) {
  	    	save2Button.addActionListener(e->setPhase(()->initialPhase(),()->deathMenu(), "savedLevel2"));
  	    } else {
  	    	save2Button.addActionListener(e->showMessageDialog(null, "There is no saved level in this slot"));
  	    }
  	    
  	    if(isSave3 == true) {
  	    	save3Button.addActionListener(e->setPhase(()->initialPhase(), ()->deathMenu(), "savedLevel3"));
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
 		//addKeyListener(controller);
 		
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
  	     remove(this);
  	    };
 	 	     
  	    // Add listeners
  	    if(level == null) {
  	    	save1Button.addActionListener(e->showMessageDialog(null, "You must be playing a level to save it"));
  	    	save2Button.addActionListener(e->showMessageDialog(null, "You must be playing a level to save it"));
  	    	save3Button.addActionListener(e->showMessageDialog(null, "You must be playing a level to save it"));
  	    } else {
	  	    save1Button.addActionListener(e->{
				try {
					Levels.saveLevel(getCurrentLevel(), "savedLevel1");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
	  	    save1Button.addActionListener(e->isSave1 = true);
	  	    save2Button.addActionListener(e->{
				try {
					Levels.saveLevel(getCurrentLevel(), "savedLevel2");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
	  	    save2Button.addActionListener(e->isSave2 = true);
	  	    save3Button.addActionListener(e->{
				try {
					Levels.saveLevel(getCurrentLevel(), "savedLevel3");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
	  	    save3Button.addActionListener(e->isSave3 = true);
  	    }
  	    
  	    menu.addActionListener(e->initialPhase());
  	    
  	    setPreferredSize(new Dimension(1000,600));
  	    setResizable(false);
  
 	    pack();

    }
	
    void closePausePopup(JPanel popup) {
    	remove(popup);
    	inPause = false;
    }
    
	/**
	 * Pause menu displaying info for user
	 */
	void pauseMenu() {
		inPause = true;

		popup = new JPanel();
		popup.setMaximumSize(new Dimension(600, 400));
		popup.setOpaque(true);
		popup.setLayout(new BorderLayout());
		
		popup.setAlignmentX(0.1f);
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
		var text = new JLabel("");
		
		// Setting position and size
	    resume.setBounds(200, 300, 200, 50);
	    help.setBounds(50, 100, 200, 50);
	    exit.setBounds(350, 100, 200, 50);
	    saveLevel.setBounds(50, 200, 200, 50);
	    loadLevel.setBounds(350, 200, 200, 50);
	    
	    // Setting font and colour
 		pause.setFont(LoadedFont.PixeloidSans.getSize(50f));
 		pause.setForeground(Color.white);
 	    
 	    // Add Text and Buttons
 		popup.add(BorderLayout.NORTH, pause);
 		
 		popup.add(BorderLayout.CENTER, exit);
 		popup.add(BorderLayout.CENTER, saveLevel);
 		popup.add(BorderLayout.CENTER, loadLevel);
 		popup.add(BorderLayout.CENTER, help);
 		popup.add(BorderLayout.CENTER, restart);
 		popup.add(BorderLayout.CENTER, resume);
 		popup.add(BorderLayout.CENTER, text);
 		
 		popup.setVisible(true);
 		
 		add(popup);
 		
        resume.setCursor(new Cursor(Cursor.HAND_CURSOR));  
        
        // Add listeners
        resume.addActionListener(e->closePausePopup(popup));
        help.addActionListener(e->helpMenu());
        exit.addActionListener(e->initialPhase());
        saveLevel.addActionListener(e->isSave = true);
        saveLevel.addActionListener(e->{
			try {
				Levels.saveLevel(getCurrentLevel(), "savedLevel");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
        saveLevel.addActionListener(e->closePausePopup(popup));
        saveLevel.addActionListener(e->saveMenu());
 	    loadLevel.addActionListener(e->loadMenu());
 	    
 	    setVisible(true);
  
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
		var rules = new JLabel("<html>Movement: Use the WASD keys<br><br>Goal: To collect all the apples and go through the exit<br><br>Tips: Collect pickaxes to break rocks to access apples<br>DON'T get caught by the monster<br><br>For extra help look for sign posts around the map</html>");
		var menu = new JButton("Back to Menu");
		var resume = new JButton("Resume");
		
		var up = new JButton("Up: w");
		var left = new JButton("Left: a");
		var down = new JButton("Down: s");
		var right = new JButton("Right: d");
		var startGame1 = new JLabel("Start game - level 1: Ctrl 1");
		var startGame2 = new JLabel("Start game - level 2: Ctrl 2");
		var exitGame = new JLabel("Exit game: Ctrl x");
		var saveGame = new JLabel("Save game: Ctrl s");
		var loadGame = new JLabel("Load game: Ctrl r");
		var pause = new JLabel("Pause: SpaceBar");
		
		Map<String, Integer> movements = new HashMap<String, Integer>();
	    movements.put("up", KeyEvent.VK_W);
		movements.put("left", KeyEvent.VK_A);
		movements.put("down", KeyEvent.VK_S);
		movements.put("right", KeyEvent.VK_D);
	    
		// Setting position and size
		header.setBounds(200, 50, 600, 200);
		menu.setBounds(400, 500, 200, 50);
		resume.setBounds(400, 450, 200, 50);
		
		up.setBounds(600, 275, 200, 50);
		left.setBounds(600, 325, 200, 50);
		down.setBounds(600, 375, 200, 50);
		right.setBounds(600, 425, 200, 50);
		startGame1.setBounds(610, 100, 200, 50);
		startGame2.setBounds(610, 125, 200, 50);
		exitGame.setBounds(610, 150, 200, 50);
		saveGame.setBounds(610, 175, 200, 50);
		loadGame.setBounds(610, 200, 200, 50);
		pause.setBounds(610, 225, 200, 50);
	      
	    // Setting font and colour
 		header.setFont(LoadedFont.PixeloidSans.getSize(50f));
 		header.setForeground(Color.white);
 		
 		setFont(new Font("Calibri", Font.BOLD, 16));
 		setForeground(Color.white);
 		rules.setBounds(50, 200, 500, 400);
 		
 		// Add Text and Buttons
 		add(up);
	    add(left);
	    add(down);
	    add(right);
	    add(startGame1);
	    add(startGame2);
	    add(exitGame);
	    add(saveGame);
	    add(loadGame);
	    add(pause);
	    add(BorderLayout.CENTER, menu);
	    if(inPause == true) {
	    	add(BorderLayout.CENTER, resume);
	    }
	    add(rules);
	    add(BorderLayout.NORTH, header);
	    
	 
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setVisible(true);
		   
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(this);
	    };
	    
	    up.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if(updateKey(movements, e) == true) {
	    			char keyChar = e.getKeyChar();
			    	up.setText("Up: " + keyChar);
	    		}
	    		
	    	}
	    	
	    });
	    
	    left.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if(updateKey(movements, e) == true) {
		    		char keyChar = e.getKeyChar();
			    	left.setText("Left: " + keyChar);
	    		}
	    	}
	    });
	    
	    down.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if(updateKey(movements, e) == true) {
		    		char keyChar = e.getKeyChar();
			    	down.setText("Down: " + keyChar);
	    		}
	    	}
	    });
	    
	    right.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		if(updateKey(movements, e) == true) {
		    		char keyChar = e.getKeyChar();
			    	right.setText("Right: " + keyChar);
	    		}
	    	}
	    });
	    
	    // Add listeners
 	    menu.addActionListener(e->initialPhase());
 	    
 	    setPreferredSize(new Dimension(1000,600));
 	    setResizable(false);
 
	    pack();
	}

	private boolean checkForDuplicates(Map<String, Integer> movements, KeyEvent e) {
	  int keyCode = e.getKeyCode();
	  for(Entry<String, Integer> entry : movements.entrySet()) {
		  if(keyCode == entry.getValue() || keyCode == KeyEvent.VK_SPACE) { 
			  JOptionPane.showMessageDialog(null, "The key " + e.getKeyChar() + " is already in use for the " + entry.getKey() + " movement");
			  return true;
		  }
	  }
	  return false;
	}
	
	public boolean updateKey(Map<String, Integer> movements, KeyEvent e) {
		int keyCode = e.getKeyCode();
		// Check a move with already that key set as its action
		if(checkForDuplicates(movements, e) == false) {
			// Display in terminal
			char keyChar = e.getKeyChar();
    		System.out.println("New Right Key is " + keyChar);
    		// Update keys in controller
    		movements.put("right", keyCode);
    		Controller.updateMoves(movements);
    		return true;
		}
		return false;
	}
}