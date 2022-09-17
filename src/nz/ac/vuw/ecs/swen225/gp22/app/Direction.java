package nz.ac.vuw.ecs.swen225.gp22.app;

import nz.ac.vuw.ecs.swen225.gp22.domain.Point;

public enum Direction{
  None(0,0){},
  Up(0,-1){ 
    Direction unUp(){return None;}
  },
  Right(+1,0){
  },
  Down(0,+1){
    Direction unDown(){return None;}
  },
  Left(-1,0){
    Direction unLeft(){return None;}
  };

  public final Point next;
  Direction up(){return Up;}
  Direction right(){return Right;}
  Direction down(){return Down;}
  Direction left(){return Left;}
  Direction unUp(){return this;}
  Direction unRight(){return this;}
  Direction unDown(){return this;}
  Direction unLeft(){return this;}
  
  public Point point() {return next;}
  Direction(int x,int y){next = new Point(x, y); }
}