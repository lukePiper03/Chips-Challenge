package nz.ac.vuw.ecs.swen225.gp22.renderer.imgs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

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
  RedLockedDoor,
  BlueLockedDoor,
  GreenLockedDoor,
  YellowLockedDoor,
  InfoField,
  Exit,
  Key_R,
  Key_G,
  Key_B,
  Key_Y,
  ExitLock,
  Treasure,
  Water,
  Ice,
  Monster,
  Boots,
  Flippers,
  Background,
  Teleporter;
  public final BufferedImage image;
  Img(){image=loadImage(this.name());}
  
  /**
   * Value safe enum getter for names, will not cause game to crash and uses replacement textures instead
   * @param name
   * @return enum of correct value
   */
  static public Img getValue(String name){
	  try {
		  return valueOf(name);
	  } catch (IllegalArgumentException e){
		  System.out.println(name + " enum value does not exist.");
		  return Img.Water;
	  }
  }
  /**
   * GOverloaded getter for alternate textures - useful for animated textures as well
   * @param name
   * @param var
   * @return
   */
  static public Img getValue(String name, int curVal, int maxVal){
	  String image = name+"_"+(int)(curVal/(float)maxVal);
	  return Stream.of(Img.values()).anyMatch(v -> v.name().equals(image)) ? getValue(image) : getValue(name);
  }
  
  static private BufferedImage loadImage(String name){
    URL imagePath = Img.class.getResource(name+".png");
    try{return ImageIO.read(imagePath);}
    catch(IOException e) {
    	System.out.println(name + ".png does not exist.");
    	return Img.Water.image;
    }
  }
}