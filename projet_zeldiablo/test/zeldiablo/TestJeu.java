package zeldiablo;

import moteurJeu.Commande;
import zeldiablo.environnement.*;
import zeldiablo.exception.ActionInconnueException;
import zeldiablo.entite.Aventurier;
import zeldiablo.exception.FichierIncorrectException;
import zeldiablo.entite.Monstre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Jeu.
 */
public class TestJeu {

    private Jeu jeu;

    @BeforeEach
    public void beforeEach() throws FichierIncorrectException, IOException {
        jeu = new Jeu();
        Chargement.chargerNiveau(jeu, "laby/laby.txt");
    }

    // ########## Tests getters/setters ##########

    @Test
    public void test_getHero_retournePerso() {
        assertNotNull(jeu.getHero());
    }

    @Test
    public void test_getLaby_retourneLabyrinthe() {
        assertNotNull(jeu.getLaby());
    }

    @Test
    public void test_setPerso_modifiePerso() {
        Aventurier p = new Aventurier(2, 2, 3);
        jeu.setHero(p);
        assertEquals(p, jeu.getHero());
    }

    @Test
    public void test_setLaby_modifieLabyrinthe() {
        Labyrinthe lab = new Labyrinthe(5, 5);
        jeu.setLaby(lab);
        assertEquals(lab, jeu.getLaby());
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
        jeu.getCases().add(p);
        assertEquals(p, jeu.getCase(5, 5));
    }

    @Test
    public void test_getCase_inexistante() {
        assertNull(jeu.getCase(99, 99));
    }

    // ########## Tests evoluer - deplacement valide ##########

