package zeldiablo;

import moteurJeu.Commande;
import zeldiablo.entite.Spider;
import zeldiablo.environnement.*;
import zeldiablo.exception.ActionInconnueException;
import zeldiablo.entite.Aventurier;
import zeldiablo.exception.FichierIncorrectException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Jeu.
 */
public class TestJeu {

    private Jeu jeu;

    @BeforeEach
    public void beforeEach() throws FichierIncorrectException, IOException {
        jeu = new Jeu();
        jeu.chargerNiveau("laby/laby.txt");
    }

    // ########## Tests getters/setters ##########

    @Test
    public void test_getHero_retournePerso() {
        assertNotNull(jeu.getHero());
    }

    @Test
    public void test_getSize_retourneTaille() {
        assertNotNull(jeu.getSize());
    }

    @Test
    public void test_setPerso_modifiePerso() {
        Aventurier p = new Aventurier(2, 2);
        jeu.setHero(p);
        assertEquals(p, jeu.getHero());
    }

    @Test
    public void test_setSize_modifieTaille() {
        int[] size = new int[] { 5, 5 };
        jeu.setSize(size);
        assertEquals(size, jeu.getSize());
    }

    @Test
    public void test_getCases_retourneListe() {
        assertNotNull(jeu.getCases());
    }

    @Test
    public void test_getMonstres_retourneListe() {
        assertNotNull(jeu.getMonstres());
    }

    @Test
    public void test_getCase_existante() {
        Piege p = new Piege(5, 5);
        jeu.addCase(p);
        assertEquals(p, jeu.getCase(5, 5));
    }

    @Test
    public void test_getCase_inexistante() {
        assertNull(jeu.getCase(99, 99));
    }

    // ########## Tests evoluer - deplacement valide ##########

