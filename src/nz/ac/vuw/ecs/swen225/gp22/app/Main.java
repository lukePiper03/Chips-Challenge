package nz.ac.vuw.ecs.swen225.gp22.app;
import javax.swing.SwingUtilities;

public class Main {
	/**
	 * @param a 
	 */
	public static void main(String[]a){
	    SwingUtilities.invokeLater(Chips::new);
	}
}
