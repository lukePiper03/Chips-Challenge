package nz.ac.vuw.ecs.swen225.gp22.persistency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;

/**
 * Creates and saves levels to and from files
 * @author Jarvis Warnock 300578342
 *
 */
public class Levels {
	
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
			BufferedReader reader = new BufferedReader(new FileReader(filename));	// The file reader
			Integer levelNum = getLevelNum(reader.readLine());
			System.out.println(levelNum);
			// Creates the map for the level
			reader.mark(100000);
			reader.readLine();
			String line = reader.readLine().replace("\t<numRows>", "").replace("</numRows>", "");	// Current line of the reader
			int rows = Integer.parseInt(line);		// Amount of rows in the saved level
			line = reader.readLine().replace("\t<numCols>", "").replace("</numCols>", "");
			int cols = Integer.parseInt(line);		// Amount of columns in the saved level
			char[][] map = new char[rows][cols];	// A map of all the tiles in the level
			List<String> lines = reader.lines().filter(s -> s.startsWith("\t\t<row>"))
			.map(str -> str = formatLine(str)).toList();
			IntStream.range(0, rows)
			.forEach(row -> {IntStream.range(0, cols)
					.forEach(col -> map[row][col] = lines.get(row).charAt(col));});
			
			// Creates the entities for the level
			reader.reset();
			reader.lines().filter(s -> s.startsWith("\t<key>")).forEach(k -> createKey((String)k,entities));reader.reset();
			reader.lines().filter(s -> s.startsWith("\t<treasure>")).forEach(k -> createTreasure((String)k,entities));reader.reset();
			reader.lines().filter(s -> s.startsWith("\t<info>")).forEach(k -> createInfo((String)k,entities));reader.reset();
			reader.lines().filter(s -> s.startsWith("\t<exit>")).forEach(k -> createExit((String)k,entities));reader.reset();
			reader.close();
			//entities.stream().forEach(System.out::println);
			return new Level(next,soundPlayer,map,entities,levelNum);
		} catch (IOException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method to get the level number based off the filename
	 * @param filename - the filename to perform the operation on
	 * @return The level number or 0 if there is not valid number in the name
	 */
	private static Integer getLevelNum(String str) {
		str = str.replace("<levelNum>", "");
		str = str.replace("</levelNum>", "");
		return Integer.parseInt(str);
	}
	
	/**
	 * Formats a string by removing xml tags
	 * @param str - The string to be formatted
	 * @return String - The formatted string
	 */
	private static String formatLine(String str) {
		// Removes the xml tags from the string
		str = str.replace("\t\t<row>", "");
		str = str.replace("</row>", "");
		return str;
	}
	
	/**
	 * Creates a key from a string from the file
	 * @param s - The string from the file
	 * @param entities - The current list of entities
	 */
	private static void createKey(String s,Set<Entity> entities) {
		int x,y,code;
		s = s.replace("\t<key>", "");s = s.replace("</key>", "");
		Scanner sc = new Scanner(s);
		x = sc.nextInt();
		y = sc.nextInt();
		code = sc.nextInt();
		sc.close();
		entities.add(new Key(new Point(x,y),code));
	}
	
	/**
	 * Creates treasure from a string from the file
	 * @param s - The string from the file
	 * @param entities - The current list of entities
	 */
	private static void createTreasure(String s,Set<Entity> entities) {
		int x,y;
		s = s.replace("\t<treasure>", "");s = s.replace("</treasure>", "");
		Scanner sc = new Scanner(s);
		x = sc.nextInt();
		y = sc.nextInt();
		sc.close();
		entities.add(new Treasure(new Point(x,y)));
	}
	
	/**
	 * Creates info from a string from the file
	 * @param s - The string from the file
	 * @param entities - The current list of entities
	 */
	private static void createInfo(String s,Set<Entity> entities) {
		int x,y;
		String message;
		s = s.replace("\t<info>", "");s = s.replace("</info>", "");
		Scanner sc = new Scanner(s);
		x = sc.nextInt();
		y = sc.nextInt();
		message = sc.nextLine();
		sc.close();
		entities.add(new InfoField(new Point(x,y),message));
	}
	
	/**
	 * Creates an exit from a string from the file
	 * @param s - The string from the file
	 * @param entities - The current list of entities
	 */
	private static void createExit(String s,Set<Entity> entities) {
		int x,y;
		s = s.replace("\t<exit>", "");s = s.replace("</exit>", "");
		Scanner sc = new Scanner(s);
		x = sc.nextInt();
		y = sc.nextInt();
		sc.close();
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
