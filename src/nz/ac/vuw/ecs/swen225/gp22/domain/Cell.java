package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

public class Cell{
	private CellState state;
	private int x,y;
	
	public Cell(CellState state, int x, int y) {
		this.state = state;
		this.x = x;
		this.y = y;
	}
	
	//getters/setters
	public CellState state() {return state;}
	public void setState(CellState s) {state = s;}
	public int x() {return x;}
	public int y() {return y;}
	
	public boolean isSolid() {return state.isSolid(this);}
	public char symbol() {return state.symbol(this);}
	//public void onPing() {state.onPing(this);}
	public Img getImage(){return state.getImage(this);}
}

/*
 * CellState interface for different types of cells to implement
 */
interface CellState{
	boolean isSolid(Cell self);
	char symbol(Cell self);
	//void onPing(Cell self);
	Img getImage(Cell self);
}

class Floor implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '_';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.floor;}
}

class Wall implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return '#';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.wall;}
}

//record Info(String message) implements CellState{
//	public boolean isSolid(Cell self) {return false;}
//	public char symbol(Cell self) {return 'i';}
//	//public void onPing(Cell self) {}
//	public Img getImage(Cell self) {return Img.water;} //change later
//}

class LockedDoor implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'L';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;} //change later
}

class ExitLock implements CellState{
	public boolean isSolid(Cell self) {return true;}
	public char symbol(Cell self) {return 'X';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;} //change later
}

class Spawn implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return 's';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.spawn;}
}

class Water implements CellState{
	public boolean isSolid(Cell self) {return false;}
	public char symbol(Cell self) {return '.';}
	//public void onPing(Cell self) {}
	public Img getImage(Cell self) {return Img.water;}
}


//public record Cell(List<Img> imgs, int x, int y){
//  static final public int renderX = 64;
//  static final public int renderY = 64;
//
//  void draw(Graphics g, Point center, Dimension size, Point player){
//    int w1=x*Cell.renderX-(int)(center.x()*Cell.renderX);
//    int h1=y*Cell.renderY-(int)(center.y()*Cell.renderY);
//    int w2=w1+Cell.renderX;
//    int h2=h1+Cell.renderY;
//    double dist = Math.hypot(x- player.x(), y - player.y()) - 2;
//    
//    var isOut=h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
//    if(isOut){ return; }
//    imgs.forEach(i->g.drawImage(i.image,w1,h1,w2,h2,0,0,Cell.renderX,Cell.renderY,null));
//    if(imgs.contains(Img.wall)) {
//    	g.drawImage(Img.wall.image,w1,h1,w2+8,h2+8,0,0,Cell.renderX+8,Cell.renderY+8,null);
//    }
//    
//    var percentage = .2d; // 50% bright - change this (or set dynamically) as you feel fit
//    percentage = 1- dist/Cell.renderX*16;
//    if (percentage < 0.1) percentage = 0.1;
//    if (percentage > 1) percentage = 1;
//    
//    int brightness = (int)(256 - 256 * percentage);
//    g.setColor(new Color(0,0,0,brightness));
//    g.fillRect(w1, h1, 72, 72);
//  }
//  
//  // replace later as each cell will be a different class with different interactions!! STATE PATTERN
//  Img type(){
//	  return imgs.get(0);
//  }
//  
//}