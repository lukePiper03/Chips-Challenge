package nz.ac.vuw.ecs.swen225.gp22.recorder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
public class Replay extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1874411741562700245L;
	Element replayElement;
	int time;
	int speed = 33;
	boolean end;
	Consumer<Direction> movePlayer;
	Runnable advanceByTick;
	Timer timer;
	
	
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
		JButton step = new JButton("Advance By Step");
		step.addActionListener(e->advanceByTick());
		
		JButton autoReplay = new JButton("Auto Replay");
		autoReplay.addActionListener(e->startRunning());
		
		JSlider replaySpeed = new JSlider(0,10, speed);
		replaySpeed.addChangeListener((e)->timer.setDelay(speed=((JSlider)e.getSource()).getValue()));
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(e->timer.stop());
		
		add(step);
		add(autoReplay);
		add(replaySpeed);
	}
	
	/**
	 * @param movePlayer The consumer for the player movement
	 */
	public void setController(Consumer<Direction> movePlayer) {
		this.movePlayer = movePlayer;
	}
	
	public void setAdvanceByTick(Runnable advanceByTick) {
		this.advanceByTick = advanceByTick;
	}
	
	public void advanceByTick() {
		if (!end) {
			advanceByTick.run();
			movePlayer();
			if (time >= findEndTime()) {
				end = true;
				timer.stop();
			}
		}
	}
	
	public void startRunning() {
		timer = new Timer(100-speed,unused->{
		      assert SwingUtilities.isEventDispatchThread();
		      advanceByTick();
		});
		timer.start();
	}
	
	/**
	 * @return The the filename of the map that was loaded
	 */
	public String getLevelPath() {
		return replayElement.getChild("map").getText();
	}
	
	public String toString() {
		return new XMLOutputter(Format.getPrettyFormat()).outputString(replayElement);
	}
	
	/**
	 * @return  if the move was stored in the recorder
	 * 
	 */
	private boolean movePlayer() {
		List<Element> eventList = replayElement.getChild("events").getChildren("events");
		Optional<Element> event = eventList.stream()
										   .filter((e)->(Integer.parseInt(e.getAttribute("time").getValue()) == time))
										   .filter((e)->e.getChildText("actor").equals("player"))
										   .findFirst();
		if (event.isEmpty())return false;
		Direction direction = Direction.valueOf(event.get().getChildText("action"));
		movePlayer.accept(direction);
		time++;
		return true;
	}
	
	private int findEndTime() {
		List<Element> eventList = replayElement.getChild("events").getChildren("events");
		return Integer.parseInt(eventList.get(eventList.size()-1).getAttributeValue("time"));
	}
}
