package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Linda Zhang  300570498
 * The board of the current level in the game. Cells
 *         consists of many single cell objects.
 */
public class Cells {
	int maxX;
	int maxY;
	Point spawn;
	private final List<List<Cell>> inner = new ArrayList<>();

	/**
	 * @param map cells loaded from the demo
	 */
	public Cells(char[][] map) {
		maxX = map[0].length;
		maxY = map.length;

		for (int x = 0; x < maxX; x++) {
			var tmp = new ArrayList<Cell>();
			inner.add(tmp);
			for (int y = 0; y < maxY; y++) {
				if (map[y][x] == '#') {
					tmp.add(new Cell(new Wall(), x, y));
				} else if (map[y][x] == 's') {
					tmp.add(new Cell(new Spawn(), x, y));
					spawn = new Point(x, y);
				} else if (map[y][x] == 'L') {
					tmp.add(new Cell(new LockedDoor(1), x, y));
				} else if (map[y][x] == 'X') {
					tmp.add(new Cell(new ExitLock(), x, y));
				}else {
					tmp.add(new Cell(new Floor(), x, y));
				}
			}
		}
	}

	/**
	 * Gets the cell on the x and y positon
	 * 
	 * @param x x position on the board
	 * @param y y position on the board
	 * @return the cell on the x and y position. If out of range, return a water
	 *         cell.
	 */
	public Cell get(int x, int y) {
		var isOut = x < 0 || y < 0 || x >= maxX || y >= maxY;
		if (isOut) {
			return new Cell(new Water(), x, y);
		}
		var res = inner.get(x).get(y);
		assert res != null;
		return res;
	}

	/**
	 * Gets the cell on the point
	 * 
	 * @param p point of cell
	 * @return the cell on the point. If out of range, return a water cell.
	 */
	public Cell get(Point p) {
		var isOut = p.x() < 0 || p.y() < 0 || p.x() >= maxX || p.y() >= maxY;
		if (isOut) {
			return new Cell(new Water(), p.x(), p.y());
		}
		var res = inner.get(p.x()).get(p.y());
		assert res != null;
		return res;
	}
	
	/**
	 * Gets all Locked doors on the board
	 * @return the list of Locked doors
	 */
	public List<Cell> getAllLockedDoors(){
		return inner.stream().flatMap(cells -> cells.stream())
						.filter(c -> c.state() instanceof LockedDoor)
						.toList();
	}
	
	/**
	 * @return the cell that represents the ExitLock
	 */
	public Cell getExitLock() {
		for(List<Cell> cells: inner) {
			for(Cell cell: cells) {
				if(cell.state() instanceof ExitLock) {
					return cell;
				}
			}
		}
		throw new IllegalStateException("No ExitLock exists on board!");
	}

	/**
	 * @return the point at which the player should spawn
	 */
	public Point getSpawn() {
		return spawn;
	}

}