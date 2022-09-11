package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Chips extends JFrame{
//	State curState;
	Controller c;
	Runnable closePhase = ()->{};
	
	Chips(){
	    assert SwingUtilities.isEventDispatchThread();
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    c = new Controller();
	    initialPhase();
	    System.out.println("Initialising pane");
	    setVisible(true);
	    addWindowListener(new WindowAdapter(){
	      public void windowClosed(WindowEvent e){closePhase.run();}
	    });
	}
	
	private void initialPhase() {
		var welcome=new JLabel(" Welcome to Chips Challenge! ");
	    var start=new JButton(" Start! ");
	    
	    // closephase set up
	    closePhase.run();
	    closePhase=()->{
	     remove(welcome);
	     remove(start);
	     };
	     
	     
	     // draw buttons
	     add(BorderLayout.CENTER,welcome);
	     add(BorderLayout.SOUTH,start);
	     
	     
	     // add listeners
	     start.addActionListener(e->setPhase());
	     
	     
	     setPreferredSize(new Dimension(1000,600));
	     pack();
	}
	
	void setPhase(){
		System.out.println("Lets go");
	  }
}
