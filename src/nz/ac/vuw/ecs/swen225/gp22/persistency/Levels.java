package nz.ac.vuw.ecs.swen225.gp22.persistency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;

public class Levels {
	
	/**
	 * Loads a level based off what name is passed in
	 * 
	 * @param filename - The name of the file to be loaded
	 * @return char[][] - A map of the level in characters to be loaded in cells
	 */
	public static char[][] loadLevel(String filename) {
		String prefix = "./src/nz/ac/vuw/ecs/swen225/gp22/persistency/levels/";	// Filepath prefix
		filename = prefix + filename;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));	// The file reader
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
			reader.close();
			return map;
		} catch (IOException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Formats a string by removing xml tags
	 * @param str - The string to be formatted
	 * @return String - The formatted string
	 */
	public static String formatLine(String str) {
		// Removes the xml tags from the string
		str = str.replace("\t\t<row>", "");
		str = str.replace("</row>", "");
		return str;
	}
	
	/**
	 * Saves the passed in level to a file
	 * @param level - The level to be saved
	 */
	public static void saveLevel(Level level){
		Cells cells = level.getCells();		// A cells object containing all the cells in the level
	}

}
