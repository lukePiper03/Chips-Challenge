package nz.ac.vuw.ecs.swen225.gp22.domain;

/**
 * @author Linda Zhang
 * simulates a level of the game. Contains the playing board and the player.
 */
public class Level {
	Cells cells;
	Player p;
	//Entities
	
	/**
	 * Makes a simple map for demo
	 */
	public Level(){
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
	
	/**
	 * Every tick of the game. States of cells and obejcts may change.
	 */
	public void tick() {
		p.tick(cells);
	}
	
	/**
	 * @return player
	 */
	public Player getPlayer() {
		return p;
	}
	
	/**
	 * @return cell board
	 */
	public Cells getCells() {
		return cells;
	}
}
