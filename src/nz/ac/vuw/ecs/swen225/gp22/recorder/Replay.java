package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import nz.ac.vuw.ecs.swen225.gp22.global.Direction;

/**
 * @author Quinten Smit 300584150
 *
 */
public class Replay {
	Element replayElement;
	double time;
	Consumer<Direction> movePlayer;
	
	
	/**
	 * Loads a Recording from a file
	 * @param fileName The name of the file to load from
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Replay(String fileName) throws JDOMException, IOException {
		replayElement = ((Document) (new SAXBuilder()).build(new File(fileName))).getRootElement();
		time = Integer.parseInt(replayElement.getChild("time").getText());
		//Element eventsElement = replayElement.getChild("events");
	}
	
	public void setController(Consumer<Direction> movePlayer) {
		this.movePlayer = movePlayer;
	}
	
	/**
	 * @return
	 */
	public String getLevelPath() {
		return replayElement.getChild("map").getText();
	}
	
	public String toString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(replayElement);
	}
	
	/**
	 * 
	 */
	public boolean movePlayer() {
		List<Element> eventList = replayElement.getChild("events").getChildren("events");
		Optional<Element> event = eventList.stream().filter((e)->(Integer.parseInt(e.getAttribute("time").getValue()) == time)).findFirst();
		if (event.isEmpty())return false;
		Direction direction = Direction.valueOf(event.get().getChildText("action"));
		movePlayer.accept(direction);
		time++;
		return true;
	}
}
