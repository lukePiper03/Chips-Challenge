package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;

public class Recorder {
	Document document;
	Element eventsElement;
	/**
	 * @param mapFileName 
	 * 
	 */
	public Recorder(String mapFileName) {
		document = new Document();
		Element replayElement = new Element("replay");
		document.setRootElement(replayElement);
		
		Element mapElement = new Element("map");
		//Maybe save the map as well here? Could be expanded later
		mapElement.addContent(mapFileName);
		replayElement.addContent(mapElement);
		
		Element eventsElement = new Element("events");
		replayElement.addContent(eventsElement);
	}
	
	/**
	 * @param time The time at which the action was performed
	 * @param action The direction that the player moved in
	 * @param length The length of time that the action was performed over
	 */
	public void savePlayerMoveEvent(int time, Direction action, int length) {
		Element eventElement = new Element("event");
		eventElement.setAttribute("time", time+"");
		
		//Could be used for non player actions
		Element actorElement = new Element("actor");
		actorElement.addContent("Player");
		eventElement.addContent(actorElement);
		
		Element actionElement = new Element("action");
		actionElement.setAttribute("length", length+"");
		actionElement.addContent(action.toString());
		eventElement.addContent(actionElement);
		
		eventsElement.addContent(eventElement);
	}
	
	public void saveToFile(String name){
		
	}
	
	/**
	 * 
	 */
	public static void readFromFile() {
		try {
			Element channelElement = ((Document) (new SAXBuilder()).build(new File(""))).getRootElement();
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
