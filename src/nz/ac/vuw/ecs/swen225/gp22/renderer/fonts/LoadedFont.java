package nz.ac.vuw.ecs.swen225.gp22.renderer.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * @author Declan Cross
 * Loaded font class storing all fonts
 */
public enum LoadedFont{
	  // font declarations
	  PixeloidBold,
	  PixeloidSans,
	  PixeloidMono;
	
	  /**
	   *  Current loaded font style
	   */
	  public final Font style;
	  
	  /**
	   * Constructor for LoadedFont
	   */
	  
	  LoadedFont(){style=loadFont(this.name());}
	  /**
	   * Load font on initialisation
	   * @param name of font
	   * @return loaded font
	   */
	  
	  static private Font loadFont(String name){
		try {
			// try create font and register
			Font tempFont = Font.createFont(Font.TRUETYPE_FONT, LoadedFont.class.getResourceAsStream(name + ".ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(tempFont);
			return tempFont;
		} catch (Exception e) {throw new Error(e);}
	    
	  }
	  
	  /**
	   * Return correct requested font of certain size for rendering
	   * @param size of font
	   * @return  font of size
	   */
	  public Font getSize(Float size) {
		  return style.deriveFont(size);
	  }
}
