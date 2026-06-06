package zeldiablo;

import zeldiablo.entite.Spider;
import zeldiablo.environnement.*;
import zeldiablo.entite.Aventurier;
import org.junit.jupiter.api.Test;
import moteurJeu.Commande;
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
        Aventurier a = new Aventurier(1, 1);
        p.effet(new Jeu(), a);
        assertEquals(5, a.getVie());
        assertTrue(p.getIsRevele());
    }

    @Test
    public void test_Piege_effetSurMonstre() {
        Piege p = new Piege(1, 1);
        Spider m = new Spider(1, 1);
        p.effet(new Jeu(), m);
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

    // ########## Tests Teleporteur ##########

    @Test
    public void test_Teleporteur_creation() {
        Teleporteur t = new Teleporteur(5, 6);
        assertEquals("Teleporteur", t.getType());
        assertEquals(5, t.getX());
        assertEquals(6, t.getY());
    }

    @Test
    public void test_Teleporteur_effet() {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1);
        a.setJeu(jeu);
        jeu.setHero(a);
        jeu.setSize(new int[] { 10, 10 });

        Teleporteur t1 = new Teleporteur(2, 1);
        Teleporteur t2 = new Teleporteur(5, 5);

        jeu.addCase(t1);
        jeu.addCase(t2);

        Commande cmd = new Commande();
        cmd.droite = true;

        a.deplacer(jeu, cmd);

        assertEquals(2, a.getX());
        assertEquals(1, a.getY());
    }

    // ########## Tests Soins ##########

    /**
     * Teste la creation d'une case de Soins.
     */
    @Test
    public void test_Soins_creation() {
        Soins s = new Soins(3, 4);
        assertEquals("Soins", s.getType());
        assertEquals(3, s.getX());
        assertEquals(4, s.getY());
    }

    /**
     * Teste l'effet de soin sur un aventurier blesse et la destruction de la case.
     */
    @Test
    public void test_Soins_effetSoigneEtDetruit() {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1);
        Soins s = new Soins(1, 1);
        jeu.addCase(s);
        s.effet(jeu, a);
        assertEquals(8, a.getVie());
        // La case Soins doit etre remplacee par SoinsDetruit
        boolean hasSoinsDetruit = jeu.getCase(1, 1) instanceof SoinsDetruit;
        assertTrue(hasSoinsDetruit);
    }
}
