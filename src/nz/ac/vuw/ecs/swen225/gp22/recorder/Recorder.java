package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import nz.ac.vuw.ecs.swen225.gp22.app.Direction;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;

/**
 * @author Quinten Smit 300584150
 *
 */
public class Recorder {
	Document document;
	Element eventsElement;
	@SuppressWarnings("javadoc")
	public static Recorder recorder = new Recorder((Level) null); 
	
	/**
	 * Makes a new recorder which is ready to receive inputs
	 * @param level the current level
	 */
	public Recorder(Level level) {
		document = new Document();
		Element replayElement = new Element("replay");
		document.setRootElement(replayElement);
		
		Element mapElement = new Element("map");
		//Maybe save the map as well here? Could be expanded later
		//mapElement.setText(level.getFileName());
		replayElement.addContent(mapElement);
		
		eventsElement = new Element("events");
		replayElement.addContent(eventsElement);
	}
	
	/**
	 * Loads a Recorder from a file
	 * @param fileName The name of the file to load from
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Recorder(String fileName) throws JDOMException, IOException {
		document = (Document) (new SAXBuilder()).build(new File(fileName));
	}
	
	/**
	 * Adds a new player moved event to the recorder
	 * @param time The time at which the action was performed
	 * @param action The direction that the player moved in
	 */
	public void savePlayerMoveEvent(int time, Direction action) {
		Element eventElement = new Element("event");
		eventElement.setAttribute("time", time+"");
		
		//Could be used for non player actions
		Element actorElement = new Element("actor");
		actorElement.setText("Player");
		eventElement.addContent(actorElement);
		
		Element actionElement = new Element("action");
		//actionElement.setAttribute("length", length+"");
		actionElement.setText(action.toString());
		eventElement.addContent(actionElement);
		
		eventsElement.addContent(eventElement);
	}
	
	/**
	 * Saves the current Record to a file in the XML format
	 * @param fileName the name of the file to save to
	 * @throws IOException 
	 */
	public void saveToFile(String fileName) throws IOException{
		new XMLOutputter(Format.getPrettyFormat()).output(document, new FileWriter(fileName+".xml"));
	}
	
	public String toString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
	}
	
	
	/*****************************************************************************
	 * Testing methods
	 *****************************************************************************/
	
	/**
	 * Testing main, will create a Recorder, print it, save it to test.xml,
	 * and then load it back into another Recorder from text.xml and print it out. 
	 * @param args
	 */
	public static void main(String[] args){
	    Recorder recorder1 = makeTestRecorder();
	    System.out.println("One: " + recorder1.toString());
	    
	    try {
			recorder1.saveToFile("test");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    Recorder recorder2 = null;
	    try {
	    	recorder2 = new Recorder("test.xml");
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("Two " + recorder2.toString());
	}
	
	private static Recorder makeTestRecorder() {
		Recorder recorder = new Recorder((Level)null);
	    recorder.savePlayerMoveEvent(0, Direction.Left);
	    recorder.savePlayerMoveEvent(5, Direction.Right);
	    return recorder;
	}
	
}