    @Test
    public void test_evoluer_gauche() throws ActionInconnueException, IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int xAvant = jeu.getHero().getX();
        Commande c = new Commande();
        c.gauche = true;
        jeu.evoluer(c);
        assertEquals(xAvant - 1, jeu.getHero().getX());
    }

    @Test
    public void test_evoluer_droite() throws ActionInconnueException, IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int xAvant = jeu.getHero().getX();
        Commande c = new Commande();
        c.droite = true;
        jeu.evoluer(c);
        assertEquals(xAvant + 1, jeu.getHero().getX());
    }

    @Test
    public void test_evoluer_haut() throws ActionInconnueException, IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        assertEquals(yAvant - 1, jeu.getHero().getY());
    }

    @Test
    public void test_evoluer_bas() throws ActionInconnueException, IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.bas = true;
        jeu.evoluer(c);
        assertEquals(yAvant + 1, jeu.getHero().getY());
    }

    @Test
    public void test_evoluer_allerRetour() throws ActionInconnueException, IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
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

    @Test
    public void test_evoluer_actionInconnue_leveException() {
        assertThrows(ActionInconnueException.class, () -> jeu.evoluer(new Commande()));
    }

    // ########## Tests evoluer - deplacement invalide ##########

    @Test
    public void test_evoluer_dansMur_positionInchangee() {
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        Commande c = new Commande();
        c.droite = true;
        jeu.evoluer(c);
        jeu.evoluer(c); // mur
        assertEquals(xAvant + 1, jeu.getHero().getX());
        assertEquals(yAvant, jeu.getHero().getY());
    }

    // ########## Tests chargerJeu ##########

    @Test
    public void test_chargerJeu_labySimple_ok() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        assertNotNull(jeu.getHero());
        assertNotNull(jeu.getLaby());
    }

    @Test
    public void test_chargerJeu_positionPerso() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        assertEquals(2, jeu.getHero().getX());
        assertEquals(4, jeu.getHero().getY());
    }

    @Test
    public void test_chargerJeu_caseVidePasMur() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        assertFalse(jeu.getLaby().getCase(2, 2));
    }

    @Test
    public void test_chargerJeu_fichierInexistant() {
        assertThrows(FileNotFoundException.class, () -> Chargement.chargerNiveau(jeu, "laby/inexistant.txt"));
    }

    @Test
    public void test_chargerJeu_caractereInconnu() {
        assertThrows(FichierIncorrectException.class, () -> Chargement.chargerNiveau(jeu, "laby/laby_invalie_carac.txt"));
    }

    @Test
    public void test_chargerJeu_sansPersonnage() {
        assertThrows(FichierIncorrectException.class, () -> Chargement.chargerNiveau(jeu, "laby/laby_sans_perso.txt"));
    }

    @Test
    public void test_chargerJeu_avecMonstres() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_test_monstre.txt");
        assertFalse(jeu.getMonstres().isEmpty());
    }

    @Test
    public void test_chargerJeu_avecPieges() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_test_monstre.txt");
        boolean hasPiege = jeu.getCases().stream().anyMatch(c -> c instanceof Piege);
        assertTrue(hasPiege);
    }

    @Test
    public void test_chargerJeu_avecMurFriable() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_test_monstre.txt");
        boolean hasMur = jeu.getCases().stream().anyMatch(c -> c instanceof MurFriable);
        assertTrue(hasMur);
    }

    // ########## Tests convertLab ##########

    @Test
    public void test_convertLab_retourneLabyrinthe() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        Labyrinthe lab = Chargement.convertLab("laby/laby_simple.txt", lignes);
        assertNotNull(lab);
    }

    @Test
    public void test_convertLab_nombreLignes() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        Chargement.convertLab("laby/laby_simple.txt", lignes);
        assertEquals(7, lignes.size());
    }

    @Test
    public void test_convertLab_dimensions() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        Labyrinthe lab = Chargement.convertLab("laby/laby_simple.txt", lignes);
        int[] taille = lab.returnSize();
        assertEquals(5, taille[0]);
        assertEquals(7, taille[1]);
    }

    @Test
    public void test_convertLab_fichierInexistant() {
        assertThrows(FileNotFoundException.class, () -> Chargement.convertLab("laby/inexistant.txt", new ArrayList<>()));
    }

    // ########## Tests getChar ##########

    @Test
    public void test_getChar_mur() {
        assertEquals(Labyrinthe.MUR, jeu.getChar(0, 0));
    }

    @Test
    public void test_getChar_hero() {
        assertEquals(Labyrinthe.HERO, jeu.getChar(jeu.getHero().getX(), jeu.getHero().getY()));
    }

    @Test
    public void test_getChar_vide() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        assertEquals(Labyrinthe.VIDE, jeu.getChar(1, 1));
    }

    @Test
    public void test_getChar_piege() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        jeu.getCases().add(new Piege(1, 1));
        assertEquals(Labyrinthe.PIEGE, jeu.getChar(1, 1));
    }

    @Test
    public void test_getChar_murFriable() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        jeu.getCases().add(new MurFriable(1, 1));
        assertEquals(Labyrinthe.MurFriable, jeu.getChar(1, 1));
    }

    @Test
    public void test_getChar_bombe() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        jeu.addBombe(1, 1);
        assertEquals(Labyrinthe.BOMBE, jeu.getChar(1, 1));
    }

    @Test
    public void test_getChar_monstre() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        jeu.getMonstres().add(new Monstre(1, 1, 3));
        assertEquals(Labyrinthe.MONSTRE, jeu.getChar(1, 1));
    }

    // ########## Tests detruire ##########

    @Test
    public void test_detruire_retireCase() {
        Piege p = new Piege(5, 5);
        jeu.getCases().add(p);
        assertNotNull(jeu.getCase(5, 5));
        jeu.detruire(5, 5);
        assertNull(jeu.getCase(5, 5));
    }

    // ########## Tests addBombe ##########

    @Test
    public void test_addBombe_ajouteBombe() {
        jeu.addBombe(3, 3);
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
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        Commande c = new Commande();
        c.haut = true;
        jeu.evoluer(c);
        jeu.evoluer(c);
        assertTrue(jeu.etreFini());
    }

    @Test
    public void test_etreFini_heroMort() {
        Aventurier hero = new Aventurier(1, 1, 0);
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

    // ########## Tests jeuToString ##########

    @Test
    public void test_jeuToString_nonVide() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        assertNotNull(jeu.jeuToString());
        assertFalse(jeu.jeuToString().isEmpty());
    }

    @Test
    public void test_jeuToString_contientPerso() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        String s = jeu.jeuToString();
        assertEquals("#####\n#   #\n# & #\n#   #\n# @ #\n#   #\n#####\n", s);
    }

    // ########## Tests monstreAttaque ##########

    @Test
    public void test_monstreAttaque_heroProche() {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        Monstre m = new Monstre(x + 1, y, 3);
        jeu.getMonstres().add(m);
        int vieAvant = jeu.getHero().getVie();
        jeu.getGestionnaireMonstres().monstreAttaque(x, y);
        assertEquals(vieAvant - 2, jeu.getHero().getVie());
    }

    @Test
    public void test_monstreAttaque_heroLoin_pasDeDegat() {
        Monstre m = new Monstre(1, 1, 3);
        jeu.getMonstres().add(m);
        int vieAvant = jeu.getHero().getVie();
        jeu.getGestionnaireMonstres().monstreAttaque(jeu.getHero().getX(), jeu.getHero().getY());
        assertEquals(vieAvant, jeu.getHero().getVie());
    }

    // ########## Tests verifMort ##########

    @Test
    public void test_verifMort_monstreMortRetire() {
        Monstre m = new Monstre(1, 1, 0);
        jeu.getMonstres().add(m);
        jeu.getGestionnaireMonstres().verifMort();
        assertFalse(jeu.getMonstres().contains(m));
    }

    @Test
    public void test_verifMort_monstreVivantReste() {
        Monstre m = new Monstre(1, 1, 3);
        jeu.getMonstres().add(m);
        jeu.getGestionnaireMonstres().verifMort();
        assertTrue(jeu.getMonstres().contains(m));
    }

    // ########## Tests evoluerMonster ##########

    @Test
    public void test_evoluerMonster_deplacement() {
        Monstre m = new Monstre(1, 1, 3);
        jeu.getMonstres().add(m);
        moteurJeu.Commande c = new moteurJeu.Commande();
        c.droite = true;
        jeu.getGestionnaireMonstres().evoluerMonster(c);
        assertEquals(2, m.getX());
        assertEquals(1, m.getY());
    }

    // ########## Tests hero marche sur piege ##########

    @Test
    public void test_heroMarcheSurPiege() throws IOException {
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        Piege piege = new Piege(x, y - 1);
        jeu.getCases().add(piege);
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
        Chargement.chargerNiveau(jeu, "laby/laby_simple.txt");
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        jeu.getCases().add(new MurFriable(x, y - 1));
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
        jeu.getHero().attaquer(jeu);
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
        jeu.addBombe(x, y);
        Case bombe = jeu.getCase(x, y);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(jeu.getHero());
        Thread.sleep(1600);
        assertEquals(vieAvant - 5, jeu.getHero().getVie());
    }

    @Test
    public void test_bombeExplosionDetruitMurFriable() throws InterruptedException {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        MurFriable mur = new MurFriable(x + 1, y);
        jeu.getCases().add(mur);
        jeu.addBombe(x, y);
        Case bombe = jeu.getCase(x, y);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(jeu.getHero());
        Thread.sleep(1600);
        assertNull(jeu.getCase(x + 1, y));
    }

    @Test
    public void test_bombeExplosionTueMonstre() throws InterruptedException {
        int x = jeu.getHero().getX();
        int y = jeu.getHero().getY();
        Monstre m = new Monstre(x + 1, y, 3);
        jeu.getMonstres().add(m);
        jeu.addBombe(x, y);
        Case bombe = jeu.getCase(x, y);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(jeu.getHero());
        Thread.sleep(1600);
        assertFalse(jeu.getMonstres().contains(m));
    }
}
