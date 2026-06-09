package zeldiablo;

import zeldiablo.exception.FichierIncorrectException;
import zeldiablo.item.Amulette;
import zeldiablo.entite.*;
import zeldiablo.environnement.*;
import zeldiablo.exception.ActionInconnueException;

import moteurJeu.Commande;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe principale gerant la logique du jeu.
 */
public class Jeu implements moteurJeu.Jeu {
    public static final char VIDE = ' ';
    public static final char MUR = '#';
    public static final char MUR_FRIABLE = '*';
    public static final char SOINS = '+';
    public static final char PIEGE = 'P';
    public static final char TELEPORTEUR = '|';
    public static final char FIN = '&';
    public static final char AMULETTE = 'A';
    public static final char HERO = '@';
    public static final char SPIDER = 'S';
    public static final char TROLL = 'T';
    public static final char GHOST = 'G';
    public static final char BLOB = 'B';
    public static final char ARTIFICIER = 'R';

    // ########## Variables ##########
    private int[] size;
    private Aventurier hero;
    private ArrayList<Personnage> monstres = new ArrayList<>();
    private GestionnaireMonstres gestionnaireMonstres = new GestionnaireMonstres(this);

    private Case[][] cases; // Matrice de cases pour un accès plus rapide aux cases par coordonnées
    private ArrayList<int[]> explosionAffichage = new ArrayList<>();

    private int[] fin;
    private boolean recharger = true;
    private int niveau;

    // ########## Getters/Setters ##########

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
     * Modifie la taille du jeu.
     *
     * @param size la nouvelle taille
     */
    public void setSize(int[] size) {
        this.size = size;
    }

    /**
     * modifie le niveau du jeu
     *
     * @param niveau le nouveau niveau
     */
    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    /**
     * Retourne le niveau actuel du jeu
     *
     * @return le niveau actuel du jeu
     */
    public int getNiveau() {
        return niveau;
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
     * Retourne la taille du jeu.
     *
     * @return la taille du jeu
     */
    public int[] getSize() {
        return this.size;
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

    /**
     * Retourne les coordonnees de la case de fin du niveau.
     *
     * @return les coordonnees de la case de fin
     */
    public int[] getFin() {
        return fin;
    }

    /**
     * Retourne la matrice des cases du labyrinthe.
     *
     * @return la matrice des cases
     */
    public Case[][] getCases() {
        return cases;
    }

    // ########## Méthodes ##########
    /**
     * Ajoute une case specifique a une position donnee dans la grille du labyrinthe.
     *
     * @param c la case a ajouter
     */
    public void addCase(Case c) {
        int x = c.getX(), y = c.getY();
        if (cases == null) {
            cases = new Case[x + 1][y + 1];
            size = new int[]{x + 1, y + 1};
        }
        if (x >= 0 && y >= 0 && x < cases.length && y < cases[x].length) {
            Case existing = this.cases[x][y];
            if (existing != null && existing != c) {
                c.setCaseSousJacente(existing);
            }
            this.cases[x][y] = c;
        }
    }

    /**
     * Supprime la case a une position donnee dans la grille en la remplacant par sa case sous-jacente (ou null).
     *
     * @param x la coordonnee x (colonne)
     * @param y la coordonnee y (ligne)
     */
    public void removeCase(int x, int y) {
        if (cases != null && x >= 0 && y >= 0 && x < cases.length && y < cases[x].length) {
            Case c = this.cases[x][y];
            if (c != null) {
                this.cases[x][y] = c.getCaseSousJacente();
            }
        }
    }

    /**
     * Charge un jeu a partir d'un fichier texte.
     *
     * @param nomFichier le chemin vers le fichier de labyrinthe
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException si une erreur de lecture survient
     * @throws FichierIncorrectException si le fichier contient des caracteres invalides, si le personnage est absent, ou si le nombre de caisses ne correspond pas au nombre de depots
     */
    public void chargerNiveau(String nomFichier) throws FileNotFoundException, IOException, FichierIncorrectException {
        ArrayList<String> ligne = new ArrayList<>();

        BufferedReader file = new BufferedReader(new FileReader(nomFichier));

        String currentLine;
        while ((currentLine = file.readLine()) != null) {
            ligne.add(currentLine);
        }
        file.close();

        this.cases = null;
        this.monstres.clear();
        this.hero = null;
        this.fin = null;

        int max = 0;
        for (String line : ligne) {
            if (line.length() > max) max = line.length();
        }
        this.size = new int[]{max, ligne.size()};

        this.cases = new Case[max][ligne.size()];

        for (int i = 0; i < ligne.size(); i++) {
            String line = ligne.get(i);
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case Jeu.VIDE -> {}
                    case Jeu.MUR -> this.cases[j][i] = new Mur(j, i);
                    case Jeu.HERO -> {
                        hero = new Aventurier(j, i);
                        hero.setJeu(this);
                    }
                    case Jeu.FIN -> fin = new int[]{j, i};
                    case Jeu.PIEGE -> this.cases[j][i] = new Piege(j, i);
                    case Jeu.SOINS -> this.cases[j][i] = new Soins(j, i);
                    case Jeu.TELEPORTEUR -> this.cases[j][i] = new Teleporteur(j, i);
                    case Jeu.MUR_FRIABLE -> this.cases[j][i] = new MurFriable(j, i);
                    case Jeu.AMULETTE -> this.cases[j][i] = new Amulette(j, i);
                    case Jeu.SPIDER -> monstres.add(new Spider(j, i));
                    case Jeu.TROLL -> monstres.add(new Troll(j, i));
                    case Jeu.GHOST -> monstres.add(new Ghost(j, i));
                    case Jeu.BLOB -> monstres.add(new Blob(j, i));
                    case Jeu.ARTIFICIER -> {
                        Artificier artificier = new Artificier(j, i);
                        artificier.setJeu(this);
                        monstres.add(artificier);
                    }
                    default -> throw new FichierIncorrectException("caractère inconnu " + line.charAt(j));
                }
            }
        }

        if (hero == null) throw new FichierIncorrectException("hero inconnu");  // Si il y as 2 personnages, ça prend le dernière
        else if (fin == null) throw new FichierIncorrectException("case de fin inconnue");
    }

