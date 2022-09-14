package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;
import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

public class Player {
	private Point pos;
	private Direction direction = Direction.None;
	double scale = 0.5;
	
	Player(Point p){
		pos = p;
	}
	
	public Point getPos() {
		return new Point(pos.x(), pos.y());
	}
	
	
//	  public Direction direction(){ return direction; }
//	  public void direction(Direction d){ direction=d; }
	  public Runnable set(Function<Direction,Direction> f){
	    return ()->direction=f.apply(direction);
	  }
	  
	public void tick(Cells cells){
		Point newPos = direction.arrow(0.1d);
		// check collisions with tiles
		
		// moving left or right
		if( 
			(cells.get((int)(pos.x() + scale/2 - 0.1), (int)(pos.y()+ scale/2)).isSolid() && newPos.x() < 0)
			|| (cells.get((int)(pos.x()+scale/2 - 0.1), (int)(pos.y()+ scale*(3/2d))).isSolid() && newPos.x() < 0)
			|| (cells.get((int)(pos.x()+scale*(3/2d) + 0.1), (int)(pos.y()+ scale/2)).isSolid() && newPos.x() > 0)
			|| (cells.get((int)(pos.x()+scale*(3/2d) + 0.1), (int)(pos.y()+ scale*(3/2d))).isSolid() && newPos.x() > 0)
			
			){
			newPos = new Point(0, newPos.y());
//			System.out.println("block horizontal") ;
		}
		// moving up or down
		if( 
			(cells.get((int)(pos.x()+ scale/2) ,(int)(pos.y() + scale/2 - 0.1)).isSolid() && newPos.y() < 0)
			|| (cells.get((int)(pos.x()+ scale*(3/2d)) ,(int)(pos.y()+scale/2 - 0.1)).isSolid() && newPos.y() < 0)
			|| (cells.get((int)(pos.x()+ scale/2) ,(int)(pos.y()+scale*(3/2d) + 0.1)).isSolid() && newPos.y() > 0)
			|| (cells.get((int)(pos.x()+ scale*(3/2d)) ,(int)(pos.y()+scale*(3/2d) + 0.1)).isSolid() && newPos.y() > 0)
			
			){
			newPos = new Point(newPos.x(), 0);
//			System.out.println("block vertical") ;
		}
		
		
		pos = pos.add(newPos);
		
		
		
	}
	
	public void draw(BufferedImage img, Graphics g, Point center, Dimension size){
		double w1=pos.x()*64-(center.x()*64) + 64*(scale/2);
		double h1=pos.y()*64-(center.y()*64) + 64*(scale/2);
		double w2=w1+64*scale;
		double h2=h1+64*scale;
		
//	    var l = pos;
//	    var lx = (l.x()-center.x())*64;
//	    var ly = (l.y()-center.y())*64;
//	    double iw = img.getWidth()/2d;
//	    double ih = img.getHeight()/2d;
//	    int w1 = (int)(lx-iw);
//	    int w2 = (int)(lx+iw);
//	    int h1 = (int)(ly-ih);
//	    int h2 = (int)(ly+ih);
	    var isOut = h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
	    if(isOut){ return; }
	    g.drawImage(img,(int)w1,(int)h1,(int)w2,(int)h2,0,0,64,64,null);
//	    g.drawImage(img,w1,h1,w2,h2,0,0,img.getWidth(),img.getHeight(),null);
	    }
}
