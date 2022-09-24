package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * @author Linda Zhang 300570498
 * simulates a level of the game. Contains the playing board and the player.
 */
public class Level {
	Cells cells;
	Player p;
	List<Entity> entities = new ArrayList<>();
	Runnable next;
	SoundPlayer soundplayer;
	
	/**
	 * Makes a simple map for demo
	 */
	public Level(Runnable next){
		this.next = next;
		soundplayer = new SoundPlayer();
		soundplayer.loop(Sound.eightbitsong);
		char[][] map = {
				{'#', '#', '#', '#', '#', '#' ,'#' ,'#', '#', '#'},
				{'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
				{'#', '.', '#', '.', '.', '.', '#', '.', '.', '#'},
				{'#', '.', '#', '.', 's', '.', '.', '.', '.', '#'},
				{'#', '.', '.', '.', '.', '.', '.', '.', '.', '#'},
				{'#', '.', '.', '.', 'L', '.', '#', '#', '.', '#'},
				{'#', '.', '.', '.', '.', '.', '#', '.', 'X', '#'},
				{'#', '#', '#', '#', '#', '#' ,'#' ,'#', '#', '#'}
		};
		entities.add(new Key(new Point(4,6),1)); //demo has one key at point 1,1 with code 1
		entities.add(new InfoField(new Point(1,1), "Message display here!"));
		entities.add(new Treasure(new Point(8,3))); //demo has two treasures at point 8,3 and 8,4
		entities.add(new Treasure(new Point(8,4)));
		entities.add(new Exit(new Point(7,6), this)); //demo has an exit at 7,6 which calls gameOver
		
		cells = new Cells(map);
		p = new Player(cells.getSpawn(), entities);
	}


	/**
	 * Switches back to the home menu.
	 */
	public void gameOver() {
		soundplayer.stopAll();
		next.run();
	}

	/**
	 * Every tick of the game. States of cells may change.
	 */
	public void tick() {
		
		//entities are sometimes removed during the loop (cannot use foreach)
		for(int i=0; i<entities.size();i++) {
			if(entities.get(i).onInteraction(p, cells, soundplayer)) {
				i--;
			}
		}
		
		//update player and cells
		p.tick(cells);
		
		//if exit gone, win game - currently just takes bakc to home screen
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
	
	/**
	 * @return a clone of entities on the level
	 */
	public List<Entity> getEntites(){
		return entities.stream().toList();
	}
}
