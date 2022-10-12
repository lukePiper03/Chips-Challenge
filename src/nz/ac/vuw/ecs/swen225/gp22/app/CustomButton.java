package nz.ac.vuw.ecs.swen225.gp22.app;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;

/**
 * @author lukepiper
 *
 */
public class CustomButton extends JButton{
	CustomButton(String text) {
		this.setText(text);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setFont(LoadedFont.PixeloidSans.getSize(14f));
		this.setForeground(Color.white);
		this.setBackground(new Color(110, 74, 47, 150));
		this.setBorder(BorderFactory.createLineBorder(Color.white, 4));
		this.setOpaque(true);
	}
}
