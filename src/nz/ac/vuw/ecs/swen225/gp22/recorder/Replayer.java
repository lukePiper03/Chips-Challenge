package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Replayer {
	Element replayElement;
	int time;
	
	/**
	 * Loads a Recorder from a file
	 * @param fileName The name of the file to load from
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Replayer(String fileName) throws JDOMException, IOException {
		replayElement = ((Document) (new SAXBuilder()).build(new File(fileName))).getRootElement();
		time = Integer.parseInt(replayElement.getChild("time").getText());
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
	
}
