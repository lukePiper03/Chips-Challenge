package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
	private Integer levelNum;
	private Optional<Monster> monster;
	private int hasEnded = 0;
	private boolean endSequenceStarted = false;
	private Optional<Runnable> end = Optional.empty();

	/**
	 * Makes a Level WITHOUT a Monster
	 * @param next the next 'phase' the game will be in (e.g. homescreen, next level)
	 * @param soundPlayer the sound to play when level is started
	 * @param map the map of Cells that make up the Level
	 * @param entities the Set of Entities (Key, Treasure, etc) for the Level
	 * @param levelNum the level number
	 */
	public Level(Runnable next, SoundPlayer soundPlayer, char[][] map, Set<Entity> entities, Integer levelNum){
		this.next = next;
		this.soundPlayer = soundPlayer; //remove later
		timeElapsed = 0;
		this.entities = entities;
		this.levelNum = levelNum; //to track what level it's on
		
		cells = new Cells(map);
		p = new Player(cells.getSpawn(), entities);
		monster = Optional.empty();
	}
	
	/**
	 * Makes a Level WITH a Monster
	 * @param next the next 'phase' the game will be in (e.g. homescreen, next level)
	 * @param soundPlayer the sound to play when level is started
	 * @param map the map of Cells that make up the Level
	 * @param entities the Set of Entities (Key, Treasure, etc) for the Level
	 * @param levelNum the level number
	 * @param m the monster of the game, if any
	 */
	public Level(Runnable next, SoundPlayer soundPlayer, char[][] map, Set<Entity> entities, Integer levelNum, Monster m){
		this.next = next;
		this.soundPlayer = soundPlayer;
		timeElapsed = 0;
		this.entities = entities;
		this.levelNum = levelNum; //to track what level it's on
		
		cells = new Cells(map);
		p = new Player(cells.getSpawn(), entities);
		monster = Optional.of(m);
	}

	/**
	 * (Temporarily) Switches back to the home menu.
	 * (can switch to next levels once created)
	 */
	public void gameOver() {
		endSequenceStarted = true; //triggers the next Runnable in tick
		
		if(hasEnded == 1) { //for now, save data once
			end.ifPresent(r -> r.run());
			try {
				Recorder.recorder.saveToFile("Example"); //change later
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//new Thread(() -> soundPlayer.fadeOut(Sound.eightbitsong, 50)).start();

	}
	
	/**
	 * @param r The runnable to call for ending sequence
	 */
	public void setLevelEnd(Runnable r) { end = Optional.of(r);}

	/**
	 * Every tick of the game. States of cells and entities may change.
	 */
	public void tick() {
		//if(timeElapsed == 0) new Thread(() -> soundPlayer.loop(Sound.eightbitsong,50)).start();
		timeElapsed++;
		
		//if monster is touching player, end the game
		monster.ifPresent(m -> {
			if(m.getPos().equals(p.getPos())) gameOver();
		});
		
		//if player has active InfoField but is not on it anymore, make it null
		p.getActiveInfoField().ifPresent(i -> {
			if(!p.getPos().equals(p.getActiveInfoField().get().getPos())) p.removeActiveInfoField();
		});
		
		//call onInteraction on entities touching player
		entities.stream().filter(e -> p.getPos().equals(e.getPos())).forEach(e -> e.onInteraction(p, cells));

		//check for gameOver
		p.entitiesToRemove().stream().filter(e -> e instanceof Exit).findAny().ifPresent(e -> gameOver());
				
		//remove entities that need removing
		p.entitiesToRemove().stream().forEach(e -> entities.remove(e));
		p.entitiesToRemove().clear();
		
		//update player, monster and cells
		p.tick(cells);
		monster.ifPresent(m -> m.tick(cells));
		
		if(endSequenceStarted)hasEnded++;
		if(hasEnded > 50) { //after 50 ticks, leave the Level
			next.run();
		}
		
	}
	
	/**
	 * @return player
	 */
	public Player getPlayer() {return p;}
	
	/**
	 * @return cell board
	 */
	public Cells getCells() {return cells;}
	
	/**
	 * @return a clone of entities on the level in a list
	 */
	public List<Entity> getEntites(){return entities.stream().toList();}
	
	/**
	 * @return levelNum
	 */
	public Integer getLevelNum() {return levelNum;}
	
	/**
	 * @return the Monster optional. Could be empty.
	 */
	public Optional<Monster> getMonster(){return monster;}
}
