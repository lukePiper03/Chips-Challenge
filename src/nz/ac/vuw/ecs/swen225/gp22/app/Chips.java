package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import static javax.swing.JOptionPane.showMessageDialog;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.global.Direction;
import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.recorder.Replay;
import nz.ac.vuw.ecs.swen225.gp22.renderer.LevelView;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import org.jdom2.JDOMException;



/**
 * Chips class.
 *
 * @author lukepiper
 *
 */
public class Chips extends JFrame {
  /**
   * serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  Controller controller;
  Level level;
  LevelView view;
  Recorder rec;
  AtomicInteger count;
  String fileName;
  Replay replay;

  boolean inPause;
  boolean inLevel = false;

  boolean isSave = false;

  Map<String, Integer> movements = new HashMap<String, Integer>();

  JPanel popup;

  Runnable closePhase = () -> {
  };

  /**
   * Chips Controller.
   */
  public Chips() {
    assert SwingUtilities.isEventDispatchThread();
    controller = new Controller(this);
    initialPhase();
    System.out.println("Initialising pane");
    movements.put("up", KeyEvent.VK_W);
    movements.put("left", KeyEvent.VK_A);
    movements.put("down", KeyEvent.VK_S);
    movements.put("right", KeyEvent.VK_D);
    addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        closePhase.run();
      }
    });
  }

  /**
   * Start menu of the game.
   */
  void initialPhase() {
    inPause = false;
    // Set background
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());

    // Create Text and Buttons
    var welcome = new JLabel("<html>Bunny's<br>Challenge!</html>", SwingConstants.CENTER);
    // Setting font and colour
    welcome.setFont(LoadedFont.PixeloidSans.getSize(70f));
    welcome.setForeground(Color.white);
    
    
    var startLvl1 = new CustomButton("Start! (Lvl 1)");
    startLvl1.setBounds(100, 400, 200, 50);
    var startLvl2 = new CustomButton("Start! (Lvl 2)");
    startLvl2.setBounds(100, 475, 200, 50);
    var loadLevel = new CustomButton("Load saved Level");
    loadLevel.setBounds(400, 400, 200, 50);
    var help = new CustomButton("Help");
    help.setBounds(700, 400, 200, 50);
    var replay = new CustomButton("Watch replay");
    replay.setBounds(400, 475, 200, 50);
    var quit = new CustomButton("Quit");
    quit.setBounds(700, 475, 200, 50);

    // Add Text and Buttons
    add(BorderLayout.CENTER, startLvl1);
    add(BorderLayout.CENTER, startLvl2);
    add(BorderLayout.CENTER, loadLevel);
    add(BorderLayout.CENTER, help);
    add(BorderLayout.CENTER, replay);
    add(BorderLayout.CENTER, quit);
    add(BorderLayout.CENTER, welcome);

    addKeyListener(controller);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    if (level != null) {
      level.gameOver();
    }
    // closephase set up
    closePhase.run();
    closePhase = () -> {
      remove(startLvl1);
      remove(startLvl2);
      remove(loadLevel);
      remove(help);
      remove(replay);
      remove(quit);
      remove(welcome);
      remove(this);
    };

    // Add listeners
    startLvl1.addActionListener(e -> phaseOne());
    startLvl2.addActionListener(e -> phaseTwo());
    loadLevel.addActionListener(e -> loadMenu());
    help.addActionListener(e -> helpMenu());
    replay.addActionListener(e -> setRecorder());
    quit.addActionListener(e -> System.exit(0));

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    requestFocus();
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Call setPhase to load the first level.
   */
  public void phaseOne() {
    setPhase(() -> phaseTwo(), () -> deathMenu(), "level1");
  }

  /**
   * Call setPahse to load the second level.
   */
  public void phaseTwo() {
    setPhase(() -> victoryMenu(), () -> deathMenu(), "level2");
  }

  void setPhase(Runnable next, Runnable end, String f) {
    System.out.println("Setting level");
    inPause = false;
    fileName = f;

    count = new AtomicInteger();
    rec = new Recorder(fileName, count.get());

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
    // Create the recorder
    // Recorder.recorder = new Recorder(fileName, time);

    // Set up the viewport
    view = new LevelView(level);

    // KeyListener to listen for controls from the user
    view.addKeyListener(controller);
    controller.newInstance(level.getPlayer());
    view.setFocusable(true);

    // New timer
    Timer timer = new Timer(33, unused -> {
      assert SwingUtilities.isEventDispatchThread();
      if (inPause == false) {
        level.tick();
        view.repaint();
        rec.savePlayerMoveEvent(count.getAndIncrement(), level.getPlayer().direction());
      }
    });
    closePhase.run(); // close phase before adding any element of the new phase
    closePhase = () -> {
      timer.stop();
      try {
        rec.saveToFile(fileName + "_replay");
      } catch (Exception e) {
        e.printStackTrace();
      }
      remove(view);
    };
    add(view); // add the new phase viewport
    setPreferredSize(getSize()); // to keep the current size
    pack(); // after pack
    view.requestFocus(); // need to be after pack
    timer.start();
  }

  void setRecorder() {
    System.out.println("Making replay");
    // fileName = f;

    JFileChooser j = new JFileChooser(new File("./"));
    j.setFileFilter(new FileNameExtensionFilter("Just XML files", "xml"));
    j.showSaveDialog(this);
    fileName = j.getSelectedFile().getAbsolutePath();

    count = new AtomicInteger();
    // rec = new Recorder(fileName, count.get());

    setLayout(new OverlayLayout(getContentPane()));
    setLocationRelativeTo(null);

    // Create _replay
    replay = null;
    try {
      replay = new Replay(fileName);
    } catch (JDOMException | IOException e1) {
      e1.printStackTrace();
    }

    try {
      if (replay != null) {
        level = Levels.loadLevel(() -> initialPhase(), () -> deathMenu(), replay.getLevelPath());
      }
    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

    if (replay != null) {
      replay.setController((Direction d) -> level.getPlayer().direction(d));
      replay.setAdvanceByTick(()->{level.tick(); view.repaint();});
    }

    // Set up the viewport
    view = new LevelView(level);

    // KeyListener to listen for controls from the user
    view.addKeyListener(controller);
    controller.newInstance(level.getPlayer());

    view.setFocusable(true);

    closePhase.run(); // close phase before adding any element of the new phase
    closePhase = () -> {
    	replay.stop();
    };
    setLayout(new BorderLayout());
    add(BorderLayout.SOUTH, replay);
    
    add(view); // add the new phase viewport
    setPreferredSize(getSize()); // to keep the current size
    pack(); // after pack
    view.requestFocus(); // need to be after pack
   // timer.start();
  }

  /**
   * Return current level.
   *
   * @return level
   */
  public Level getCurrentLevel() {
    return level;
  }

  /**
   * Return whether game is in pause.
   *
   * @return inPause
   */
  public boolean getPause() {
    return inPause;
  }

  /**
   * Load menu to allow users to load saved levels.
   */
  void loadMenu() {
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());
    // addKeyListener(controller);

    // Header text
    var header = new JLabel("<html> Save Slots </html>", SwingConstants.CENTER);
    // Setting font and colour
    header.setFont(LoadedFont.PixeloidSans.getSize(70f));
    header.setForeground(Color.white);
    
    // Making buttons and setting their bounds
    var save1 = new JLabel("Save 1 Slot");
    save1.setBounds(150, 100, 200, 100);
    var save2 = new JLabel("Save 2 Slot");
    save2.setBounds(400, 100, 200, 100);
    var save3 = new JLabel("Save 3 SLot");
    save3.setBounds(650, 100, 200, 100);

    var save1Button = new CustomButton("Empty");
    save1Button.setBounds(150, 250, 200, 100);
    
    var save2Button = new CustomButton("Empty");
    save2Button.setBounds(400, 250, 200, 100);
    
    var save3Button = new CustomButton("Empty");
    save3Button.setBounds(650, 250, 200, 100);
    var menu = new CustomButton("Back to Menu");
    menu.setBounds(400, 500, 200, 50);

    // Setting fonts of buttons
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
    var text = new JLabel("");
    add(BorderLayout.CENTER, text);
    add(BorderLayout.NORTH, header);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // closephase set up
    closePhase.run();
    closePhase = () -> {
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
    File save1File = new File("./levels/savedLevel1.xml");
    File save2File = new File("./levels/savedLevel2.xml");
    File save3File = new File("./levels/savedLevel3.xml");
    loadLevel(save1File, "savedLevel1", save1Button);
    loadLevel(save2File, "savedLevel2", save2Button);
    loadLevel(save3File, "savedLevel3", save3Button);

    menu.addActionListener(e -> initialPhase());

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    pack();
  }

  /**
   * Load a saved level.
   *
   * @param File file
   * @param String fileName
   * @param JButton button
   */
  void loadLevel(File file, String fileName, JButton button) {
    if (file.exists()) {
      try {
        level = Levels.loadLevel(() -> {
        }, () -> {
        }, fileName);
        button.setText("<html>Level: " + level.getLevelNum() + "<br>Time Left: "
            + (int) level.getCountdown()
            + "<br>Chips Left: " + level.getPlayer().treasuresToCollect() + "</html>");
      } catch (IOException | JDOMException e1) {
        button.setText("Corrupt file");
      }
      button.addActionListener(e -> {
        setPhase(() -> initialPhase(), () -> deathMenu(), fileName);
        System.out.println("Loading save slot");
      });
    } else {
      button.addActionListener(e -> showMessageDialog(null,
          "There is no saved level in this slot"));
    }
  }

  /**
   * Save menu to allow users to save the current level they are on.
   */
  void saveMenu() {
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());
    // addKeyListener(controller);

    // Header text
    var header = new JLabel("<html> Save Slots </html>", SwingConstants.CENTER);
    // Setting font and colour
    header.setFont(LoadedFont.PixeloidSans.getSize(70f));
    header.setForeground(Color.white);
    
    // Making buttons and setting bounds
    var save1 = new JLabel("Save 1 Slot");
    save1.setBounds(150, 150, 200, 100);
    var save2 = new JLabel("Save 2 Slot");
    save2.setBounds(400, 150, 200, 100);
    var save3 = new JLabel("Save 3 SLot");
    save3.setBounds(650, 150, 200, 100);
    var save1Button = new CustomButton("Save to Slot 1");
    save1Button.setBounds(150, 250, 200, 100);
    var save2Button = new CustomButton("Save to Slot 2");
    save2Button.setBounds(400, 250, 200, 100);
    var save3Button = new CustomButton("Save to Slot 3");
    save3Button.setBounds(650, 250, 200, 100);
    var resume = new CustomButton("Resume Level");
    resume.setBounds(400, 425, 200, 50);
    var menu = new CustomButton(" Back to Menu ");
    menu.setBounds(400, 500, 200, 50); 

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
    var text = new JLabel("");
    add(BorderLayout.CENTER, text);
    add(BorderLayout.NORTH, header);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // closephase set up
    closePhase.run();
    closePhase = () -> {
      remove(resume);
      remove(menu);
      remove(save1);
      remove(save2);
      remove(save3);
      remove(save1Button);
      remove(save2Button);
      remove(save3Button);
      remove(text);
      remove(header);
      remove(this);
    };

    savingLevel(level, save1Button, "savedLevel1");
    savingLevel(level, save2Button, "savedLevel2");
    savingLevel(level, save3Button, "savedLevel3");

    resume.addActionListener(e -> resetPhase());
    menu.addActionListener(e -> initialPhase());

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    pack();

  }

  void savingLevel(Level level, JButton button, String fileName) {
    // Add listeners
    if (level == null) {
      button.addActionListener(e -> showMessageDialog(null,
          "You must be playing a level to save it"));

    } else {
      button.addActionListener(e -> {
        try {
          Levels.saveLevel(getCurrentLevel(), fileName);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        System.out.println("Saving to save slot");
      });
    }
  }

  /**
   * Reset to the current level the user was completing.
   */
  void resetPhase() {
    inPause = false;
    System.out.println("Resetting");
    setLayout(new OverlayLayout(getContentPane()));
    setLocationRelativeTo(null);

    controller.newInstance(level.getPlayer());
    view.setFocusable(true);

    Timer timer = new Timer(33, unused -> {
      assert SwingUtilities.isEventDispatchThread();
      if (inPause == false) {
        level.tick();

        view.repaint();
        rec.savePlayerMoveEvent(count.getAndIncrement(), level.getPlayer().direction());
      }
    });
    closePhase.run(); // close phase before adding any element of the new phase
    closePhase = () -> {
      timer.stop();
      try {
        rec.saveToFile(fileName + "_replay");
      } catch (Exception e) {
        e.printStackTrace();
      }
      remove(view);
    };
    add(view); // add the new phase viewport
    setPreferredSize(getSize()); // to keep the current size
    pack(); // after pack
    view.requestFocus(); // need to be after pack
    timer.start();
  }

  /**
   * Close pause popup.
   *
   * @param popup - JPanel to close
   */
  void closePausePopup(JPanel popup) {
    remove(popup);
    inPause = false;
  }

  /**
   * Pause menu displaying info for user.
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

    // Header text
    var pause = new JLabel("<html> Pause Menu </html>", SwingConstants.CENTER);
    // Setting font and colour
    pause.setFont(LoadedFont.PixeloidSans.getSize(50f));
    pause.setForeground(Color.white);
    
    // Making buttons and setting bounds
    var resume = new JButton("Resume");
    resume.setBounds(200, 300, 200, 50);
    var exit = new JButton("Exit");
    exit.setBounds(350, 100, 200, 50);
    var saveLevel = new JButton("Save Level");
    saveLevel.setBounds(50, 200, 200, 50);
    var loadLevel = new JButton("Load level");
    loadLevel.setBounds(350, 200, 200, 50);
    var help = new JButton("Help");
    help.setBounds(50, 100, 200, 50);
    var restart = new JButton("Restart");
    var text = new JLabel("");

    // Add Text and Buttons
    popup.add(BorderLayout.NORTH, pause);

    popup.add(exit);
    popup.add(saveLevel);
    popup.add(loadLevel);
    popup.add(help);
    popup.add(restart);
    popup.add(resume);
    popup.add(BorderLayout.CENTER, text);

    popup.setVisible(true);

    add(popup);

    resume.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add listeners
    resume.addActionListener(e -> closePausePopup(popup));
    help.addActionListener(e -> helpMenu());
    exit.addActionListener(e -> initialPhase());
    saveLevel.addActionListener(e -> {
      isSave = true;
      try {
        Levels.saveLevel(getCurrentLevel(), "savedLevel");
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    saveLevel.addActionListener(e -> {
      closePausePopup(popup);
      saveMenu();
    });
    loadLevel.addActionListener(e -> loadMenu());

    setVisible(true);

    pack();
  }

  /**
   * Menu for if player has died during the game.
   */
  void deathMenu() {
    // Set background
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());

    // Text and Buttons
    var deathText = new JLabel("Oh no, you DIED", SwingConstants.CENTER);
    var menu = new CustomButton("Back to Menu");

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
    closePhase = () -> {
      remove(deathText);
      remove(menu);
      remove(this);
    };

    // Add listeners
    menu.addActionListener(e -> initialPhase());

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    pack();
  }

  /**
   * Victory menu.
   */
  void victoryMenu() {
    // Set background
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());

    // Text and Buttons
    var victoryText = new JLabel("<html>You have finished Bunny's Challenge!<br>"
        + "Congratulations!</html>",
        SwingConstants.CENTER);
    var menu = new CustomButton(" Back to Menu ");

    // Setting position and size
    victoryText.setBounds(500, 50, 600, 200);
    menu.setBounds(400, 400, 200, 50);

    // Setting font and colour
    victoryText.setFont(LoadedFont.PixeloidSans.getSize(40f));
    victoryText.setForeground(Color.white);

    // Add Text and Buttons
    add(BorderLayout.CENTER, menu);
    add(BorderLayout.CENTER, victoryText);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // closephase set up
    closePhase.run();
    closePhase = () -> {
      remove(this);
    };

    // Add listeners
    menu.addActionListener(e -> initialPhase());

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    pack();
  }

  /**
   * Help Menu to let users change movement keyBinds and give information on how
   * to play.
   */
  void helpMenu() {
    // Set background
    setContentPane(new JLabel(new ImageIcon(Img.Background.image)));
    setLayout(new BorderLayout());

    // Create text buttons
    var header = new JLabel("A little stuck?", SwingConstants.CENTER);
    header.setBounds(300, 50, 600, 200);
    // Setting font and colour
    header.setFont(LoadedFont.PixeloidSans.getSize(50f));
    header.setForeground(Color.white);
    var rules = new JLabel(
        "<html>Movement: Use the WASD keys<br><br>Goal: Collect all chips and go through exit<br>"
        + "<br>Tips:<br>Pickaxe breaks rocks<br>Axe breaks wood<br>Shovel breaks dirt<br>"
        + "Hoe breaks plants<br>DON'T get caught by the monster<br><br>"
        + "For extra help look for sign posts around the map</html>",
        SwingConstants.CENTER);
    rules.setFont(new Font("Calibri", Font.BOLD, 20));
    rules.setForeground(Color.white);
    rules.setBounds(50, 100, 500, 400);
    
    var menu = new CustomButton("Back to Menu");
    menu.setBounds(400, 500, 200, 50);
    var resume = new CustomButton("Resume");
    resume.setBounds(400, 450, 200, 50);

    var up = new CustomButton("Up: " + (char) ((int) movements.get("up")));
    up.setBounds(650, 275, 200, 50);
    var left = new CustomButton("Left: " + (char) ((int) movements.get("left")));
    left.setBounds(650, 325, 200, 50);
    var down = new CustomButton("Down: " + (char) ((int) movements.get("down")));
    down.setBounds(650, 375, 200, 50);
    var right = new CustomButton("Right: " + (char) ((int) movements.get("right")));
    right.setBounds(650, 425, 200, 50);
    var startGame1 = new JLabel("Start game - level 1: Ctrl 1");
    startGame1.setBounds(650, 100, 200, 50);
    var startGame2 = new JLabel("Start game - level 2: Ctrl 2");
    startGame2.setBounds(650, 125, 200, 50);
    var exitGame = new JLabel("Exit game: Ctrl x");
    exitGame.setBounds(650, 150, 200, 50);
    var saveGame = new JLabel("Save game: Ctrl s");
    saveGame.setBounds(650, 175, 200, 50);
    var loadGame = new JLabel("Load game: Ctrl r");
    loadGame.setBounds(650, 200, 200, 50);
    var pause = new JLabel("Pause: SpaceBar");
    pause.setBounds(660, 225, 200, 50);

    setFont(new Font("Calibri", Font.BOLD, 16));
    setForeground(Color.white);

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
    add(menu);
    if (inPause == true) {
      add(resume);
    }
    add(rules);
    var text = new JLabel("");
    add(BorderLayout.CENTER, text);
    add(BorderLayout.NORTH, header);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // closephase set up
    closePhase.run();
    closePhase = () -> {
      remove(up);
      remove(left);
      remove(down);
      remove(right);
      remove(startGame1);
      remove(startGame2);
      remove(exitGame);
      remove(saveGame);
      remove(loadGame);
      remove(pause);
      remove(header);
      remove(menu);
      remove(resume);
      remove(rules);
      remove(text);
      remove(this);
    };

    up.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (updateKey(movements, e, "up") == true) {
          up.setText("Up: " + Character.toUpperCase(e.getKeyChar()));
        }
      }
    });

    left.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (updateKey(movements, e, "left") == true) {
          left.setText("Left: " + Character.toUpperCase(e.getKeyChar()));
        }
      }
    });

    down.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (updateKey(movements, e, "down") == true) {
          down.setText("Down: " + Character.toUpperCase(e.getKeyChar()));
        }
      }
    });

    right.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (updateKey(movements, e, "right") == true) {
          right.setText("Right: " + Character.toUpperCase(e.getKeyChar()));
        }
      }
    });

    resume.addActionListener(e -> resetPhase());
    // Add listeners
    menu.addActionListener(e -> initialPhase());

    setPreferredSize(new Dimension(1000, 600));
    setResizable(false);

    pack();
  }

  /**
   * Check for duplicates in the controller map.
   *
   * @param Map movements
   * @param KeyEvent e
   * @return success boolean of operation
   */
  private boolean checkForDuplicates(Map<String, Integer> movements, KeyEvent e) {
    int keyCode = e.getKeyCode();
    for (Entry<String, Integer> entry : movements.entrySet()) {
      if (keyCode == entry.getValue() || keyCode == KeyEvent.VK_SPACE) {
        JOptionPane.showMessageDialog(null,
            "The key " + e.getKeyChar() + " is already in use for the "
            + entry.getKey() + " movement");
        return true;
      }
    }
    return false;
  }

  /**
   * Update Key inside the controller map.
   *
   * @param movements - keyBind movements
   * @param e - current KeyEvent
   * @param key - String of key
   * @return success boolean of operation
   */
  public boolean updateKey(Map<String, Integer> movements, KeyEvent e, String key) {
    int keyCode = e.getKeyCode();
    // Check a move with already that key set as its action
    if (checkForDuplicates(movements, e) == false) {
      // Update keys in controller
      movements.put(key, keyCode);
      Controller.updateMoves(movements);
      return true;
    }
    return false;
  }
}