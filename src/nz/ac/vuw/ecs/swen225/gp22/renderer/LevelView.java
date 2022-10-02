package nz.ac.vuw.ecs.swen225.gp22.renderer;

import nz.ac.vuw.ecs.swen225.gp22.app.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.fonts.LoadedFont;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.Img;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.player_sprites.PlayerImg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JPanel;

/**
 * @author Declan Cross
 * Renders all content of map and GUI on screen when playing levels
 */
public class LevelView extends JPanel{
	// serial number
	private static final long serialVersionUID = 1L;

	// level variables
	private final Level l;				// stored level for rendering
	
	
	// animation variables
	private int fadeIn;  				// current fade amount
	private final int FADELENGTH = 25;  // max time in ticks for fade in
	private final double STEPS = 2; 	// animation steps for fading in objects
	private int tickCount;				// current tick count for animations
	private final int MAXTICKS = 16;    // animation cycle max time
	
	// rendering variables
	private final int RENDERSIZE = 64;	// default rendering size of cells
	
	// player variables
	Direction oldDir;					// old position to remember for player animation
	
	// TESTING
	EventHandler e;
	
	/**
	 * Constructor
	 * @param newLevel  level object to be observed 
	 */
	public LevelView(Level newLevel) {
		l = newLevel;
		oldDir = Direction.Down;
		e = new EventHandler(l, new SoundPlayer());
	}
	
	
	/**
	 * Override method to paint screen every game tick
	 * @param g  graphics panel to draw on
	 */
	public void paintComponent(Graphics g) {
	   /// get size of graphics
	   super.paintComponent(g);
	   Dimension s = getSize();
	   
	   // fade in or out animation
	   if(fadeIn < FADELENGTH) fadeIn++;
	   
	   // tick counter for animated textures
	   tickCount++;
	   tickCount = tickCount % MAXTICKS;
	   
	   // get player info
	   Player curPlayer = l.getPlayer();
	   Point pos = curPlayer.getPos();
	   
	   // work out difference between positions to make movement animation
	   Point diff = curPlayer.getOldPos().distance(pos);
	   float xShift =  diff.x() * (1-curPlayer.getMoveTime()); 
	   float yShift = diff.y() * (1-curPlayer.getMoveTime());
	   PlayerFields pf = new PlayerFields(pos, xShift, yShift);
	   
	   // find centre of map relative to player
	   var centerP = new Point(-(int)(s.width * 0.65)/(int)(2*RENDERSIZE), -s.height/(int)(2*RENDERSIZE));
	   var c = pos.add(centerP);
	   ScreenFields sf = new ScreenFields(g, c, s);
	   
	   
	   // draw map, player, and GUI
	   drawMap(sf, pf);
	   drawPlayer(sf, pos);
	   drawGUI(g, s, l, curPlayer);
	 
	}
	
	/**
	 * Method to draw the map and its child components
	 * @param sf  record containing screen fields
	 * @param pf  record containing player fields
	 */
	void drawMap(ScreenFields sf, PlayerFields pf){
		// paint background black
		sf.g().setColor(new Color(0, 0, 0, fadeIn *10));
		sf.g().fillRect(0, 0, sf.size().width, sf.size().height);
		
		// get cells to draw
		Cells c = l.getCells();
		List<Cell> wallTiles = new ArrayList<>();
		int range = (int)((float)fadeIn/STEPS);
		
		// use nestled intstreams to go through for all squares on screen to draw cells 
		IntStream.range(pf.player().x()-range+1, pf.player().x()+range)
			.forEach(row -> IntStream.range(pf.player().y()-range+1, pf.player().y()+range)
			.forEach(col -> {
				if(c.get(row, col).isSolid()){
					wallTiles.add(c.get(row, col));
				} else {
					 drawCell(sf, pf, c.get(row, col));
				}
			})
		);
		
		// get entities to draw
		List<Entity> entities = l.getEntites(); 
		entities.stream().forEach(ent -> drawEntity(sf, pf, ent));
		
		// walls must be drawn last for 3D effect
		wallTiles.forEach(a -> drawCell(sf, pf, a));
		
		// draw shadows over map
		IntStream.range(pf.player().x()-range+1, pf.player().x()+range)
		.forEach(row -> IntStream.range(pf.player().y()-range+1, pf.player().y()+range)
		.forEach(col -> drawShadow(sf, pf, new Point(row, col))));
	}
	
