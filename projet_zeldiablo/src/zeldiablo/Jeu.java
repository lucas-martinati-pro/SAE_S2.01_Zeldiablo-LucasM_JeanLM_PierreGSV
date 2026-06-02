package zeldiablo;

import zeldiablo.environnement.Case;
import zeldiablo.entite.Monstre;
import zeldiablo.environnement.Bombe;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.entite.Aventurier;
import zeldiablo.exception.ActionInconnueException;
import zeldiablo.exception.FichierIncorrectException;
import zeldiablo.environnement.Piege;
import zeldiablo.environnement.MurFriable;

import moteurJeu.Commande;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe principale gerant la logique du jeu.
 */
public class Jeu implements moteurJeu.Jeu {
    // ########## Variables ##########
    private Labyrinthe laby;
    private Aventurier hero;
    private ArrayList<Personnage> monstres = new ArrayList<>();
    private GestionnaireMonstres gestionnaireMonstres = new GestionnaireMonstres(this);

    private ArrayList<Case> cases = new ArrayList<>();
    private ArrayList<int[]> explosionAffichage = new ArrayList<>();

    private int[] fin;
    private boolean recharger = true;

    /**
     * Constantes pour se déplacer en haut
     */
    public static final String HAUT = "Haut";
    /**
     * Constantes pour se déplacer en bas
     */
    public static final String BAS = "Bas";
    /**
     * Constantes pour se déplacer à gauche
     */
    public static final String GAUCHE = "Gauche";
    /**
     * Constantes pour se déplacer à droite
     */
    public static final String DROITE = "Droite";

    // ########## Getters/Setters ##########

    /**
     * Modifie la case de fin du jeu.
     *
     * @param fin la nouvelle coordonnee de fin
     */
    public void setFin(int[] fin) {
        this.fin = fin;
    }

    /**
     * Retourne le gestionnaire de monstres associe au jeu.
     *
     * @return le gestionnaire de monstres
     */
    public GestionnaireMonstres getGestionnaireMonstres() {
        return this.gestionnaireMonstres;
    }

    /**
     * Modifie le hero du jeu.
     *
     * @param hero le nouveau hero
     */
    public void setHero(Aventurier hero) {
        this.hero = hero;
    }

    /**
     * Modifie le labyrinthe du jeu.
     *
     * @param laby le nouveau labyrinthe
     */
    public void setLaby(Labyrinthe laby) {
        this.laby = laby;
    }

    /**
     * Retourne le hero du jeu.
     *
     * @return le hero du jeu
     */
    public Aventurier getHero() {
        return this.hero;
    }

    /**
     * Retourne le labyrinthe du jeu.
     *
     * @return le labyrinthe du jeu
     */
    public Labyrinthe getLaby() {
        return this.laby;
    }

    /**
     * Retourne la liste des cases (pieges, bombes, murs friables, etc.) presentes dans le jeu.
     *
     * @return la liste des cases
     */
    public ArrayList<Case> getCases() {
        return cases;
    }

    /**
     * Retourne la liste des coordonnees des explosions a afficher.
     *
     * @return la liste des coordonnees d'explosions
     */
    public ArrayList<int[]> getExplosionAffichage() {
        return explosionAffichage;
    }

    /**
     * Retourne la liste des monstres presents dans le jeu.
     *
     * @return la liste des monstres
     */
    public ArrayList<Personnage> getMonstres() {
        return monstres;
    }

    // ########## Méthodes ##########
    /**
     * Detruit la case situee aux coordonnees (x, y) et la retire du jeu.
     *
     * @param x la colonne de la case a detruire
     * @param y la ligne de la case a detruire
     */
    public void detruire(int x, int y) {
        this.cases.remove(getCase(x, y));
    }

    /**
     * Ajoute une bombe dans le jeu aux coordonnees (x, y).
     *
     * @param x la colonne ou deposer la bombe
     * @param y la ligne ou deposer la bombe
     */
    public void addBombe(int x, int y) {
        cases.add(new Bombe(x, y));
    }

    // =========================================================
    // SECTION : Déplacements
    // =========================================================

    /**
     * Cherche et retourne la case speciale a la position specifiee.
     *
     * @param x la colonne de la case
     * @param y la ligne de la case
     * @return la case correspondante ou null si aucune case n'est trouvee
     */
    public Case getCase(int x, int y) {
        for (Case c : cases) {
            if (c.getCoord()[0] == x && c.getCoord()[1] == y) return c;
        }
        return null;
    }

    /**
     * Retourne le caractere representant l'element present a la position (x, y).
     * L'ordre de priorite est : mur, hero, vide.
     *
     * @param x la colonne a verifier
     * @param y la ligne a verifier
     * @return le caractere correspondant a l'element a cette position
     */
    public char getChar(int x, int y) {
        if (this.laby.getCase(x, y)) return Labyrinthe.MUR;
        else if (this.fin[0] == x && this.fin[1] == y) return Labyrinthe.FIN;
        else {
            for (Case c : this.cases) {
                if (c.getCoord()[0] == x && c.getCoord()[1] == y) {
                    if (c instanceof Piege) return Labyrinthe.PIEGE;
                    if (c instanceof MurFriable) return Labyrinthe.MurFriable;
                    if (c instanceof Bombe) return Labyrinthe.BOMBE;
                }
            }
            for (Personnage m : this.monstres) {
                if (m.getX() == x && m.getY() == y) return Labyrinthe.MONSTRE;
            }
            if (this.hero.getX() == x && this.hero.getY() == y) return Labyrinthe.HERO;
            return Labyrinthe.VIDE;
        }
    }

