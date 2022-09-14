package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.app.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;



public class LevelView extends JPanel{
	// level
	Level l;
	
	// rendering variables
	private final int renderSize = 64;
	
	public LevelView(Level newLevel) {
		l = newLevel;
	}
	
	
	public void paintComponent(Graphics g) {
	   super.paintComponent(g);//Cell.renderX,Cell.renderY
	   Dimension s = getSize();
	   var centerP = new Point(
	      -s.width/(double)(2*renderSize),
	      -s.height/(double)(2*renderSize));
	   var c = l.getPlayer().getPos().add(centerP);
	   drawMap(g, c, s, l.getPlayer().getPos());
	   l.getPlayer().draw(Img.player.image, g, c, s);
//	   l.getCells().drawAll(c, g, s, l.getPlayer().getPos());
//	   l.getPlayer().draw(Img.player.image, g, c, s);
	   
	   
	   
//	   cells.forAll(p.getPos(), 10, cell->cell.draw(g,c,s));
//	   model.entities().forEach(e->e.draw(g, c, s));
	}
	
	void drawMap(Graphics g, Point centre, Dimension size, Point player){
//		Point p = centre;
		Cells c = l.getCells();
		
		List<Cell> wallTiles = new ArrayList<>();
		int range = 10;
		for(int x=(int)player.x()-range;x<=player.x()+range;x++){
		  for(int y=(int) (player.y()-range);y<=player.y()+range;y++){
			  if(c.get(x, y).isSolid() ) {
				  wallTiles.add(c.get(x, y));
			  } else {
				  drawCell(g, centre, size, player, c.get(x, y));
			  }
		  }
		}

		// draw walls on top
		for(Cell curCell : wallTiles) {
			drawCell(g, centre, size, player, curCell);
		
		}
		
	}
	
	void drawCell(Graphics g, Point center, Dimension size, Point player, Cell c) {
		int w1=c.x()*renderSize-(int)(center.x()*renderSize);
	    int h1=c.y()*renderSize-(int)(center.y()*renderSize);
	    int w2=w1+renderSize;
	    int h2=h1+renderSize;
	    double dist = Math.hypot(c.x()- player.x(), c.y() - player.y()) - 2;
	    
	    var isOut=h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
	    if(isOut){ return; }
	    g.drawImage(c.getImage().image, w1, h1, w2, h2, 0, 0, renderSize, renderSize, null);
//	    imgs.forEach(i->g.drawImage(i.image,w1,h1,w2,h2,0,0,Cell.renderX,Cell.renderY,null));
//	    if(imgs.contains(Img.wall)) {
//	    	g.drawImage(Img.wall.image,w1,h1,w2+8,h2+8,0,0,renderSize+8,renderSize+8,null);
//	    }
	}

}
