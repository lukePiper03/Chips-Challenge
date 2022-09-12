package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.app.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;



public class LevelView extends JPanel{
	
	
	Level l;
	
	public LevelView(Level newLevel) {
		l = newLevel;
	}
	
	
	public void paintComponent(Graphics g) {
	   super.paintComponent(g);//Cell.renderX,Cell.renderY
	   Dimension s = getSize();
	   var centerP = new Point(
	      -s.width/(double)(2*Cell.renderX),
	      -s.height/(double)(2*Cell.renderY));
	   var c = l.getPlayer().getPos().add(centerP);
	   l.getCells().drawAll(c, g, s, l.getPlayer().getPos());
	   l.getPlayer().draw(Img.player.image, g, c, s);
//	   cells.forAll(p.getPos(), 10, cell->cell.draw(g,c,s));
//	   model.entities().forEach(e->e.draw(g, c, s));
	}
}
