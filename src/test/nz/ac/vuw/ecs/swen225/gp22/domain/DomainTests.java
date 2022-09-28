package test.nz.ac.vuw.ecs.swen225.gp22.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;
import nz.ac.vuw.ecs.swen225.gp22.domain.Entity;
import nz.ac.vuw.ecs.swen225.gp22.domain.Exit;
import nz.ac.vuw.ecs.swen225.gp22.domain.InfoField;
import nz.ac.vuw.ecs.swen225.gp22.domain.Key;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.domain.Point;
import nz.ac.vuw.ecs.swen225.gp22.domain.Treasure;
import nz.ac.vuw.ecs.swen225.gp22.renderer.SoundPlayer;

class DomainTests {

	@Test void initialiseLevel() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
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
		Set<Entity> entities = Set.of(new Key(new Point(1,1),1), 
										new Treasure(new Point(1,2)), 
										new InfoField(new Point(1,3), ""),
										new Exit(new Point(1,4)));
		Level l = new Level(next,s,map,entities);
		
		assert map.length == l.getCells().getMaxY(); //check Y boundary
		for(int y = 0; y < map.length; y++) assert map[y].length == l.getCells().getMaxX(); //check X boundaries
		assert mapsMatch(map, l.getCells().toMap()); //check map
		assert entities.equals(l.getPlayer().entitiesOnBoard()); //check entities
		assert entities.stream().toList().equals(l.getEntites()); //check entities toList
		l.getCells().get(-1,0).state(); //point not on map is allowed
		assert l.getPlayer().direction() == Direction.None; //player does not move
		l.tick(); //tick level without moving
		l.gameOver(); //stop music looping
	}
	
	@Test void moveOnceLegal() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = Set.of();
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick();
		l.gameOver();

	}
	
	@Test void moveOnceIllegal1() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = Set.of();
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Left);
		try { l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to illegal pos
		}catch(IllegalArgumentException e) {}
		
		l.tick();
		l.gameOver();

	}
	@Test void moveOnceIllegal2() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'L', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = Set.of();
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Left);
		try { l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to illegal pos
		}catch(IllegalArgumentException e) {}
		
		l.tick();
		l.gameOver();

	}
	
	@Test void moveOnceIllegal3() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'X', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = Set.of();
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Left);
		try { l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to illegal pos
		}catch(IllegalArgumentException e) {}
		
		l.tick();
		l.gameOver();

	}
	
	@Test void moveToExitLegal() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Exit exit = new Exit(new Point(2,1));
		entities.add(exit);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on exit
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		entities.stream().forEach(e -> e.onInteraction(l.getPlayer(), l.getCells(), s)); //should not throw exception
		l.tick(); //should call gameOver

	}
	
	@Test void moveToExitIllegal() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		entities.add(new Exit(new Point(2,1)));
		Level l = new Level(next,s,map,entities);
		
		try {
			entities.stream().forEach(e -> e.onInteraction(l.getPlayer(), l.getCells(), s)); //player not on exit
		}catch(IllegalStateException e) {}
		
		l.tick(); //should not call gameOver
		l.gameOver();
	}
	
	@Test void moveToInfoFieldLegal1() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		InfoField i = new InfoField(new Point(2,1), "");
		entities.add(i);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on InfoField
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick();
		
		assert l.getPlayer().getActiveInfoField() == i; //should make activeInfoField i
		
		l.getPlayer().direction(Direction.Left);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to back from InfoField
		
		assert l.getPlayer().getPos().equals(new Point(1,1));
		assert l.getPlayer().getOldPos().equals(new Point(2,1));
		
		l.tick(); //should make activeInfoPos null
		assert l.getPlayer().getActiveInfoField() == null;
		
		l.gameOver();
	}
	
	@Test void moveToInfoFieldLegal2() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		InfoField i = new InfoField(new Point(2,1), "");
		entities.add(i);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on InfoField
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick();
		
		assert l.getPlayer().getActiveInfoField() == i; //should make activeInfoField i
		
		l.tick(); //tick again to check that infoField is only displayed once
		assert l.getPlayer().getActiveInfoField() == i;
		
		l.gameOver();
	}
	
	@Test void moveToInfoFieldIllegal() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		InfoField i = new InfoField(new Point(2,1), "");
		entities.add(i);
		Level l = new Level(next,s,map,entities);
		
		try {
			entities.stream().forEach(e -> e.onInteraction(l.getPlayer(), l.getCells(), s)); //player not on InfoField
		}catch(IllegalStateException e) {}
		
		assert l.getPlayer().getActiveInfoField() == null;
		
		l.tick();
		l.gameOver();
	}
	
	@Test void moveToKeyLegal() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', 'L'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Key key = new Key(new Point(2,1), 1);
		entities.add(key);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Key
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick(); //call onInteraction for the key
		assert l.getPlayer().inventory().contains(key); //key added to inventory
		assert entities.isEmpty(); //key removed from entities
		assert !mapsMatch(map,l.getCells().toMap()); //cells should now be different
		char[][] newMap = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '.'},
				{'#', '#', '#', '#'}
		};
		assert mapsMatch(newMap, l.getCells().toMap()); //locked door is unlocked when key is picked up
		l.gameOver();
	}
	
	@Test void moveToKeyIllegalNoLockedDoor() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '.'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Key key = new Key(new Point(2,1), 1);
		entities.add(key);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Key
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		try {
			l.tick(); //call onInteraction for the key, but Locked Door does not exist
		}catch(AssertionError e) {}
		l.gameOver();
	}
	
	@Test void moveToKeyIllegalNoMatchingCode() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', 'L'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Key key = new Key(new Point(2,1), 2);
		entities.add(key);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Key
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		try {
			l.tick(); //call onInteraction for the key, but Locked Door does not match the code of the key
		}catch(AssertionError e) {}
		l.gameOver();
	}
	
	@Test void moveToKeyIllegalNotOnKey() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Key key = new Key(new Point(2,1),1);
		entities.add(key);
		Level l = new Level(next,s,map,entities);
		
		try {
			entities.stream().forEach(e -> e.onInteraction(l.getPlayer(), l.getCells(), s)); //player not on Key
		}catch(IllegalStateException e) {}
		
		assert l.getPlayer().inventory().isEmpty();
		
		l.tick();
		l.gameOver();
	}
	
	@Test void moveToTreasureLegal1() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', 'X'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Treasure t = new Treasure(new Point(2,1));
		entities.add(t);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Treasure
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick(); 
		assert l.getPlayer().treasuresToCollect() == 0; //no more treasures to collect
		assert entities.isEmpty(); //treasure removed from entities
		assert !mapsMatch(map,l.getCells().toMap()); //cells should now be different
		char[][] newMap = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '.'},
				{'#', '#', '#', '#'}
		};
		assert mapsMatch(newMap, l.getCells().toMap()); //exit lock is unlocked when treasure is picked up
		l.gameOver();
	}
	
	@Test void moveToTreasureLegal2() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'s', '.', '.', 'X'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		entities.add(new Treasure(new Point(1,1)));
		entities.add(new Treasure(new Point(2,1)));
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Treasure
		
		assert l.getPlayer().getPos().equals(new Point(1,1));
		assert l.getPlayer().getOldPos().equals(new Point(0,1));
		
		l.tick(); 
		assert l.getPlayer().treasuresToCollect() == 1; //1 more treasures to collect
		assert entities.size() == 1; //treasure removed from entities
		assert mapsMatch(map,l.getCells().toMap()); //cells should now be different
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Treasure
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		l.tick(); 
		assert l.getPlayer().treasuresToCollect() == 0; //no more treasures to collect
		assert entities.isEmpty(); //treasure removed from entities
		assert !mapsMatch(map,l.getCells().toMap()); //cells should now be different
		char[][] newMap = {
				{'#', '#', '#', '#'},
				{'s', '.', '.', '.'},
				{'#', '#', '#', '#'}
		};
		assert mapsMatch(newMap, l.getCells().toMap()); //exit lock is unlocked when treasure is picked up
		l.gameOver();
	}
	
	@Test void moveToTreasureIllegalNotOnTreasure() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Treasure key = new Treasure(new Point(2,1));
		entities.add(key);
		Level l = new Level(next,s,map,entities);
		
		try {
			entities.stream().forEach(e -> e.onInteraction(l.getPlayer(), l.getCells(), s)); //player not on Key
		}catch(IllegalStateException e) {}
		
		assert l.getPlayer().treasuresToCollect() == 1;
		
		l.tick();
		l.gameOver();
	}
	
	@Test void moveToTreasureIllegalNoExitLock() {
		Runnable next = () -> {};
		SoundPlayer s = new SoundPlayer();
		char[][] map = {
				{'#', '#', '#', '#'},
				{'#', 's', '.', '#'},
				{'#', '#', '#', '#'}
		};
		Set<Entity> entities = new HashSet<>();
		Treasure t = new Treasure(new Point(2,1));
		entities.add(t);
		Level l = new Level(next,s,map,entities);
		
		l.getPlayer().direction(Direction.Right);
		l.getPlayer().move(l.getPlayer().direction(), l.getCells()); //move to legal pos on Treasure
		
		assert l.getPlayer().getPos().equals(new Point(2,1));
		assert l.getPlayer().getOldPos().equals(new Point(1,1));
		
		try {
			l.tick(); //no exit lock on board
		}catch(IllegalStateException e) {}
		l.gameOver();
	}
	
	//helper method to check if the map of cells match
	private boolean mapsMatch(char[][] map, char[][] cellMap) {
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				if(map[y][x] != cellMap[y][x]) return false;
			}
		}
		return true;
	}

}
