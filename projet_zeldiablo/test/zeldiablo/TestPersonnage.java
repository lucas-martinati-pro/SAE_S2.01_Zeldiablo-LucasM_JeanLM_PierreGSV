package zeldiablo;

import zeldiablo.entite.Aventurier;
import zeldiablo.entite.Spider;
import zeldiablo.entite.Blob;
import zeldiablo.entite.Artificier;
import zeldiablo.entite.Ghost;
import zeldiablo.entite.Troll;
import zeldiablo.environnement.Bombe;
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
    public void test_attaquer_aventurierContreMontsre() throws InterruptedException {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1, 5);
        a.setJeu(jeu);
        jeu.setHero(a);
        Spider m = new Spider(2, 1, 3);
        jeu.getMonstres().add(m);
        a.attaquer(m);
        Thread.sleep(1600);
        assertTrue(m.etreMort());
        assertFalse(jeu.getMonstres().contains(m));
    }

    @Test
    public void test_attaquer_tueMonstre() throws InterruptedException {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1, 5);
        a.setJeu(jeu);
        jeu.setHero(a);
        Spider m = new Spider(2, 1, 2);
        jeu.getMonstres().add(m);
        a.attaquer(m);
        Thread.sleep(1600);
        assertTrue(m.etreMort());
    }

    // ########## Tests Blob (Fonctionnalite 4) ##########

    /**
     * Teste la creation d'un Blob.
     */
    @Test
    public void test_Blob_creation() {
        Blob b = new Blob(3, 4, 2);
        assertEquals(3, b.getX());
        assertEquals(4, b.getY());
        assertEquals(2, b.getVie());
        assertEquals("Blob", b.getType());
    }

    /**
     * Teste que le Blob ne se deplace pas.
     */
    @Test
    public void test_Blob_neSeDeplacePas() {
        Jeu jeu = new Jeu();
        Blob b = new Blob(2, 2, 2);
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.haut = true;
        b.deplacer(jeu, c);
        assertEquals(2, b.getX());
        assertEquals(2, b.getY());
    }

    /**
     * Teste qu'une attaque detruit un segment du Blob.
     *
     * @throws InterruptedException si le sommeil du thread est interrompu
     */
    @Test
    public void test_Blob_destructionSegment() throws InterruptedException {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1, 5);
        a.setJeu(jeu);
        jeu.setHero(a);
        Blob b = new Blob(1, 2, 2);
        jeu.getMonstres().add(b);
        a.attaquer(b);
        Thread.sleep(1600);
        assertTrue(b.etreMort());
    }

    // ########## Tests Artificier (Fonctionnalite 3) ##########

    /**
     * Teste la creation d'un Artificier.
     */
    @Test
    public void test_Artificier_creation() {
        Artificier art = new Artificier(5, 5, 4);
        assertEquals(5, art.getX());
        assertEquals(5, art.getY());
        assertEquals(4, art.getVie());
        assertEquals("Artificier", art.getType());
    }

    /**
     * Teste que l'Artificier pose bien une bombe a sa position lors d'une attaque.
     */
    @Test
    public void test_Artificier_attaquePoseBombe() throws InterruptedException {
        Jeu jeu = new Jeu();
        Aventurier a = new Aventurier(1, 1, 5);
        a.setJeu(jeu);
        jeu.setHero(a);
        Artificier art = new Artificier(2, 2, 4);
        art.setJeu(jeu);
        jeu.getMonstres().add(art);
        art.attaquer(a);
        boolean hasBombe = java.util.Arrays.stream(jeu.getCases())
                .flatMap(java.util.Arrays::stream)
                .filter(java.util.Objects::nonNull)
                .anyMatch(c -> c instanceof Bombe && c.getX() == 2 && c.getY() == 2);
        assertTrue(hasBombe);
        Thread.sleep(1600); // laisser la bombe exploser pour eviter un crash en arriere-plan
    }

    // ########## Tests Affichage Visuel (Fonctionnalite 2) ##########

    /**
     * Teste la transition de l'etat d'attaque (devient temporairement true puis
     * false).
     *
     * @throws InterruptedException si le sommeil du thread est interrompu
     */
    @Test
    public void test_Personnage_isAttaque_transition() throws InterruptedException {
        Spider m = new Spider(1, 1, 3);
        Aventurier a = new Aventurier(2, 2, 5);
        assertFalse(m.getIsAttaque());
        m.attaquer(a);
        assertTrue(m.getIsAttaque());
        Thread.sleep(350);
        assertFalse(m.getIsAttaque());
    }

    // ########## Tests Troll ##########

    /**
     * Teste la creation d'un Troll.
     */
    @Test
    public void test_Troll_creation() {
        Troll t = new Troll(6, 6, 6);
        assertEquals(6, t.getX());
        assertEquals(6, t.getY());
        assertEquals(6, t.getVie());
        assertEquals("Troll", t.getType());
    }

    /**
     * Teste la regeneration de vie du Troll lorsqu'il est blesse.
     */
    @Test
    public void test_Troll_regenerer() {
        Troll t = new Troll(1, 1, 6);
        t.addVie(-2);
        assertEquals(4, t.getVie());
        t.regenerer();
        assertEquals(6, t.getVie());
    }

    // ########## Tests Ghost ##########

    /**
     * Teste la creation d'un Ghost.
     */
    @Test
    public void test_Ghost_creation() {
        Ghost g = new Ghost(8, 8, 3);
        assertEquals(8, g.getX());
        assertEquals(8, g.getY());
        assertEquals(3, g.getVie());
        assertEquals("Ghost", g.getType());
    }

    /**
     * Teste que le Ghost traverse les murs lors de ses deplacements.
     */
    @Test
    public void test_Ghost_deplacerTraverseMur() {
        Jeu jeu = new Jeu();
        jeu.addCase(new zeldiablo.environnement.Mur(1, 2), 1, 2);
        Ghost g = new Ghost(1, 1, 3);
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.bas = true;
        g.deplacer(jeu, c);
        assertEquals(1, g.getX());
        assertEquals(2, g.getY());
    }
}
