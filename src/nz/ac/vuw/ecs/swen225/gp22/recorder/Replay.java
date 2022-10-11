package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @author Quinten Smit 300584150
 *
 */
public class Replay {
	Element replayElement;
	double time;
	double replaySpeed = 1;
	boolean autoReplay = true;
	
	
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
	
	/**
	 * @return
	 */
	public String getLevelPath() {
		return replayElement.getChild("map").getText();
	}
	
	/**
	 * 
	 */
	public void autoReplay() {autoReplay = true;}

	/**
	 * 
	 */
	public void stepByStep() {autoReplay = false;}
	
	
	
	public String toString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(replayElement);
	}
	
	/*private class Event{
		int time;
		String actor;
		Direction action;
		
		public Event(int time, String actor, Direction action) {
			this.time = time;
			this.actor = actor;
			this.action = action;
		}
		
		public Event(Element Event) {
			
		}
	}*/
}
