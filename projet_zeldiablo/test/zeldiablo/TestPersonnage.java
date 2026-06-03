package zeldiablo;

import zeldiablo.entite.Aventurier;
import zeldiablo.entite.Spider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPersonnage {
    // ########## Tests Aventurier ##########

    @Test
    public void test_Aventurier_creation() {
        Aventurier a = new Aventurier(3, 4, 5);
        assertEquals(3, a.getX());
        assertEquals(4, a.getY());
        assertEquals(5, a.getVie());
    }

    @Test
    public void test_Aventurier_creationVieNegative() {
        Aventurier a = new Aventurier(1, 1, -3);
        assertTrue(a.etreMort());
    }

    @Test
    public void test_Aventurier_setPos() {
        Aventurier a = new Aventurier(1, 1, 5);
        a.setPos(5, 7);
        assertEquals(5, a.getX());
        assertEquals(7, a.getY());
    }

    @Test
    public void test_Aventurier_setPosNegatif() {
        Aventurier a = new Aventurier(1, 1, 5);
        a.setPos(-3, -2);
        assertEquals(0, a.getX());
        assertEquals(0, a.getY());
    }

    @Test
    public void test_Aventurier_addVie() {
        Aventurier a = new Aventurier(1, 1, 5);
        a.addVie(3);
        assertEquals(8, a.getVie());
    }

    @Test
    public void test_Aventurier_addVieNegatif() {
        Aventurier a = new Aventurier(1, 1, 5);
        a.addVie(-2);
        assertEquals(3, a.getVie());
    }

    @Test
    public void test_Aventurier_etreMortFaux() {
        Aventurier a = new Aventurier(1, 1, 5);
        assertFalse(a.etreMort());
    }

    @Test
    public void test_Aventurier_etreMortVrai() {
        Aventurier a = new Aventurier(1, 1, 0);
        assertTrue(a.etreMort());
    }

    @Test
    public void test_Aventurier_etreMortApresDegatLetaux() {
        Aventurier a = new Aventurier(1, 1, 1);
        a.addVie(-5);
        assertTrue(a.etreMort());
    }

    // ########## Tests Spider ##########

    @Test
    public void test_Monstre_creation() {
        Spider m = new Spider(2, 3, 3);
        assertEquals(2, m.getX());
        assertEquals(3, m.getY());
        assertEquals(3, m.getVie());
    }

    @Test
    public void test_Monstre_setPos() {
        Spider m = new Spider(1, 1, 3);
        m.setPos(4, 5);
        assertEquals(4, m.getX());
        assertEquals(5, m.getY());
    }

    // ########## Tests attaquer ##########

    @Test
    public void test_attaquer_infliceDegats() {
        Spider m = new Spider(1, 1, 3);
        Aventurier a = new Aventurier(2, 2, 5);
        m.attaquer(a);
        assertEquals(3, a.getVie());
    }

    @Test
    public void test_attaquer_mortNeAttaquePas() {
        Spider m = new Spider(1, 1, 0);
        Aventurier a = new Aventurier(2, 2, 5);
        m.attaquer(a);
        assertEquals(5, a.getVie());
    }

    @Test
    public void test_attaquer_aventurierContreMontsre() {
        Aventurier a = new Aventurier(1, 1, 5);
        Spider m = new Spider(2, 2, 3);
        a.attaquer(m);
        assertEquals(1, m.getVie());
    }

    @Test
    public void test_attaquer_tueMonstre() {
        Aventurier a = new Aventurier(1, 1, 5);
        Spider m = new Spider(2, 2, 2);
        a.attaquer(m);
        assertTrue(m.etreMort());
    }
}
