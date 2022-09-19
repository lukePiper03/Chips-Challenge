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
	   super.paintComponent(g);
	   Dimension s = getSize();
	   
	   // find centre of map relative to player
	   var centerP = new Point(
	      -s.width/(int)(2*renderSize),
	      -s.height/(int)(2*renderSize));
	   var c = l.getPlayer().getPos().add(centerP);
	   
	   // work out difference between positions to make movement animation
	   Point pos = l.getPlayer().getPos();
	   Point oldPos = l.getPlayer().getOldPos();
	   Point diff = oldPos.distance(pos);
	   float xShift =  diff.x() * (1-l.getPlayer().getMoveTime()); 
	   float yShift = diff.y() * (1-l.getPlayer().getMoveTime());
	   
	   // draw map and player
	   drawMap(g, c, s, l.getPlayer().getPos(), xShift, yShift);
	   l.getPlayer().draw(Img.player.image, g, c, s);

	 
	}
	
	/// MAKE A DRAWPLAYER METHOD. FIND THE GETMOVETIME TO CHANGE THE SPRITESHEET AS WELL AS DIRECTION
	// EACH DIR MOVEMENT HAS 4 FRAMES WHICH WE CAN USE FOR EACH 0.2 OF TIME.
	
	
	void drawMap(Graphics g, Point centre, Dimension size, Point player, float xShift, float yShift){
		Cells c = l.getCells();
		
		List<Cell> wallTiles = new ArrayList<>();
		int range = 10;
		for(int x=(int)player.x()-range;x<=player.x()+range;x++){
		  for(int y=(int) (player.y()-range);y<=player.y()+range;y++){
			  if(c.get(x, y).isSolid() ) {
				  wallTiles.add(c.get(x, y));
			  } else {
				  drawCell(g, centre, size, player, c.get(x, y), xShift, yShift);
			  }
		  }
		}

		// draw walls on top
		for(Cell curCell : wallTiles) {
			drawCell(g, centre, size, player, curCell, xShift, yShift);
		
		}
		
	}
	
	
	void drawCell(Graphics g, Point center, Dimension size, Point player, Cell c, float xShift, float yShift) {
		int w1=c.x()*renderSize-(int)((center.x()+xShift)*renderSize);
	    int h1=c.y()*renderSize-(int)((center.y()+yShift)*renderSize);
	    int w2=w1+renderSize;
	    int h2=h1+renderSize;
	    
	    // use player distance for lighting
	    double dist = Math.hypot(c.x()- player.x(), c.y() - player.y()) - 2;
	    
	   
	    
	    var isOut=h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
	    if(isOut){ return; }
	    g.drawImage(c.getImage().image, w1, h1, w2, h2, 0, 0, renderSize, renderSize, null);
//	    if(c.isSolid()) {
//	    	g.drawImage(Img.wall.image,w1,h1,w2+8,h2+8,0,0,renderSize+8,renderSize+8,null);
//	    }
	}

}
