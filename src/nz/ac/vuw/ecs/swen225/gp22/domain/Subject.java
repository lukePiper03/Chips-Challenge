package nz.ac.vuw.ecs.swen225.gp22.domain;

import java.util.Optional;

/**
 * @author Linda Zhang 300570498
 * Classes that extend this can be subscribed to by others!
 * Others can listen to onChange() to do whatever they want.
 */
public abstract class Subject{
	/**
	 * the Optional Observer in the subject for Observer Pattern
	 */
	public Optional<Observer> ob=Optional.empty();
	/**
	 * update the observer
	 */
	public void onChange(){ob.ifPresent(o->o.update());}
	 /**
	 * @param o set observer
	 */
	public void attach(Observer o){ob=Optional.of(o);}
	 /**
	 * remove observer
	 */
	public void detatch(){ob = Optional.empty();}
}