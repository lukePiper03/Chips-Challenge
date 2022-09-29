package nz.ac.vuw.ecs.swen225.gp22.renderer.imgs.player_sprites;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public enum PlayerImg{
  idle_Down_1,
  idle_Down_2,
  
  idle_Left_1,
  idle_Left_2,
  
  idle_Right_1,
  idle_Right_2,
  
  idle_Up_1,
  idle_Up_2,
  
  walk_Down_1,
  walk_Down_2,
  
  walk_Left_1,
  walk_Left_2,
  
  walk_Right_1,
  walk_Right_2,
  
  walk_Up_1,
  walk_Up_2;
  
  public final BufferedImage image;
  PlayerImg(){image=loadImage(this.name());}
  static private BufferedImage loadImage(String name){
    URL imagePath = PlayerImg.class.getResource(name+".png");
    try{return ImageIO.read(imagePath);}
    catch(IOException e) { 
    	throw new Error(e); }
  }
}

