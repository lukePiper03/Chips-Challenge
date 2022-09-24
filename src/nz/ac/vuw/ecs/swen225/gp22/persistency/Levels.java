package nz.ac.vuw.ecs.swen225.gp22.persistency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp22.domain.*;

public class Levels {
	public static void main(String args[]) {
		Levels.loadLevel("testInput.xml");
	}
	
	/**
	 * Loads a level based off what name is passed in
	 * 
	 * @param filename - The name of the file to be loaded
	 * @return char[][] - A map of the level in characters to be loaded in cells
	 */
	public static char[][] loadLevel(String filename) {
		String prefix = "./src/nz/ac/vuw/ecs/swen225/gp22/persistency/";
		filename = prefix + filename;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			reader.readLine();
			String line = reader.readLine().replace("\t<numRows>", "").replace("</numRows>", "");
			int rows = Integer.parseInt(line);
			line = reader.readLine().replace("\t<numCols>", "").replace("</numCols>", "");
			int cols = Integer.parseInt(line);
			char[][] map = new char[rows][cols];
			List<String> lines = reader.lines().filter(s -> s.startsWith("\t\t<row>"))
//			.forEach(str -> {str = str.replace("\t\t<row>", "");
//							str = str.replace("</row>", "");
//							lines.add(str);
//					});
			.map(str -> str = formatLine(str)).toList();
			IntStream.range(0, rows)
			.forEach(row -> {IntStream.range(0, cols)
					.forEach(col -> map[row][col] = lines.get(row).charAt(col));});
			for(int i=0;i<rows;i++) {
				for(int j=0;j<cols;j++) {
					System.out.print(map[i][j]);
				}
				System.out.print("\n");
			}
			reader.close();
			return map;
		} catch (IOException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
		return null;
	}
	
	public static String formatLine(String str) {
		str = str.replace("\t\t<row>", "");
		str = str.replace("</row>", "");
		return str;
	}
	
	public static void saveLevel(List<List<Cell>> cells){
		
	}

}
