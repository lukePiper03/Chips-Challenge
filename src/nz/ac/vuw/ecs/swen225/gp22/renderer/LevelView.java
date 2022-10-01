package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.app.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.player_sprites.PlayerImg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JPanel;



public class LevelView extends JPanel{
	// level variables
	Level l;
	int tickCount;
	
	// animation variables
	int fadeIn = 0;
	double steps = 2;
	
	// rendering variables
	private final int renderSize = 64;
	
	// player variables
	Direction oldDir = Direction.Down;
	
	/**
	 * Constructor
	 * @param newLevel
	 */
	public LevelView(Level newLevel) {
		l = newLevel;
		
	}
	
	
	public void paintComponent(Graphics g) {
	   /// get size of graphics
	   super.paintComponent(g);
	   Dimension s = getSize();
	   
	   // fade in or out animation
	   if(fadeIn < 25) fadeIn++;
	   
	   // tick counter for animated textures
	   tickCount++;
	   tickCount = tickCount % 16;
	   
	   
	   // find centre of map relative to player
	   var centerP = new Point(
	      -(int)(s.width * 0.65)/(int)(2*renderSize),
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
	   drawGUI(g, s, l);
	 
	}
	
	
	/**
	 * Method to draw the map and its child components
	 * @param g
	 * @param centre
	 * @param size
	 * @param player
	 * @param xShift
	 * @param yShift
	 */
	void drawMap(Graphics g, Point centre, Dimension size, Point player, float xShift, float yShift){
		// get cells to draw
		Cells c = l.getCells();
		List<Cell> wallTiles = new ArrayList<>();
		int range = (int)((float)fadeIn/steps);
		
		// use for loop for all squares on screen to draw cells 
		IntStream.range(player.x()-range+1, player.x()+range)
			.forEach(row -> IntStream.range(player.y()-range+1, player.y()+range)
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
		entities.stream().forEach(ent -> drawEntity(g, centre, size, player, ent, xShift, yShift));
		
		// walls must be drawn last for 3D effect
		wallTiles.forEach(a -> drawCell(g, centre, size, player, a, xShift, yShift));
		
		// draw shadows over map
		IntStream.range(player.x()-range+1, player.x()+range)
		.forEach(row -> IntStream.range(player.y()-range+1, player.y()+range)
		.forEach(col -> drawShadow(g, centre, size, player, c.get(row, col), xShift, yShift)));
	}
	
	/**
	 * Method to draw shadows
	 * @param g
	 * @param center
	 * @param size
	 * @param player
	 * @param c
	 * @param xShift
	 * @param yShift
	 */
	void drawShadow(Graphics g, Point center, Dimension size, Point player, Cell c, float xShift, float yShift) {
		 int w1=c.x()*renderSize-(int)((center.x()+xShift)*renderSize);
		 int h1=c.y()*renderSize-(int)((center.y()+yShift)*renderSize);
		 double dist = Math.hypot(c.x()- player.x()-xShift, c.y() - player.y()-yShift) - 2;
		 dist *= 50;
//		 dist += Math.random() *4 -2;
//		 dist -= (c.x()- player.x())*20 %4;
//		 if(Math.abs(c.x()- player.x()) %4 == 1 && Math.abs(c.y()- player.y()) % 3 == 1){
//			 dist += 15 * (xShift + yShift);
//		 }
		 if(dist < 0) {dist = 0;}
		 if(dist > 255) {dist = 255;}
		 
		 g.setColor(new Color(0, 0, 0, (int)dist));
		 g.fillRect(w1, h1, renderSize, renderSize);
	}
	
	/**
	 * Method to draw a single cell
	 * @param g
	 * @param center
	 * @param size
	 * @param player
	 * @param c
	 * @param xShift
	 * @param yShift
	 */
	void drawCell(Graphics g, Point center, Dimension size, Point player, Cell c, float xShift, float yShift) {
		int w1=c.x()*renderSize-(int)((center.x()+xShift)*renderSize);
	    int h1=c.y()*renderSize-(int)((center.y()+yShift)*renderSize);
	    int w2=w1+renderSize;
	    int h2=h1+renderSize;
	    
	    // use player distance for lighting
	    double dist = Math.hypot(c.x()- player.x(), c.y() - player.y()) - 2;
	    
	    var isOut=h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
	    if(isOut){ return; }
	    dist *= 10;
	    if(dist > 255) {dist = 254;}
	    g.setColor(new Color(0, 0, 0, 100));
	    
	    if(c.isSolid()) {
	    	g.drawImage(c.getImage().image,w1,h1,w2+8,h2+8,0,0,renderSize+8,renderSize+8,null);
	    } else {
	    	g.drawImage(c.getImage().image, w1, h1, w2, h2, 0, 0, renderSize, renderSize, null);
	    }
	}
	
	/**
	 * Method to draw a single entity
	 * @param g
	 * @param center
	 * @param size
	 * @param player
	 * @param ent
	 * @param xShift
	 * @param yShift
	 */
	void drawEntity(Graphics g, Point center, Dimension size, Point player, Entity ent, float xShift, float yShift){
		Point pos = ent.getPos();
		int w1=pos.x()*renderSize-(int)((center.x()+xShift)*renderSize);
	    int h1=pos.y()*renderSize-(int)((center.y()+yShift)*renderSize);
	    int w2=w1+renderSize;
	    int h2=h1+renderSize;
	    Point e = ent.getPos();
	    if(Math.hypot(e.x()- player.x()+0.5, e.y() - player.y()+0.5) > (int)((float)fadeIn/steps)) {return;}
	    g.drawImage(ent.getImage().image, w1, h1, w2, h2, 0, 0, renderSize, renderSize, null);
	}
	
	/**
	 * Method to draw a play
	 * @param g
	 * @param center
	 * @param size
	 * @param pos
	 */
	void drawPlayer(Graphics g, Point center, Dimension size, Point pos) {
		double scale = 0.5;
		double w1=pos.x()*renderSize-(center.x()*renderSize) + renderSize*(scale/2);
		double h1=pos.y()*renderSize-(center.y()*renderSize) + renderSize*(scale/2);
		double w2=w1+renderSize*scale;
		double h2=h1+renderSize*scale;
		
		// work out player positions
		String type;
		if(l.getPlayer().direction() != Direction.None) {
			oldDir = l.getPlayer().direction();
			type = "walk";
		} else{
			type = "idle";
		}
		int val = tickCount > 8 ? 1 : 2;
		// draw player image
	    g.drawImage(PlayerImg.valueOf(type+"_"+ oldDir + "_" + val).image,(int)w1,(int)h1,(int)w2,(int)h2,0,0,renderSize,renderSize,null);
	}
	
	
	/**
	 * Method to draw on screen informative elements
	 * @param g    graphics to render in
	 * @param s    size of screen
	 * @param p    level object
	 */
	void drawGUI(Graphics g, Dimension s, Level l) {
		// draw background card
		g.setColor(new Color(120, 131, 84, fadeIn * 9));
		g.fillRoundRect(s.width - (int)(s.width * 3/12f) - (int)(s.height * 1/12f), (int)(s.height * 1/12f) , (int)(s.width * 3/12f), (int)(s.height * 5/6f), 30, 30);
		
		// draw text
		g.setColor(Color.white);
		g.setFont( LoadedFont.PixeloidSans.getSize(48f));
		
		// titles
		g.drawString("Level",  s.width - (int)(s.width * 3/12f) , 130);
		g.drawString("Time",  s.width - (int)(s.width * 3/12f) , 270);
		g.drawString("Chips",  s.width - (int)(s.width * 3/12f) , 410);
		
		g.setFont( LoadedFont.PixeloidSans.getSize(36f));
		g.setColor(new Color(190, 196, 161));
		
		// values
		g.drawString(String.format("%03d", l.getLevelNum()),  s.width - (int)(s.width * 3/12f) , 180);
//		g.drawString(String.format("%03d", (int)(l.getTime()*(0.034))),  s.width - (int)(s.width * 3/12f) , 320);
		g.drawString(String.format("%03d", l.getPlayer().treasuresToCollect()),  s.width - (int)(s.width * 3/12f) , 460);
		
	}

}
