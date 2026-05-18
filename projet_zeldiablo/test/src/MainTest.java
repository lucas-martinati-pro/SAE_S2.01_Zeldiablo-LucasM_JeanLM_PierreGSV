package src;

import main.Main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void testRetour() {
        Main m = new Main();
        int val = m.retour();
        assertEquals(1, val, "devrait être égal à 1");
    }
}