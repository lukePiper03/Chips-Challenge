package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.app.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JPanel;



public class LevelView extends JPanel{
	// level
	Level l;
//	SoundPlayer s;
	
	// rendering variables
	private final int renderSize = 64;
	
	public LevelView(Level newLevel) {
		l = newLevel;
//		s = new SoundPlayer();
//		s.loop(Sound.eightbitsong);
//		s.stop(Sound.eightbitsong);
//		l.addSoundPlayer(s);
	}
	
	
	public void paintComponent(Graphics g) {
	   /// get size of graphcs
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
	   drawPlayer(g, c, s, l.getPlayer().getPos());

	 
	}
	
	/// MAKE A DRAWPLAYER METHOD. FIND THE GETMOVETIME TO CHANGE THE SPRITESHEET AS WELL AS DIRECTION
	// EACH DIR MOVEMENT HAS 4 FRAMES WHICH WE CAN USE FOR EACH 0.2 OF TIME.
	
	
	void drawMap(Graphics g, Point centre, Dimension size, Point player, float xShift, float yShift){
		// get cells to draw
		Cells c = l.getCells();
		List<Cell> wallTiles = new ArrayList<>();
		int range = 10;
		
		// use for loop for all squares on screen to draw cells 
		IntStream.range(player.x()-range, player.x()+range)
			.forEach(row -> IntStream.range(player.y()-range, player.y()+range)
			.forEach(col -> {
				if(c.get(row, col).isSolid()){
					wallTiles.add(c.get(row, col));
				} else {
					 drawCell(g, centre, size, player, c.get(row, col), xShift, yShift);
				}
			})
		);
		
		// get entities to draw
		List<Entity> entities = l.getEntites(); 
		entities.stream().forEach(ent -> drawEntity(g, centre, size, ent, xShift, yShift));
		
		// walls must be drawn last for 3D effect
		wallTiles.forEach(a -> drawCell(g, centre, size, player, a, xShift, yShift));
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
	    if(c.isSolid()) {
	    	g.drawImage(c.getImage().image,w1,h1,w2+8,h2+8,0,0,renderSize+8,renderSize+8,null);
	    }
	}
	
	void drawEntity(Graphics g, Point center, Dimension size, Entity ent, float xShift, float yShift){
		Point pos = ent.getPos();
		int w1=pos.x()*renderSize-(int)((center.x()+xShift)*renderSize);
	    int h1=pos.y()*renderSize-(int)((center.y()+yShift)*renderSize);
	    int w2=w1+renderSize;
	    int h2=h1+renderSize;
	    g.drawImage(ent.getImage().image, w1, h1, w2, h2, 0, 0, renderSize, renderSize, null);
	}
	
	void drawPlayer(Graphics g, Point center, Dimension size, Point pos) {
		double scale = 0.5;
		double w1=pos.x()*renderSize-(center.x()*renderSize) + renderSize*(scale/2);
		double h1=pos.y()*renderSize-(center.y()*renderSize) + renderSize*(scale/2);
		double w2=w1+renderSize*scale;
		double h2=h1+renderSize*scale;
	    g.drawImage(Img.player.image,(int)w1,(int)h1,(int)w2,(int)h2,0,0,renderSize,renderSize,null);
	}

}
