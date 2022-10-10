package nz.ac.vuw.ecs.swen225.gp22.renderer;

import java.util.ArrayList;
import java.util.List;
import nz.ac.vuw.ecs.swen225.gp22.domain.Level;
import nz.ac.vuw.ecs.swen225.gp22.domain.Observer;
import nz.ac.vuw.ecs.swen225.gp22.domain.Subject;
import nz.ac.vuw.ecs.swen225.gp22.renderer.sounds.Sound;


/**
 * Class that observes subjects in domain.
 *
 * @author Declan Cross 300567901
 */
public class EventHandler {
  // active listeners and sound player variables
  List<Subject> activeListeners = new ArrayList<>();
  SoundPlayer sounds;

  /**
   * Constructor.
   *
   * @param l level to get objects to observe
   * @param s SoundPlayer object to generate sound effects
   */
  public EventHandler(Level l, SoundPlayer s) {
    this.sounds = s;
    // add beep sound to all entity interactions
    l.getEntites().forEach(a -> {
      a.attach(() -> s.play(Sound.beep));
      activeListeners.add(a);
    });
    // walking sound effect for player
    // l.getPlayer().attach(()->s.play(Sound.beep));
    // activeListeners.add(l.getPlayer());

  }

  /**
   * Attach an listener observer to a subject.
   *
   * @param s subject
   * @param o observer with onChange lambda method
   */
  public void attach(Subject s, Observer o) {
    s.attach(o);
    activeListeners.add(s);
  }

  /**
   * Removes all observers for the level.
   */
  public void detatchAll() {
    activeListeners.forEach(a -> a.detatch());
    activeListeners.clear();
  }

}
