package nz.ac.vuw.ecs.swen225.gp22.renderer.imgs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * @author dj
 * enum storing all usable images for cells and entities
 */
public enum Img{
  Floor,
  Wall,
  Spawn,
  Player,
  LockedDoor,
  InfoField,
  Exit,
  Key,
  ExitLock,
  Treasure,
  Water;
  public final BufferedImage image;
  Img(){image=loadImage(this.name());}
  static private BufferedImage loadImage(String name){
    URL imagePath = Img.class.getResource(name+".png");
    try{return ImageIO.read(imagePath);}
    catch(IOException e) {
    	System.out.println(name + ".png does not exist1");
    	return Img.Water.image;
    }
  }
}