    @Test
    public void test_evoluer_gauche() throws ActionInconnueException, IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int xAvant = jeu.getHero().getX();
        Commande c = new Commande();
        c.gauche = true;
        jeu.evoluer(c);
        assertEquals(xAvant - 1, jeu.getHero().getX());
    }

    @Test
    public void test_evoluer_droite() throws ActionInconnueException, IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int xAvant = jeu.getHero().getX();
        Commande c = new Commande();
        c.droite = true;
        jeu.evoluer(c);
        assertEquals(xAvant + 1, jeu.getHero().getX());
    }

    @Test
    public void test_evoluer_haut() throws ActionInconnueException, IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        assertEquals(yAvant - 1, jeu.getHero().getY());
    }

    @Test
    public void test_evoluer_bas() throws ActionInconnueException, IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.bas = true;
        jeu.evoluer(c);
        assertEquals(yAvant + 1, jeu.getHero().getY());
    }

    @Test
    public void test_evoluer_allerRetour() throws ActionInconnueException, IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int xInit = jeu.getHero().getX();
        int yInit = jeu.getHero().getY();
        Commande c = new Commande();
        c.gauche = true;
        jeu.evoluer(c);
        c.gauche = false;
        c.droite = true;
        jeu.evoluer(c);
        assertEquals(xInit, jeu.getHero().getX());
        assertEquals(yInit, jeu.getHero().getY());
    }

    // ########## Tests evoluer - deplacement invalide ##########

    @Test
    public void test_evoluer_dansMur_positionInchangee() {
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.droite = true;
        jeu.evoluer(c);
        assertEquals(xAvant, jeu.getHero().getX());
        assertEquals(yAvant, jeu.getHero().getY());
    }

    // ########## Tests chargerJeu ##########

    @Test
    public void test_chargerJeu_labySimple_ok() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        assertNotNull(jeu.getHero());
        assertNotNull(jeu.getSize());
    }

    @Test
    public void test_chargerJeu_positionPerso() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        assertEquals(2, jeu.getHero().getX());
        assertEquals(4, jeu.getHero().getY());
    }

    @Test
    public void test_chargerJeu_caseVidePasMur() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        assertFalse(jeu.getCase(2, 2) instanceof Mur);
    }

    @Test
    public void test_chargerJeu_fichierInexistant() {
        assertThrows(FileNotFoundException.class, () -> jeu.chargerNiveau("laby/inexistant.txt"));
    }

    @Test
    public void test_chargerJeu_caractereInconnu() {
        assertThrows(FichierIncorrectException.class, () -> jeu.chargerNiveau("laby/laby_invalie_carac.txt"));
    }

    @Test
    public void test_chargerJeu_sansPersonnage() {
        assertThrows(FichierIncorrectException.class, () -> jeu.chargerNiveau("laby/laby_sans_perso.txt"));
    }

    @Test
    public void test_chargerJeu_avecMonstres() throws IOException {
        jeu.chargerNiveau("laby/laby_test_monstre.txt");
        assertFalse(jeu.getMonstres().isEmpty());
    }

    @Test
    public void test_chargerJeu_avecPieges() throws IOException {
        jeu.chargerNiveau("laby/laby_test_monstre.txt");
        boolean hasPiege = java.util.Arrays.stream(jeu.getCases())
                .flatMap(java.util.Arrays::stream)
                .filter(java.util.Objects::nonNull)
                .anyMatch(c -> c instanceof Piege);
        assertTrue(hasPiege);
    }

    @Test
    public void test_chargerJeu_avecMurFriable() throws IOException {
        jeu.chargerNiveau("laby/laby_test_monstre.txt");
        boolean hasMur = java.util.Arrays.stream(jeu.getCases())
                .flatMap(java.util.Arrays::stream)
                .filter(java.util.Objects::nonNull)
                .anyMatch(c -> c instanceof MurFriable);
        assertTrue(hasMur);
    }

    // ########## Tests detruire ##########

    @Test
    public void test_detruire_retireCase() {
        MurFriable m = new MurFriable(5, 5);
        jeu.addCase(m);
        assertNotNull(jeu.getCase(5, 5));
        jeu.detruire(5, 5);
        assertNull(jeu.getCase(5, 5));
    }

    @Test
    public void test_detruire_remplacePiegeParPiegeDetruit() {
        Piege p = new Piege(5, 5);
        jeu.addCase(p);
        assertNotNull(jeu.getCase(5, 5));
        jeu.detruire(5, 5);
        assertTrue(jeu.getCase(5, 5) instanceof PiegeDetruit);
    }

    @Test
    public void test_bombeSurPiegeDetruit_preservePiegeDetruit() throws InterruptedException {
        PiegeDetruit pd = new PiegeDetruit(5, 5);
        jeu.addCase(pd);
        Bombe b = new Bombe(5, 5);
        jeu.addCase(b);
        assertEquals(b, jeu.getCase(5, 5));
        b.exploser(jeu);
        Thread.sleep(1600);
        assertTrue(jeu.getCase(5, 5) instanceof PiegeDetruit);
    }

    // ########## Tests addBombe ##########

    @Test
    public void test_addBombe_ajouteBombe() {
        jeu.addCase(new Bombe(3, 3));
        Case c = jeu.getCase(3, 3);
        assertNotNull(c);
        assertTrue(c instanceof Bombe);
    }

    // ########## Tests verifierDeplacement ##########

    @Test
    public void test_verifierDeplacement_caseLibre() {
        Commande c = new Commande();
        c.droite = true;
        assertDoesNotThrow(() -> jeu.verifierDeplacement(1, 1, c));
    }

    @Test
    public void test_verifierDeplacement_mur() {
        Commande c = new Commande();
        c.droite = true;
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(0, 0, c));
    }

    @Test
    public void test_verifierDeplacement_horsLimites() {
        Commande c = new Commande();
        c.droite = true;
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(-1, -1, c));
    }

    @Test
    public void test_verifierDeplacement_horsLimitesGrandes() {
        Commande c = new Commande();
        c.droite = true;
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(100, 100, c));
    }

    @Test
    public void test_verifierDeplacement_commande_caseLibre() {
        Commande c = new Commande();
        c.droite = true;
        assertDoesNotThrow(() -> jeu.verifierDeplacement(1, 1, c));
    }

    @Test
    public void test_verifierDeplacement_commande_mur() {
        Commande c = new Commande();
        c.droite = true;
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(0, 0, c));
    }

    // ########## Tests getSuivant ##########

    @Test
    public void test_getSuivant_haut() {
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.haut = true;
        int[] res = jeu.getSuivant(5, 5, c);
        assertEquals(5, res[0]);
        assertEquals(4, res[1]);
    }

    @Test
    public void test_getSuivant_bas() {
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.bas = true;
        int[] res = jeu.getSuivant(5, 5, c);
        assertEquals(5, res[0]);
        assertEquals(6, res[1]);
    }

    @Test
    public void test_getSuivant_gauche() {
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.gauche = true;
        int[] res = jeu.getSuivant(5, 5, c);
        assertEquals(4, res[0]);
        assertEquals(5, res[1]);
    }

    @Test
    public void test_getSuivant_droite() {
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.droite = true;
        int[] res = jeu.getSuivant(5, 5, c);
        assertEquals(6, res[0]);
        assertEquals(5, res[1]);
    }

    // ########## Tests etreFini ##########

    @Test
    public void test_etreFini_debutFaux() {
        assertFalse(jeu.etreFini());
    }

    @Test
    public void test_etreFini_heroSurFin() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        jeu.evoluer(c);
        assertTrue(jeu.etreFini());
    }

    @Test
    public void test_etreFini_heroMort() {
        Aventurier hero = new Aventurier(1, 1);
        hero.setVie(0);
        jeu.setHero(hero);
        assertTrue(jeu.etreFini());
    }

    @Test
    public void test_etreFini_heroVivant_pasSurFin() {
        Commande c = new Commande();
        c.droite = true;
        jeu.evoluer(c);
        assertFalse(jeu.etreFini());
    }
    
    // ########## Tests verifMort ##########

    @Test
    public void test_verifMort_monstreMortRetire() {
        Spider m = new Spider(1, 1);
        m.setVie(0);
        jeu.getMonstres().add(m);
        jeu.getGestionnaireMonstres().verifMort();
        assertFalse(jeu.getMonstres().contains(m));
    }

    @Test
    public void test_verifMort_monstreVivantReste() {
        Spider m = new Spider(1, 1);
        jeu.getMonstres().add(m);
        jeu.getGestionnaireMonstres().verifMort();
        assertTrue(jeu.getMonstres().contains(m));
    }

    // ########## Tests evoluerMonster ##########

    @Test
    public void test_evoluerMonster_deplacement() {
        Spider m = new Spider(1, 1);
        jeu.getMonstres().add(m);
        Commande c = new Commande();
        c.droite = true;
        m.deplacer(jeu, c);
        assertEquals(2, m.getX());
        assertEquals(1, m.getY());
    }

    // ########## Tests hero marche sur piege ##########

    @Test
    public void test_heroMarcheSurPiege() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        Piege piege = new Piege(x, y - 1);
        jeu.addCase(piege);
        int vieAvant = jeu.getHero().getVie();
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        assertEquals(vieAvant - 1, jeu.getHero().getVie());
        assertTrue(piege.getIsRevele());
    }

    // ########## Tests hero bloque par mur friable ##########

    @Test
    public void test_heroBloqueParMurFriable() throws IOException {
        jeu.chargerNiveau("laby/laby_simple.txt");
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        jeu.addCase(new MurFriable(x, y - 1));
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        assertEquals(x, jeu.getHero().getX());
        assertEquals(y, jeu.getHero().getY());
    }

    // ########## Tests aventurier pose bombe ##########

    @Test
    public void test_aventurierPoseBombe() {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        assertNull(jeu.getCase(x, y));
        jeu.getHero().attaquer(new Spider(0, 0)); // L'attaque de l'aventurier pose une bombe sur sa position
        Case bombe = jeu.getCase(x, y);
        assertNotNull(bombe);
        assertTrue(bombe instanceof Bombe);
    }

    // ########## Tests bombe explosion ##########

    @Test
    public void test_bombeExplosionDegatsHero() throws InterruptedException {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        int vieAvant = jeu.getHero().getVie();
        jeu.addCase(new Bombe(x, y));
        Bombe bombe = (Bombe) jeu.getCase(x, y);
        bombe.exploser(jeu);
        Thread.sleep(1600);
        assertEquals(vieAvant - 5, jeu.getHero().getVie());
    }

    @Test
    public void test_bombeExplosionDetruitMurFriable() throws InterruptedException {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        MurFriable mur = new MurFriable(x, y - 1);
        jeu.addCase(mur);
        jeu.addCase(new Bombe(x, y - 2));
        Bombe bombe = (Bombe) jeu.getCase(x, y - 2);
        bombe.exploser(jeu);
        Thread.sleep(1600);
        assertNull(jeu.getCase(x, y - 1));
    }

    @Test
    public void test_bombeExplosionTueMonstre() throws InterruptedException {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        Spider m = new Spider(x, y - 6);
        jeu.getMonstres().add(m);
        jeu.addCase(new Bombe(x, y - 5));
        Bombe bombe = (Bombe) jeu.getCase(x, y - 5);
        bombe.exploser(jeu);
        Thread.sleep(1600);
        assertFalse(jeu.getMonstres().contains(m));
    }
}
