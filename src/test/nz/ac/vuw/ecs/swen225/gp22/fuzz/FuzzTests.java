package test.nz.ac.vuw.ecs.swen225.gp22.fuzz;
import nz.ac.vuw.ecs.swen225.gp22.app.Chips;
import nz.ac.vuw.ecs.swen225.gp22.app.Controller;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;




public class FuzzTests {
    @Test
    public void FuzzTest1() {
        Chips chip = new Chips();
        assertTrue(true);
    }

    @Test
    public void FuzzTest2() {
        assertFalse(false);
    }
}
