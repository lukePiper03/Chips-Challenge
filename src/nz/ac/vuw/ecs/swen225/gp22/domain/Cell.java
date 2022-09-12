package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

public record Cell(List<Img> imgs, int x, int y){
  static final public int renderX = 64;
  static final public int renderY = 64;

  void draw(Graphics g, Point center, Dimension size, Point player){
    int w1=x*Cell.renderX-(int)(center.x()*Cell.renderX);
    int h1=y*Cell.renderY-(int)(center.y()*Cell.renderY);
    int w2=w1+Cell.renderX;
    int h2=h1+Cell.renderY;
    double dist = Math.hypot(x- player.x(), y - player.y()) - 2;
    
    var isOut=h2<=0 || w2<=0 || h1>=size.height || w1>=size.width;
    if(isOut){ return; }
    imgs.forEach(i->g.drawImage(i.image,w1,h1,w2,h2,0,0,Cell.renderX,Cell.renderY,null));
    if(imgs.contains(Img.wall)) {
    	g.drawImage(Img.wall.image,w1,h1,w2+8,h2+8,0,0,Cell.renderX+8,Cell.renderY+8,null);
    }
    
    var percentage = .2d; // 50% bright - change this (or set dynamically) as you feel fit
    percentage = 1- dist/Cell.renderX*16;
    if (percentage < 0.1) percentage = 0.1;
    if (percentage > 1) percentage = 1;
    
    int brightness = (int)(256 - 256 * percentage);
    g.setColor(new Color(0,0,0,brightness));
    g.fillRect(w1, h1, 72, 72);
  }
  
  // replace later as each cell will be a different class with different interactions!! STATE PATTERN
  Img type(){
	  return imgs.get(0);
  }
  
}