    /**
     * Calcule la position suivante a partir d'une position (x, y) et d'une direction.
     *
     * @param x la colonne actuelle
     * @param y la ligne actuelle
     * @param commandeUser la direction du deplacement
     * @return un tableau {nouvelleColonne, nouvelleLigne} apres deplacement
     */
    public int[] getSuivant(int x, int y, Commande commandeUser) {
        if (commandeUser.haut) y--;
        if (commandeUser.bas) y++;
        if (commandeUser.gauche) x--;
        if (commandeUser.droite) x++;
        if (commandeUser.space) {
            if (recharger && this.getCase(this.hero.getX(), this.hero.getY()) == null) { // temps de recharge de la bombe pour éviter les spams
                recharger = false;
                this.hero.attaquer(this);
                new Thread (() -> { // Obliger de créer un nouveau Thread car sinon ça bloque le jeu pendant 2 secondes, et c'est pas très drôle
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    recharger = true;
                }).start();
            }
        }
        return new int[] {x, y};
    }

    /**
     * Verifie si un deplacement vers la position (x, y) est possible.
     * Lance une exception si la case est un mur ou hors limites.
     *
     * @param x la colonne de destination
     * @param y la ligne de destination
     * @param commandeUser la direction du deplacement (pour le message d'erreur)
     * @throws ActionInconnueException si la case est un mur ou hors limites
     */
    public void verifierDeplacement(int x, int y, Commande commandeUser) throws ActionInconnueException {
        try {
            if (this.laby.getCase(x, y)) throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + commandeUser);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + commandeUser);
        }
    }

    /**
     * Deplace le hero dans la direction indiquee.
     *
     * @param commandeUser la direction du deplacement
     */
    @Override
    public void evoluer(Commande commandeUser) {
        int[] coord = getSuivant(hero.getX(), hero.getY(), commandeUser);
        try {
            verifierDeplacement(coord[0], coord[1], commandeUser);
            switch (this.getChar(coord[0], coord[1])) {
                case Labyrinthe.PIEGE -> {
                    for (Case c : cases) {
                        int[] coordCase = c.getCoord();
                        if (coordCase[0] == coord[0] && coordCase[1] == coord[1]) {
                            if (this.hero.getX() != coord[0] || this.hero.getY() != coord[1]) {
                                c.effet(this.hero);
                            }
                            break;
                        }
                    }
                    this.hero.setPos(coord[0], coord[1]);
                }
                case Labyrinthe.VIDE, Labyrinthe.FIN -> this.hero.setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
        }
    }

    /**
     * Verifie si le jeu est fini, c'est-à-dire si le hero est sur la case de fin.
     *
     * @return true si le hero est sur la case de fin, false sinon
     */
    public boolean etreFini() {
        if (this.hero.getX() == this.fin[0] && this.hero.getY() == this.fin[1]) {
            System.out.println("Félicitation, vous avez gagné !!! \uD83C\uDFC6");
            return true;
        } else if (this.hero.etreMort()) {
            System.out.println("Vous êtes mort ! Vous avez perdu ! \uD83D\uDC80");
            return true;
        } else {
            return false;
        }
    }

    // =========================================================
    // SECTION : Compatibilités Testes
    // =========================================================

    /**
     * Deplace le hero dans la direction indiquee.
     *
     * @param action la direction du deplacement
     * @throws ActionInconnueException si le deplacement est impossible
     */
    public void evoluer(String action) throws ActionInconnueException {
        Commande commandeUser = new Commande();
        switch (action) {
            case HAUT -> commandeUser.haut = true;
            case BAS -> commandeUser.bas = true;
            case GAUCHE -> commandeUser.gauche = true;
            case DROITE -> commandeUser.droite = true;
            default -> throw new ActionInconnueException("L'action " + action + " n'est pas reconnue.");
        }
        evoluer(commandeUser);
    }

    /**
     * Verifie si un deplacement vers la position (x, y) est possible.
     * Lance une exception si la case est un mur ou hors limites.
     *
     * @param x la colonne de destination
     * @param y la ligne de destination
     * @param action la direction du deplacement (pour le message d'erreur)
     * @throws ActionInconnueException si la case est un mur ou hors limites
     */
    public void verifierDeplacement(int x, int y, String action) throws ActionInconnueException {
        try {
            if (this.laby.getCase(x, y)) throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + action);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + action);
        }
    }

    /**
     * Genere une representation textuelle du jeu sous forme de chaine de caracteres.
     *
     * @return la representation textuelle du jeu
     */
    public String jeuToString() {
        String res = "";
        int[] coordonnee = this.laby.returnSize();

        for (int y = 0; y < coordonnee[1]; y++) {
            for (int x = 0; x < coordonnee[0]; x++) res += this.getChar(x, y);
            res += "\n";
        }
        return res;
    }
}