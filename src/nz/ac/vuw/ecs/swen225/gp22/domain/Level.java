package nz.ac.vuw.ecs.swen225.gp22.domain;

import nz.ac.vuw.ecs.swen225.gp22.domain.Cells;
import nz.ac.vuw.ecs.swen225.gp22.domain.Player;

public class Level {
	Cells cells;
	Player p;
//	Entities
	
	// make it recieve the text file to generate a map
	
	//make a simple map for demo (change later)
	public Level(){
//		char[][] map = new char[10][8];
		char[][] map = {
				{'#', '#', '#', '#', '#', '#' ,'#' ,'#', '#', '#'},
				{'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
				{'#', '.', '#', '.', '.', '.', '#', '.', '.', '#'},
				{'#', '.', '#', '.', 's', '.', '.', '.', '.', '#'},
				{'#', '.', '.', '.', '.', '.', '#', '.', '.', '#'},
				{'#', '.', '.', '.', '.', '#', '#', '#', '.', '#'},
				{'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
				{'#', '#', '#', '#', '#', '#' ,'#' ,'#', '#', '#'}
		};
		cells = new Cells(map);
		p = new Player(cells.getSpawn());
	}
	
	public void tick() {
		//change this
		p.tick(cells);
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Cells getCells() {
		return cells;
	}
}