    /**
     * Detruit la case situee aux coordonnees (x, y) et la retire du jeu.
     *
     * @param x la colonne de la case a detruire
     * @param y la ligne de la case a detruire
     */
    public void detruire(int x, int y) {
        Case c = getCase(x, y);
        if (c != null) {
            if (c instanceof Amulette || c instanceof CaseDetruite || c instanceof Mur) {
                // Ces cases sont indestructibles par explosion
                return;
            }

            Case under = c.getCaseSousJacente();
            if (c instanceof Piege) {
                this.cases[x][y] = new PiegeDetruit(x, y);
                this.cases[x][y].setCaseSousJacente(under);
            }
            else if (c instanceof Soins) {
                this.cases[x][y] = new SoinsDetruit(x, y);
                this.cases[x][y].setCaseSousJacente(under);
            }
            else if (c instanceof Teleporteur) {
                this.cases[x][y] = new TeleporteurDetruit(x, y);
                this.cases[x][y].setCaseSousJacente(under);
            }
            else {
                // Pour les autres cases destructibles (comme Bombe, MurFriable, etc.)
                this.cases[x][y] = under;
                if (under != null) {
                    detruire(x, y); // Détruit la case en dessous
                }
            }
        }
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
        if (cases != null && x >= 0 && y >= 0 && x < cases.length && y < cases[x].length) {
            return cases[x][y];
        }
        return null;
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
        return new int[] {x, y};
    }

    /**
     * Verifie si un deplacement vers la position (x, y) est possible.
     * Lance une exception si la case est un mur ou hors limites.
     *
     * @param x la colonne de destination
     * @param y la ligne de destination
     * @throws ActionInconnueException si la case est un mur ou hors limites
     */
    public void verifierDeplacement(int x, int y, Commande commandeUser) throws ActionInconnueException {
        if (size == null || x < 0 || y < 0 || x >= size[0] || y >= size[1]) {
            throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + commandeUser);
        }
        Case c = getCase(x, y);
        if (c instanceof Mur) {
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
        if (commandeUser.space) {
            if (recharger) { // temps de recharge de la bombe pour éviter les spams
                recharger = false;
                this.hero.attaquer(new Spider(0, 0)); // La victime n'est pas utilisée dans l'attaque de l'aventurier, on peut donc lui donner n'importe quelle position et nombre de points de vie
                new Thread(() -> { // Obliger de créer un nouveau Thread car sinon ça bloque le jeu pendant 2 secondes, et c'est pas très drôle
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    recharger = true;
                }).start();
            }
        }

        if (!(commandeUser.droite == false && commandeUser.gauche == false && commandeUser.haut == false && commandeUser.bas == false)) {
            this.hero.deplacer(this, commandeUser);
        }
    }

    /**
     * Verifie si le jeu est fini, c'est-à-dire si le hero est sur la case de fin.
     *
     * @return true si le hero est sur la case de fin, false sinon
     */
    public boolean etreFini() {
        boolean levelHasAmulet = false;
        for (Case[] column : cases) {
            for (Case c : column) {
                if (c instanceof Amulette) {
                    levelHasAmulet = true;
                    break;
                }
            }
        }
        boolean hasAmuletIfRequired = !levelHasAmulet || hero.haveItem("Amulette");
        return (this.hero.getX() == this.fin[0] && this.hero.getY() == this.fin[1] && hasAmuletIfRequired) || this.hero.etreMort();
    }
}