package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nz.ac.vuw.ecs.swen225.gp22.recorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;

/**
 * @author Linda Zhang 300570498
 * simulates a level of the game. Contains the playing board and the player.
 */
public class Level {
	private Cells cells;
	private Player p;
	private Set<Entity> entities;
	private Runnable next;
	private SoundPlayer soundPlayer;
	private int timeElapsed;

	/**
	 * Makes a Level
	 * @param next the next 'phase' the game will be in (e.g. homescreen, next level)
	 * @param soundPlayer the sound to play when level is started
	 * @param map the map of Cells that make up the Level
	 * @param entities the Set of Entities (Key, Treasure, etc) for the Level
	 */
	public Level(Runnable next, SoundPlayer soundPlayer, char[][] map, Set<Entity> entities){
		this.next = next;
		this.soundPlayer = soundPlayer;
		timeElapsed = 0; //change later to a coundown (and a real second timer)
		this.entities = entities;
		
		cells = new Cells(map);
		p = new Player(cells.getSpawn(), entities);
	}


	/**
	 * (Temporarily) Switches back to the home menu.
	 * (can switch to next levels once created)
	 */
	public void gameOver() {
		try {
			Recorder.recorder.saveToFile("Example");
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(() -> soundPlayer.fadeOut(Sound.eightbitsong, 50)).start();
		next.run();
	}

	/**
	 * Every tick of the game. States of cells and entities may change.
	 */
	public void tick() {
		if(timeElapsed == 0) new Thread(() -> soundPlayer.loop(Sound.eightbitsong,50)).start();
		timeElapsed++;
		
		//if player has active InfoField but is not on it anymore, make it null
		if(p.getActiveInfoField() != null && !p.getPos().equals(p.getActiveInfoField().getPos())) p.setActiveInfoField(null);
		
		//call onInteraction on entities touching player
		entities.stream().filter(e -> p.getPos().equals(e.getPos())).forEach(e -> e.onInteraction(p, cells, soundPlayer));

		//check for gameOver
		p.entitiesToRemove().stream().filter(e -> e instanceof Exit).findAny().ifPresent(e -> gameOver());
				
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
	 * @return a clone of entities on the level in a list
	 */
	public List<Entity> getEntites(){
		return entities.stream().toList();
	}
}
