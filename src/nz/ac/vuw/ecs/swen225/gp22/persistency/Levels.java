package nz.ac.vuw.ecs.swen225.gp22.persistency;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;

/**
 * Creates and saves levels to and from files
 * @author Jarvis Warnock 300578342
 *
 */
public class Levels {
	
//	public static void main(String args[]) {
//		loadLevel(()->System.out.println(""),new SoundPlayer(),"level1.xml");
//	}
	
	/**
	 * Loads a level based off what name is passed in
	 * 
	 * @param filename - The name of the file to be loaded
	 * @return Level - A level loaded from a file
	 */
	public static Level loadLevel(Runnable next,SoundPlayer soundPlayer,String filename) {
		String prefix = "./src/nz/ac/vuw/ecs/swen225/gp22/persistency/levels/";	// Filepath prefix
		filename = prefix + filename;
		Set<Entity> entities = new HashSet<Entity>();
		try {
			File file = new File(filename);
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(file);
			
			Integer levelNum = Integer.parseInt(document.getRootElement().getAttributeValue("num"));
			
			// Creates the level map
			int rows = Integer.parseInt(document.getRootElement().getChild("map").getAttributeValue("rows"));
			int cols = Integer.parseInt(document.getRootElement().getChild("map").getAttributeValue("cols"));
			
			char[][] map = new char[rows][cols];	// A map of all the tiles in the level
			
			List<Element> lines = document.getRootElement().getChild("map").getChildren();
			IntStream.range(0, rows)
			.forEach(row -> {IntStream.range(0, cols)
					.forEach(col -> map[row][col] = lines.get(row).getText().charAt(col));});
			
			// Creates the elements from the file
			List<Element> entityList = document.getRootElement().getChild("entities").getChildren();
			entityList.stream().filter(e -> e.getName().equals("key")).forEach(k -> createKey(k,entities));
			entityList.stream().filter(e -> e.getName().equals("treasure")).forEach(k -> createTreasure(k,entities));
			entityList.stream().filter(e -> e.getName().equals("info")).forEach(k -> createInfo(k,entities));
			entityList.stream().filter(e -> e.getName().equals("exit")).forEach(k -> createExit(k,entities));
			return new Level(next,soundPlayer,map,entities,levelNum);
		}catch(JDOMException e) {
			e.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Creates a key from a string from the file
	 * @param e - The element from the file
	 * @param entities - The current list of entities
	 */
	private static void createKey(Element e,Set<Entity> entities) {
		int x = Integer.parseInt(e.getAttributeValue("x"));
		int y = Integer.parseInt(e.getAttributeValue("y"));
		int code = Integer.parseInt(e.getAttributeValue("code"));
		entities.add(new Key(new Point(x,y),code));
	}
	
	/**
	 * Creates treasure from a string from the file
	 * @param e - The element from the file
	 * @param entities - The current list of entities
	 */
	private static void createTreasure(Element e,Set<Entity> entities) {
		int x = Integer.parseInt(e.getAttributeValue("x"));
		int y = Integer.parseInt(e.getAttributeValue("y"));
		entities.add(new Treasure(new Point(x,y)));
	}
	
	/**
	 * Creates info from a string from the file
	 * @param e - The element from the file
	 * @param entities - The current list of entities
	 */
	private static void createInfo(Element e,Set<Entity> entities) {
		int x = Integer.parseInt(e.getAttributeValue("x"));
		int y = Integer.parseInt(e.getAttributeValue("y"));
		String message = e.getChildText("message");
		entities.add(new InfoField(new Point(x,y),message));
	}
	
	/**
	 * Creates an exit from a string from the file
	 * @param e - The element from the file
	 * @param entities - The current list of entities
	 */
	private static void createExit(Element e,Set<Entity> entities) {
		int x = Integer.parseInt(e.getAttributeValue("x"));
		int y = Integer.parseInt(e.getAttributeValue("y"));
		entities.add(new Exit(new Point(x,y)));
	}
	
	/**
	 * Saves the passed in level to a file
	 * @param level - The level to be saved
	 */
	public static void saveLevel(Level level){
		Cells cells = level.getCells();		// A cells object containing all the cells in the level
	}

}
