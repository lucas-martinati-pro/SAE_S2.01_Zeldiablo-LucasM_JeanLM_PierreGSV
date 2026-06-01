package zeldiablo;

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

    // @BeforeEarch permet que ce qui est contenu dans la méthode soit exécuté avant chaques tests
    /**
     * Charge le labyrinthe simple avant chaque test.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @BeforeEach
    public void beforeEach() throws FichierIncorrectException, IOException {
        jeu = new Jeu();
        jeu.chargerJeu("laby/laby.txt");
    }

    // ########## Tests getters/setters ##########

    /**
     * Verifie que getHero retourne un personnage non null.
     */
    @Test
    public void test_getHero_retournePerso() {
        assertNotNull(jeu.getHero());
    }

    /**
     * Verifie que getLaby retourne un labyrinthe non null.
     */
    @Test
    public void test_getLaby_retourneLabyrinthe() {
        assertNotNull(jeu.getLaby());
    }

    /**
     * Verifie que setPerso modifie le personnage.
     */
    @Test
    public void test_setPerso_modifiePerso() {
        Aventurier p = new Aventurier(2, 2, 3);
        jeu.setHero(p);
        assertEquals(p, jeu.getHero());
    }

    /**
     * Verifie que setLaby modifie le labyrinthe.
     */
    @Test
    public void test_setLaby_modifieLabyrinthe() {
        Labyrinthe lab = new Labyrinthe(5, 5);
        jeu.setLaby(lab);
        assertEquals(lab, jeu.getLaby());
    }

    // ########## Tests evoluer - deplacement valide ##########

    /**
     * Verifie le deplacement du personnage vers la gauche.
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_evoluer_gauche_positionChange() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec plus d'espace pour se deplacer
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        jeu.evoluer(Jeu.GAUCHE);
        assertEquals(xAvant - 1, jeu.getHero().getX());
        assertEquals(yAvant, jeu.getHero().getY());
    }

    /**
     * Verifie le deplacement du personnage vers la droite.
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_evoluer_droite_positionChange() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec plus d'espace pour se deplacer
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        jeu.evoluer(Jeu.DROITE);
        assertEquals(xAvant + 1, jeu.getHero().getX());
        assertEquals(yAvant, jeu.getHero().getY());
    }

    /**
     * Verifie le deplacement du personnage vers le haut (apres un deplacement gauche).
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_evoluer_haut_positionChange() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec plus d'espace pour se deplacer
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        jeu.evoluer(Jeu.HAUT);
        assertEquals(xAvant, jeu.getHero().getX());
        assertEquals(yAvant - 1, jeu.getHero().getY());
    }

    /**
     * Verifie qu'un aller-retour ramene le personnage a sa position initiale.
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_evoluer_deuxDeplacements() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec plus d'espace pour se deplacer
        int xInit = jeu.getHero().getX();
        int yInit = jeu.getHero().getY();
        jeu.evoluer(Jeu.GAUCHE);
        jeu.evoluer(Jeu.DROITE);
        // retour a la position initiale
        assertEquals(xInit, jeu.getHero().getX());
        assertEquals(yInit, jeu.getHero().getY());
    }

    /**
     * Verifie l'aller-retour gauche/droite (position initiale retrouvee).
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_evoluer_deuxDeplacementsGaucheDroite() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec plus d'espace pour se deplacer
        int xInit = jeu.getHero().getX();
        int yInit = jeu.getHero().getY();
        jeu.evoluer(Jeu.GAUCHE);
        jeu.evoluer(Jeu.DROITE);
        // retour a la position initiale
        assertEquals(xInit, jeu.getHero().getX());
        assertEquals(yInit, jeu.getHero().getY());
    }

    // ########## Tests du chargement du jeu ##########

    // ########## Tests chargerJeu - fichiers valides ##########

    /**
     * Verifie que chargerJeu retourne un jeu non null.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_labySimple_JeuExiste() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        assertNotNull(jeu);
    }

    /**
     * Verifie que le personnage est charge.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_labySimple_persoExiste() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        assertNotNull(jeu.getHero());
    }

    /**
     * Verifie que le labyrinthe est charge.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_labySimple_labyrintheExiste() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        assertNotNull(jeu.getLaby());
    }

    /**
     * Verifie qu'une case vide n'est pas un mur.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_labySimple_caseVideNestPasMur() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        // la case (3, 2) est le perso, pas un mur
        assertFalse(jeu.getLaby().getCase(3, 2));
    }

    /**
     * Verifie le chargement complet de laby.txt.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_laby_chargementCorrect() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby.txt");
        assertNotNull(jeu);
        assertNotNull(jeu.getHero());
        assertNotNull(jeu.getLaby());
    }

    /**
     * Verifie la position du personnage dans laby.txt.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_laby_positionPerso() throws FichierIncorrectException, IOException {
        // laby.txt : le @ est en colonne 17, ligne 18
        jeu.chargerJeu("laby/laby.txt");
        assertEquals(17, jeu.getHero().getX());
        assertEquals(18, jeu.getHero().getY());
    }

    /**
     * Verifie le chargement de laby_test.txt.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_labyTest_chargementCorrect() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_test.txt");
        assertNotNull(jeu);
        assertNotNull(jeu.getHero());
    }

    // ########## Tests chargerJeu - fichiers invalides ##########

    /**
     * Verifie qu'un fichier inexistant leve FileNotFoundException.
     */
    @Test
    public void test_chargerJeu_fichierInexistant_leveException() {
        assertThrows(FileNotFoundException.class, () -> jeu.chargerJeu("laby/fichier_inexistant.txt"));
    }

    /**
     * Verifie qu'un caractere inconnu leve FichierIncorrectException.
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_fichierCaractereInconnu_leveException() throws IOException {
        assertThrows(FichierIncorrectException.class, () -> jeu.chargerJeu("laby/laby_invalie_carac.txt"));
    }

    /**
     * Verifie qu'un fichier sans personnage leve FichierIncorrectException.
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_sansPersonnage_leveException() throws IOException {
        assertThrows(FichierIncorrectException.class, () -> jeu.chargerJeu("laby/laby_sans_perso.txt"));
    }

    // ########## Tests convertLab ##########

    /**
     * Verifie le nombre de lignes lues par convertLab.
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_convertLab_labySimple_nombreLignes() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        jeu.convertLab("laby/laby_simple.txt", lignes);
        // laby_simple.txt a 7 lignes de contenu
        assertTrue(lignes.size() == 7);
    }

    /**
     * Verifie que convertLab retourne un labyrinthe non null.
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_convertLab_labySimple_retourneLabyrinthe() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        Labyrinthe lab = jeu.convertLab("laby/laby_simple.txt", lignes);
        assertNotNull(lab);
    }

    /**
     * Verifie les dimensions du labyrinthe retourne par convertLab.
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_convertLab_labySimple_dimensionsCorrectes() throws IOException {
        ArrayList<String> lignes = new ArrayList<>();
        Labyrinthe lab = jeu.convertLab("laby/laby_simple.txt", lignes);
        int[] taille = lab.returnSize();
        // largeur max = 7, hauteur = nombre de lignes
        assertEquals(5, taille[0]);
        assertTrue(taille[1] >= 5);
    }

    /**
     * Verifie que convertLab leve FileNotFoundException pour un fichier inexistant.
     */
    @Test
    public void test_convertLab_fichierInexistant_leveException() {
        ArrayList<String> lignes = new ArrayList<>();
        assertThrows(FileNotFoundException.class, () -> jeu.convertLab("laby/inexistant.txt", lignes));
    }

    // ########## Tests jeuToString ##########

    /**
     * Verifie que jeuToString retourne une chaine non nulle.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_jeuToString_nonVide() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        String s = jeu.jeuToString();
        assertNotNull(s);
    }

    /**
     * Verifie que jeuToString retourne la representation attendue du labyrinthe.
     * @throws FichierIncorrectException si le fichier est incorrect
     * @throws IOException si erreur de lecture
     */
    @Test
    public void test_chargerJeu_jeuToString_contientPerso() throws FichierIncorrectException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt");
        String s = jeu.jeuToString();
        assertEquals("#####\n" +
                "#   #\n" +
                "# & #\n" +
                "#   #\n" +
                "# @ #\n" +
                "#   #\n" +
                "#####\n", s);
    }

    // ########## Tests evoluer - deplacement invalide (mur) ##########

    /**
     * Verifie que la position reste inchangee apres un deplacement dans un mur.
     */
    @Test
    public void test_evoluer_dansMur_positionInchangee() {
        // se deplacer vers un mur ne change pas la position
        int xAvant = jeu.getHero().getX();
        int yAvant = jeu.getHero().getY();
        try {
            // aller a droite 2 fois : la 1ere ok, la 2eme = mur
            jeu.evoluer(Jeu.DROITE);
            jeu.evoluer(Jeu.DROITE);
        } catch (ActionInconnueException e) {
            // position apres le 1er deplacement reussi
            assertEquals(xAvant + 1, jeu.getHero().getX());
            assertEquals(yAvant, jeu.getHero().getY());
        }
    }

    // ########## Tests verifierDeplacement ##########

    /**
     * Verifie qu'un deplacement vers une case libre ne leve pas d'exception.
     */
    @Test
    public void test_verifierDeplacement_caseLibre_pasException() {
        assertDoesNotThrow(() -> {
            jeu.verifierDeplacement(1, 1, Jeu.DROITE);
        });
    }

    /**
     * Verifie qu'un deplacement vers un mur leve ActionInconnueException.
     */
    @Test
    public void test_verifierDeplacement_mur_leveException() {
        // (0,0) est un mur
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(0, 0, Jeu.HAUT));
    }

    /**
     * Verifie qu'un deplacement hors limites (negatif) leve ActionInconnueException.
     */
    @Test
    public void test_verifierDeplacement_horsLimites_leveException() {
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(-1, -1, Jeu.HAUT));
    }

    /**
     * Verifie qu'un deplacement hors limites (grandes valeurs) leve ActionInconnueException.
     */
    @Test
    public void test_verifierDeplacement_horsLimitesGrandes_leveException() {
        assertThrows(ActionInconnueException.class, () -> jeu.verifierDeplacement(100, 100, Jeu.HAUT));
    }

    // ########## Tests etreFini ##########

    /**
     * Verifie que le jeu n'est pas fini au debut.
     */
    @Test
    public void test_etreFini_debutPartie_faux() {
        assertFalse(jeu.etreFini());
    }

    /**
     * Verifie que le jeu est fini lorsque le hero est sur la case de fin.
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_etreFini() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // laby avec un chemin plus simple pour atteindre la fin

        jeu.evoluer(Jeu.HAUT);
        jeu.evoluer(Jeu.HAUT);

        assertTrue(jeu.etreFini());
    }

    /**
     * Verifie que le jeu est fini lorsque le hero est sur la case de fin (autre chemin).
     * @throws ActionInconnueException si le deplacement est invalide
     */
    @Test
    public void test_etreFiniFaux() throws ActionInconnueException {
        jeu.evoluer(Jeu.DROITE);
        jeu.evoluer(Jeu.HAUT);

        assertFalse(jeu.etreFini());
    }

    @Test
    public  void test_Hero_mort(){
        Aventurier hero = new Aventurier(0,0,0);

        assertTrue(hero.etreMort());
    }

    @Test
    public void test_EtreFini_HeroMort (){
        Aventurier hero = new Aventurier(0,0,0);
        jeu.setHero(hero);

        assertTrue(jeu.etreFini());
    }

    @Test
    public void test_Bombe_getTypeEtGetCoord() {
        Bombe bombe = new Bombe(2, 3);
        assertEquals("Bombe", bombe.getType());
        assertArrayEquals(new int[]{2, 3}, bombe.getCoord());
    }

    @Test
    public void test_Bombe_effetExplosionDegatsHero() throws InterruptedException {
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();
        int vieHeroAvant = jeu.getHero().getVie();

        // Ajoute et initialise la bombe sur la case du héros
        jeu.addBombe(xHero, yHero);
        Case bombe = jeu.getCase(xHero, yHero);
        assertNotNull(bombe);
        assertTrue(bombe instanceof Bombe);

        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(jeu.getHero());

        // L'explosion doit se faire après 1 seconde (1000 ms)
        // On attend 1100 ms pour être sûr
        Thread.sleep(1100);

        // Les dégâts d'une bombe sont de -5
        assertEquals(vieHeroAvant - 5, jeu.getHero().getVie());
    }

    @Test
    public void test_Bombe_effetExplosionDetruitMurFriable() throws InterruptedException {
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Place un mur friable à côté (xHero + 1, yHero)
        int targetX = xHero + 1;
        int targetY = yHero;
        MurFriable mur = new MurFriable(targetX, targetY);
        jeu.getCases().add(mur);

        assertEquals(mur, jeu.getCase(targetX, targetY));

        // Place une bombe sur la case du héros
        jeu.addBombe(xHero, yHero);
        Case bombe = jeu.getCase(xHero, yHero);
        assertNotNull(bombe);
        ((Bombe) bombe).setJeu(jeu);

        // Déclenche l'explosion
        bombe.effet(jeu.getHero());

        // Attend l'explosion
        Thread.sleep(1100);

        // Le mur friable doit avoir été détruit et retiré des cases
        assertNull(jeu.getCase(targetX, targetY));
    }

    @Test
    public void test_Piege_getTypeEtGetCoord() {
        Piege piege = new Piege(4, 5);
        assertEquals("Piege", piege.getType());
        assertArrayEquals(new int[]{4, 5}, piege.getCoord());
        assertFalse(piege.getIsRevele());
    }

    @Test
    public void test_Piege_effetDegatsHero() {
        Piege piege = new Piege(jeu.getHero().getX(), jeu.getHero().getY());
        int vieAvant = jeu.getHero().getVie();
        piege.effet(jeu.getHero());

        assertEquals(vieAvant - 1, jeu.getHero().getVie());
        assertTrue(piege.getIsRevele());
    }

    @Test
    public void test_Jeu_heroMarcheSurPiege() throws ActionInconnueException, IOException {
        // Charge un labyrinthe vide
        jeu.chargerJeu("laby/laby_simple.txt"); // hero at (2, 4)
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Place un piège juste au-dessus du héros (2, 3)
        Piege piege = new Piege(xHero, yHero - 1);
        jeu.getCases().add(piege);

        int vieAvant = jeu.getHero().getVie();

        // Déplace le héros vers le haut (sur le piège)
        jeu.evoluer(Jeu.HAUT);

        // Vérifie que le héros a bougé
        assertEquals(xHero, jeu.getHero().getX());
        assertEquals(yHero - 1, jeu.getHero().getY());

        // Vérifie que les PV du héros ont diminué de 1
        assertEquals(vieAvant - 1, jeu.getHero().getVie());
        assertTrue(piege.getIsRevele());
    }

    @Test
    public void test_MurFriable_getTypeEtGetCoord() {
        MurFriable mur = new MurFriable(3, 3);
        assertEquals("MurFriable", mur.getType());
        assertArrayEquals(new int[]{3, 3}, mur.getCoord());
    }

    @Test
    public void test_Jeu_heroBloqueParMurFriable() throws ActionInconnueException, IOException {
        jeu.chargerJeu("laby/laby_simple.txt"); // hero at (2, 4)
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Place un mur friable au-dessus du héros (2, 3)
        MurFriable mur = new MurFriable(xHero, yHero - 1);
        jeu.getCases().add(mur);

        // Essaie de se déplacer vers le haut (sur le mur friable)
        jeu.evoluer(Jeu.HAUT);

        // Vérifie que le héros n'a pas bougé
        assertEquals(xHero, jeu.getHero().getX());
        assertEquals(yHero, jeu.getHero().getY());
    }

    @Test
    public void test_Monstre_attaquerHero() {
        Monstre monstre = new Monstre(1, 1, 3);
        int vieHeroAvant = jeu.getHero().getVie();
        monstre.attaquer(jeu.getHero());

        // Attaque de base inflige 2 dégâts
        assertEquals(vieHeroAvant - 2, jeu.getHero().getVie());
    }

    @Test
    public void test_Jeu_monstreAttaqueHeroQuandProche() {
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Place un monstre sur une case adjacente (xHero + 1, yHero)
        Monstre monstre = new Monstre(xHero + 1, yHero, 3);
        jeu.getMonstres().add(monstre);

        int vieHeroAvant = jeu.getHero().getVie();

        // Déclenche l'attaque automatique des monstres
        jeu.monstreAttaque(xHero, yHero);

        // Le héros doit avoir subi des dégâts
        assertEquals(vieHeroAvant - 2, jeu.getHero().getVie());
    }

    @Test
    public void test_Jeu_evoluerMonster() {
        // Place un monstre à une position libre (1, 1)
        Monstre monstre = new Monstre(1, 1, 3);
        jeu.getMonstres().add(monstre);

        moteurJeu.Commande commande = new moteurJeu.Commande();
        commande.droite = true; // Déplacement à droite

        // Déplace les monstres
        jeu.evoluerMonster(commande);

        // Vérifie que le monstre s'est déplacé à droite (2, 1)
        assertEquals(2, monstre.getX());
        assertEquals(1, monstre.getY());
    }

    @Test
    public void test_Jeu_monstrePrendDegatsBombeEtMeurt() throws InterruptedException {
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Place un monstre à côté du héros (xHero + 1, yHero) avec 3 PV
        Monstre monstre = new Monstre(xHero + 1, yHero, 3);
        jeu.getMonstres().add(monstre);

        assertTrue(jeu.getMonstres().contains(monstre));

        // Place et déclenche une bombe
        jeu.addBombe(xHero, yHero);
        Case bombe = jeu.getCase(xHero, yHero);
        assertNotNull(bombe);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(jeu.getHero());

        // Attend l'explosion
        Thread.sleep(1100);

        // Une bombe inflige -5 PV. Le monstre ayant 3 PV doit être mort et retiré de la liste
        assertFalse(jeu.getMonstres().contains(monstre));
    }

    @Test
    public void test_Aventurier_attaquerPoseBombe() {
        int xHero = jeu.getHero().getX();
        int yHero = jeu.getHero().getY();

        // Vérifie qu'il n'y a pas de bombe à la position du héros
        Case caseHero = jeu.getCase(xHero, yHero);
        assertNull(caseHero);

        // Le héros attaque (pose une bombe)
        jeu.getHero().attaquer(jeu);

        // Vérifie qu'une bombe a été ajoutée à la position du héros
        Case bombe = jeu.getCase(xHero, yHero);
        assertNotNull(bombe);
        assertTrue(bombe instanceof Bombe);
    }
}
