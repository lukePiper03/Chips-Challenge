package nz.ac.vuw.ecs.swen225.gp22.domain;

import javax.swing.JOptionPane;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;

/**
 * Represents an InfoField entity. Displays message.
 * @author Linda Zhang 300570498
 */
class InfoField implements Entity{
	private boolean switchOn = false; // message should only display the first tick
	private Point pos; private String message;
	InfoField(Point pos, String message){this.pos = pos; this.message = message;}
	
	public boolean onInteraction(Player p, Cells cells) {
		if(!p.getPos().equals(pos)) { //player not on Info, do nothing
			switchOn = true;
			return false;
		} 
		//display message
		if(switchOn) {
			JOptionPane.showMessageDialog(null, message);
			switchOn = false;
			return true;
		}
		return false;
	}
	
	public Img getImage() {return Img.water;} //change later
}