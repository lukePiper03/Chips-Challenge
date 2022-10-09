package test.nz.ac.vuw.ecs.swen225.gp22.persistency;

import nz.ac.vuw.ecs.swen225.gp22.persistency.*;
import nz.ac.vuw.ecs.swen225.gp22.domain.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class PersistencyTests {
	
	/**
	 * Load a valid level
	 */
	@Test void createValidLevel1() {
		Runnable next = () -> {};
		Runnable end = () -> {};
		assert(Levels.loadLevel(next, end, "level1.xml") instanceof Level);
	}
	
	/**
	 * Load a valid level
	 */
	@Test void createValidLevel2() {
		Runnable next = () -> {};
		Runnable end = () -> {};
		assert(Levels.loadLevel(next, end, "level2.xml") instanceof Level);
	}
	
	/**
	 * Try to load a level with invalid filename
	 */
	@Test void createInvalidLevel1() {
		Runnable next = () -> {};
		Runnable end = () -> {};
		assert Levels.loadLevel(next, end, "test") == null;
	}
	
	/**
	 * Try to load a level with invalid filename
	 */
	@Test void saveValidLevel1() {
		Runnable next = () -> {};
		Runnable end = () -> {};
		Level testLevel1 = Levels.loadLevel(next, end, "level1.xml");
		Levels.saveLevel(testLevel1, "testLevel.xml");
		Level testLevel2 = Levels.loadLevel(next, end, "testLevel.xml");
		assert testLevel1.getCountdown() == testLevel2.getCountdown();
		assert testLevel1.getLevelNum() == testLevel2.getLevelNum();
		assert testLevel1.getEntites().size() == testLevel2.getEntites().size();
		for(int i=0;i<testLevel1.getEntites().size();i++) {
			//assert testLevel1.getEntites().get(i).getName() == testLevel2.getEntites().get(i).getName();
			//assert testLevel1.getEntites().get(i).getPos().x() == testLevel2.getEntites().get(i).getPos().x();
		}

	}
	
}
