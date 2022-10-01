package nz.ac.vuw.ecs.swen225.gp22.renderer.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;



public enum LoadedFont{
	  PixeloidBold,
	  PixeloidSans,
	  PixeloidMono;
	  public final Font style;
	  LoadedFont(){style=loadFont(this.name());}
	  static private Font loadFont(String name){
		try {
			Font tempFont = Font.createFont(Font.TRUETYPE_FONT, LoadedFont.class.getResourceAsStream(name + ".ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(tempFont);
			return tempFont;
		} catch (Exception e) {throw new Error(e);}
	    
	  }
	  public Font getSize(Float size) {
		  return style.deriveFont(size);
	  }
}
