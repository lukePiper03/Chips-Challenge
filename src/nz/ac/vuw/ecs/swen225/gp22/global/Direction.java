package nz.ac.vuw.ecs.swen225.gp22.global;


import nz.ac.vuw.ecs.swen225.gp22.domain.Point;

public enum Direction{
  None(0,0){},
  Up(0,-1){ 
	  public Direction unUp(){return None;}
  },
  Right(+1,0){
	  public Direction unRight(){return None;}
  },
  Down(0,+1){
	  public Direction unDown(){return None;}
  },
  Left(-1,0){
	  public Direction unLeft(){return None;}
  };

  public final Point next;
  public Direction up(){return Up;}
  public Direction right(){return Right;}
  public Direction down(){return Down;}
  public Direction left(){return Left;}
  public Direction unUp(){return this;}
  public Direction unRight(){return this;}
  public Direction unDown(){return this;}
  public Direction unLeft(){return this;}
  
  public Point point() {return next;}
  Direction(int x,int y){next = new Point(x, y); }
}