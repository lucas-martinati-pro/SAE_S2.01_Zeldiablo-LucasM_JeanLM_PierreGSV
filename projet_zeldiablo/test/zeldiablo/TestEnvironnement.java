package zeldiablo;

import zeldiablo.entite.Spider;
import zeldiablo.environnement.*;
import zeldiablo.entite.Aventurier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironnement {

    // ########## Tests Piege ##########

    @Test
    public void test_Piege_creation() {
        Piege p = new Piege(3, 4);
        assertEquals("Piege", p.getType());
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());
        assertFalse(p.getIsRevele());
    }

    @Test
    public void test_Piege_effetInfligeDegatEtRevele() {
        Piege p = new Piege(1, 1);
        Aventurier a = new Aventurier(1, 1, 5);
        p.effet(a);
        assertEquals(4, a.getVie());
        assertTrue(p.getIsRevele());
    }

    @Test
    public void test_Piege_effetSurMonstre() {
        Piege p = new Piege(1, 1);
        Spider m = new Spider(1, 1, 3);
        p.effet(m);
        assertEquals(2, m.getVie());
        assertTrue(p.getIsRevele());
    }

    @Test
    public void test_Piege_coordNegatives() {
        Piege p = new Piege(-1, -2);
        assertEquals(0, p.getX());
    }

    // ########## Tests MurFriable ##########

    @Test
    public void test_MurFriable_creation() {
        MurFriable mf = new MurFriable(2, 3);
        assertEquals("MurFriable", mf.getType());
        assertEquals(2, mf.getX());
        assertEquals(3, mf.getY());
    }

    @Test
    public void test_MurFriable_coordNegatives() {
        MurFriable mf = new MurFriable(-5, -3);
        assertEquals(0, mf.getX());
    }

    // ########## Tests Bombe ##########

    @Test
    public void test_Bombe_creation() {
        Bombe b = new Bombe(4, 5);
        assertEquals("Bombe", b.getType());
        assertEquals(4, b.getX());
        assertEquals(5, b.getY());
    }

    @Test
    public void test_Bombe_coordNegatives() {
        Bombe b = new Bombe(-1, -2);
        assertEquals(0, b.getX());
        assertEquals(0, b.getY());
    }

    @Test
    public void test_Bombe_setJeuNull() {
        Bombe b = new Bombe(1, 1);
        b.setJeu(null); // ne doit pas crasher, crée un jeu vide
        assertNotNull(b);
    }
}