	/**
	 * Method to draw shadows
	 * @param sf  record containing screen fields
	 * @param pf  record containing player fields
	 * @param cellPos  current cell object's position
	 */
	void drawShadow(ScreenFields sf, PlayerFields pf, Point cellPos) {
		 // calculate shadow dimensions
		 int w1=cellPos.x()*RENDERSIZE-(int)((sf.centre().x()+pf.xShift())*RENDERSIZE);
		 int h1=cellPos.y()*RENDERSIZE-(int)((sf.centre().y()+pf.yShift())*RENDERSIZE);
		 
		 // calculate distance of shadow from player
		 double dist = Math.hypot(cellPos.x()- pf.player().x()-pf.xShift(), cellPos.y() - pf.player().y()-pf.yShift()) - 2;
		 dist *= 50;
//		 dist += Math.random() *4 -2;
//		 dist -= (c.x()- player.x())*20 %4;
//		 if(Math.abs(c.x()- player.x()) %4 == 1 && Math.abs(c.y()- player.y()) % 3 == 1){
//			 dist += 15 * (xShift + yShift);
//		 }
		 if(dist < 0) {dist = 0;}
		 if(dist > 255) {dist = 255;}
		 
		 // draw shadow
		 sf.g().setColor(new Color(0, 0, 0, (int)dist));
		 sf.g().fillRect(w1, h1, RENDERSIZE, RENDERSIZE);
	}
	
	
	/**
	 * Method to draw a single cell
	 * @param sf  record containing screen fields
	 * @param pf  record containing player fields
	 * @param c  current cell object
	 */
	void drawCell(ScreenFields sf, PlayerFields pf, Cell c) {
		// calculate image dimensions
		int w1=c.x()*RENDERSIZE-(int)((sf.centre().x()+pf.xShift())*RENDERSIZE);
	    int h1=c.y()*RENDERSIZE-(int)((sf.centre().y()+pf.yShift())*RENDERSIZE);
	    int w2=w1+RENDERSIZE;
	    int h2=h1+RENDERSIZE;
	   
	    // draw enlarged images for solid objects as they are 3D and regular if not
	    if(c.isSolid()) {
	    	sf.g().drawImage(Img.valueOf(c.getName()).image,w1,h1,w2+8,h2+8,0,0,RENDERSIZE+8,RENDERSIZE+8,null); //
	    } else { //c.getImage().image
	    	sf.g().drawImage(Img.valueOf(c.getName()).image, w1, h1, w2, h2, 0, 0, RENDERSIZE, RENDERSIZE, null);
	    }
	}
	
	
	/**
	 * Method to draw a single entity
	 * @param sf  record containing screen fields
	 * @param pf  record containing player fields
	 * @param ent  current entity object
	 */
	void drawEntity(ScreenFields sf, PlayerFields pf, Entity ent){
		// return if out of render distance
		Point pos = ent.getPos();
	    if(Math.hypot(pos.x()- pf.player().x()+0.5, pos.y() - pf.player().y()+0.5) > (int)((float)fadeIn/STEPS)) {return;}
		
		// calculate entity image dimensions
		int w1=pos.x()*RENDERSIZE-(int)((sf.centre().x()+pf.xShift())*RENDERSIZE);
	    int h1=pos.y()*RENDERSIZE-(int)((sf.centre().y()+pf.yShift())*RENDERSIZE);
	    int w2=w1+RENDERSIZE;
	    int h2=h1+RENDERSIZE;
	    
	    // draw image
	    sf.g().drawImage(Img.valueOf(ent.getName()).image, w1, h1, w2, h2, 0, 0, RENDERSIZE, RENDERSIZE, null);
	}
	
	
	/**
	 * Method to draw a player
	 * @param sf  record containing screen fields
	 * @param pf  record containing player fields
	 */
	void drawPlayer(ScreenFields sf, Point pos) {
		// get dimensions of image
		double scale = 0.5;
		double w1=pos.x()*RENDERSIZE-(sf.centre().x()*RENDERSIZE) + RENDERSIZE*(scale/2);
		double h1=pos.y()*RENDERSIZE-(sf.centre().y()*RENDERSIZE) + RENDERSIZE*(scale/2);
		double w2=w1+RENDERSIZE*scale;
		double h2=h1+RENDERSIZE*scale;
		
		// work out player image to use
		String type;
		if(l.getPlayer().direction() != Direction.None) {
			oldDir = l.getPlayer().direction();
			type = "walk";
		} else{
			type = "idle";
		}
		int val = tickCount > 8 ? 1 : 2;
		
		// draw player image
	    sf.g().drawImage(PlayerImg.valueOf(type+"_"+ oldDir + "_" + val).image,(int)w1,(int)h1,(int)w2,(int)h2,0,0,RENDERSIZE,RENDERSIZE,null);
	}
	
	
	/**
	 * Method to draw on screen informative elements
	 * @param g    graphics to render in
	 * @param s    size of screen
	 * @param p    level object
	 */
	void drawGUI(Graphics g, Dimension s, Level l, Player p) {
		// draw background card
		int inventoryHeight = s.height - 2*RENDERSIZE;
		int inventoryWidth = (int)(s.width * 3/12f);
		g.setColor(new Color(120, 131, 84, fadeIn * 9));
		g.fillRoundRect(s.width - RENDERSIZE - inventoryWidth, RENDERSIZE, inventoryWidth, inventoryHeight, 30, 30);
//		g.fillRoundRect(s.width - (int)(s.width * 3/12f) - (int)(s.height * 1/12f), (int)(s.height * 1/12f) , (int)(s.width * 3/12f), (int)(s.height * 5/6f), 30, 30);
		
		
		// draw text
		g.setColor(Color.white);
		g.setFont( LoadedFont.PixeloidSans.getSize(42f));
		
		// titles
		g.drawString("Level",  s.width - inventoryWidth , 130);
		g.drawString("Time",  s.width - inventoryWidth , 270);
		g.drawString("Chips",  s.width - inventoryWidth , 410);
		
		g.setFont( LoadedFont.PixeloidSans.getSize(30f));
		g.setColor(new Color(190, 196, 161));
		
		// values
		g.drawString(String.format("%03d", l.getLevelNum()),  s.width - inventoryWidth , 180);
		//g.drawString(String.format("%03d", (int)(l.getTime()*(0.034))),  s.width - inventoryWidth , 320);
		g.drawString(String.format("%03d", l.getPlayer().treasuresToCollect()),  s.width - inventoryWidth , 460);
		
		int infoFieldHeight = 2 * RENDERSIZE;
		int infoFieldWidth = s.width - inventoryWidth - 3*RENDERSIZE;
		// draw sign if present.
		p.getActiveInfoField().ifPresent(a -> {
			g.setColor(new Color(122, 101, 91, 225));
			g.fillRoundRect(RENDERSIZE, s.height - infoFieldHeight - RENDERSIZE, infoFieldWidth, infoFieldHeight, 30 ,30);
			g.setColor(Color.white);
			g.setFont( LoadedFont.PixeloidSans.getSize(24f));
			g.drawString(a.getMessage(), (int)(RENDERSIZE*1.5), s.height - infoFieldHeight);
			
			
		});
	}
	
	
	
	
	
	
	/**
	 * Private record containing screen fields to be used for rendering
	 * @author Declan Cross
	 * @param g  graphics pane to draw on
	 * @param centre  defined centre of content
	 * @param size  size of screen
	 */
	private record ScreenFields(Graphics g, Point centre, Dimension size){}
	
	/**
	 * Private record containing player fields used for positioning
	 * @author Declan Cross
	 * @param player  position of player object
	 * @param xShift  calculated dist between old and new player pos
	 * @param yShift  calculated dist between old and new player pos
	 */
	private record PlayerFields(Point player, float xShift, float yShift){}

}
