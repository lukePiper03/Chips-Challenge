package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.Dimension;

import nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.*;

public class Cells{
  int maxX;
  int maxY;
  Point spawn; 
  private final List<List<Cell>> inner=new ArrayList<>();
  
  public Cells(char[][] map){
	 maxX = map[0].length; 
	 maxY = map.length;
	  
    for(int x=0;x<maxX;x++){
      var tmp=new ArrayList<Cell>();
      inner.add(tmp);
      for(int y=0;y<maxY;y++){
    	 if(map[y][x] == '#') {
    		 tmp.add(new Cell(new Wall(), x, y));
    	 } 
    	 else if(map[y][x] == 's') {
    		 tmp.add(new Cell(new Spawn(), x, y));
    		 spawn = new Point(x, y);
    	 }
    	 else {
//    		 tmp.add(new Cell(List.of(Img.Grass,Img.Tree),x,y));
    		 tmp.add(new Cell(new Floor(),x,y));
    	 }
    	  
        
      }
    }
//    inner.get(3).set(3,new Cell(List.of(Img.Grass),3,3));//a patch of grass
//    inner.get(4).set(4,new Cell(List.of(Img.Grass),4,4));
//    inner.get(3).set(4,new Cell(List.of(Img.Grass),3,4));
  }
  
  public Cell get(int x,int y){
    var isOut = x<0 || y<0 || x>=maxX || y>=maxY;
    if(isOut){
    	return new Cell(new Water(),x,y); }
    var res = inner.get(x).get(y);
    assert res!=null;
    return res;
  }
  
  public Point getSpawn() {
	  return spawn;
  }
  


//	public void drawAll(Point pos, Graphics g, Dimension s, Point player) {
////		inner.stream().flatMap(a -> a.stream()).forEach(b -> b.draw(g, pos, s, player));
//		ArrayList<Cell> wallTiles = new ArrayList<>();
//		int range = 10;
//		for(int x=(int)player.x()-range;x<=player.x()+range;x++){
//	      for(int y=(int) (player.y()-range);y<=player.y()+range;y++){
//	    	  if(this.get(x, y).type() == Img.wall) {
//	    		  wallTiles.add(this.get(x, y));
//	    	  } else {
//	    		  this.get(x, y).draw(g, pos, s, player);
//	    	  }
//	      }
//	    }
//		
//		// draw walls on top
//		for(Cell c : wallTiles) {
//			c.draw(g,  pos, s, player);
//		}
//	}
  
//  public void forAll(Point p,int range,Consumer<Cell>action){
//    assert range>=0;
//    for(double x=p.x()-range;x<=p.x()+range;x++){
//      for(int y=p.y()-range;y<=p.y()+range;y++){
//        action.accept(get(x,y));
//      }
//    }
//  }
}