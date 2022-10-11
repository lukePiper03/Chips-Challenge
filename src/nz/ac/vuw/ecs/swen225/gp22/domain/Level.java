package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import actor.spi.Actor;

/**
 *  Simulates a level of the game. Contains the playing board, entities, and player.
 *  
 * @author Linda Zhang 300570498
 */
public class Level {
	private Cells cells;
	private Player p;
	private Set<Entity> entities;
	private Runnable next;
	private Integer levelNum;
	private List<Actor> monsters;
	private int hasEnded = 0;
	private boolean endSequenceStarted = false; //for time delay once game over
	private Optional<Runnable> end = Optional.empty();
	private Runnable die;
	private double countdown; 
	
	/**
	 * Makes a Level.
	 * @param next the next 'phase' the game will be in (e.g. homescreen, next level)
	 * @param die the phase when the player dies (when the user loses)
	 * @param map the map of Cells that make up the Level
	 * @param entities the Set of Entities (Key, Treasure, etc) for the Level
	 * @param levelNum the level number
	 * @param m the monster of the game, if any
	 * @param countdown the countdown of the Level
	 * @param p the player in the game
	 */
	public Level(Runnable next, Runnable die,char[][] map, 
					Set<Entity> entities, Integer levelNum, List<Actor> m, double countdown, Player p){
		this.next = next;
		this.entities = entities;
		this.levelNum = levelNum;
		cells = new Cells(map);
		this.p = p;
		this.monsters = m;
		
		this.die = die; //runnable to call when player dies
		this.countdown = countdown; 
	}

	/**
	 * Switches the screen to the next Level/winning screen whgen the player wins.
	 */
	public void gameOver() {
		endSequenceStarted = true; //triggers the next Runnable in tick
		end.ifPresent(r -> r.run());
	}
	
	/**
	 * Switches the screen to homescreen/dying screen when the player is killed.
	 */
	public void playerDiesGameOver() {
		end.ifPresent(r -> r.run());
		die.run();
	}
	
	/**
	 * Set the end Runnable.
	 * @param r The runnable to call for ending sequence
	 */
	public void setLevelEnd(Runnable r) { end = Optional.of(r);}

	/**
	 * Every tick of the game. States ofthe game may change.
	 */
	public void tick() {
		countdown -= 0.04;
		if(countdown <= 0) {playerDiesGameOver(); return;} //player dies if time runs out
		
		//if monster is touching player, player dies
		monsters.forEach(m -> {
			if(m.getPos().equals(p.getPos())) playerDiesGameOver();
		});
		
		//if player touches water, player dies (unless boots in inventory)
		if(!p.bootsInInventory() && cells.get(p.getPos()).state() instanceof Water) playerDiesGameOver();
		
		//if player has active InfoField but is not on it anymore, make it en empty Optional
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
		monsters.forEach(m -> m.tick(cells));
		
		if(endSequenceStarted)hasEnded++;
		if(hasEnded > 25) { //after 25 ticks, leave the Level
			next.run();
		}
		
	}
	
	/**
	 * Gets the player of the level.
	 * @return player
	 */
	public Player getPlayer() {return p;}
	
	/**
	 * Gets the cells of the Level.
	 * @return cell board
	 */
	public Cells getCells() {return cells;}
	
	/**
	 * Gets an immutable version of the entities of the Level.
	 * @return a clone of entities on the level in a list
	 */
	public List<Entity> getEntites(){return entities.stream().toList();}
	
	/**
	 * Gets the level number this Level represents.
	 * @return levelNum
	 */
	public Integer getLevelNum() {return levelNum;}
	
	/**
	 * Gets the monsters of this Level.
	 * @return the list of Actors (monsters)
	 */
	public List<Actor> getMonsters(){return monsters;}
	
	/**
	 * Gets the coundown timer of the Level.
	 * @return get countdown of the Level
	 */
	public double getCountdown() {return countdown;}
}
