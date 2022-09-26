package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nz.ac.vuw.ecs.swen225.gp22.persistency.Levels;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * @author Linda Zhang 300570498
 * simulates a level of the game. Contains the playing board and the player.
 */
public class Level {
	Cells cells;
	Player p;
	Set<Entity> entities = new HashSet<>();
	Runnable next;
	SoundPlayer soundPlayer;
	
	/**
	 * Makes a simple map for demo
	 * @param next the next 'phase' the game will be in (e.g. homescreen, next level)
	 */
	public Level(Runnable next, SoundPlayer soundPlayer){
		this.next = next;
		this.soundPlayer = soundPlayer;
		//soundPlayer.loop(Sound.eightbitsong);
		new Thread(() -> soundPlayer.loop(Sound.eightbitsong,50)).start();
		char[][] map = Levels.loadLevel("level1.xml"); //load map from Persistency

		entities.add(new Key(new Point(4,6),1)); //demo has one key at point 1,1 with code 1
		
		entities.add(new InfoField(new Point(1,1), "Message display here!"));
		entities.add(new Treasure(new Point(8,3))); //demo has two treasures at point 8,3 and 8,4
		entities.add(new Treasure(new Point(8,4)));
		entities.add(new Exit(new Point(7,6), this)); //demo has an exit at 7,6 which calls gameOver
		
		cells = new Cells(map);
		p = new Player(cells.getSpawn(), entities);
	}


	/**
	 * Switches back to the home menu. CHANGE LATER TO SWITCH TO DIFF LEVELS
	 */
	public void gameOver() {
		//soundPlayer.stopAll();
		new Thread(() -> soundPlayer.fadeOut(Sound.eightbitsong, 50)).start(); // doesn't work fully just yet
		next.run();
	}

	/**
	 * Every tick of the game. States of cells may change.
	 */
	public void tick() {
		//call onInteraction on entities touching player
		entities.stream().filter(e -> p.getPos().equals(e.getPos())).forEach(e -> e.onInteraction(p, cells, soundPlayer));

		//remove entities that need removing
		p.entitiesToRemove().stream().forEach(e -> entities.remove(e));
		p.entitiesToRemove().clear();
		
		//update player and cells
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
	
	/**
	 * @return a clone of entities on the level
	 */
	public List<Entity> getEntites(){
		return entities.stream().toList();
	}
}
