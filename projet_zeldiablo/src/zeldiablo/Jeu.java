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
    public static final char PIEGE = 'P';
    public static final char BOMBE = '$';
    public static final char FIN = '&';
    public static final char AMULETTE = 'A';
    public static final char HERO = '@';
    public static final char SPIDER = 'S';
    public static final char TROLL = 'T';
    public static final char GHOST = 'G';
    public static final char BLOB = 'B';

    // ########## Variables ##########
    private int[] size;
    private Aventurier hero;
    private ArrayList<Personnage> monstres = new ArrayList<>();
    private GestionnaireMonstres gestionnaireMonstres = new GestionnaireMonstres(this);

    private ArrayList<Case> cases = new ArrayList<>();
    private ArrayList<int[]> explosionAffichage = new ArrayList<>();

    private int[] fin;
    private boolean recharger = true;

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

    /**
     * Retourne les coordonnees de la case de fin du niveau.
     *
     * @return les coordonnees de la case de fin
     */
    public int[] getFin() {
        return fin;
    }

    // ########## Méthodes ##########
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

        this.cases.clear();
        this.monstres.clear();
        this.hero = null;
        this.fin = null;

        int max = 0;
        for (String line : ligne) {
            if (line.length() > max) max = line.length();
        }
        this.size = new int[]{max, ligne.size()};

        for (int i = 0; i < ligne.size(); i++) {
            String line = ligne.get(i);
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case Jeu.VIDE -> {}
                    case Jeu.MUR -> this.cases.add(new Mur(j, i));
                    case Jeu.HERO -> {
                        hero = new Aventurier(j, i, 5);
                        hero.setJeu(this);
                    }
                    case Jeu.FIN -> fin = new int[]{j, i};
                    case Jeu.PIEGE -> this.cases.add(new Piege(j, i));
                    case Jeu.MUR_FRIABLE -> this.cases.add(new MurFriable(j, i));
                    case Jeu.AMULETTE -> this.cases.add(new Amulette(j, i));
                    case Jeu.SPIDER -> monstres.add(new Spider(j, i, 3));
                    case Jeu.TROLL -> monstres.add(new Troll(j, i, 1));
                    case Jeu.GHOST -> monstres.add(new Ghost(j, i, 4));
                    case Jeu.BLOB -> monstres.add(new Blob(j, i, 2));
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
        if (c instanceof Piege) {
            this.cases.remove(c);
            this.cases.add(new PiegeDetruit(x, y));
        }
        if (!(c instanceof Amulette)) this.cases.remove(getCase(x, y));
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
            if (c.getX() == x && c.getY() == y) {
                // Si c'est un piège détruit, on le considère comme une case vide
                if (!(c instanceof PiegeDetruit)) return c;
            }
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
            Case c = this.getCase(this.hero.getX(), this.hero.getY());
            if (recharger && (c == null)) { // temps de recharge de la bombe pour éviter les spams
                recharger = false;
                this.hero.attaquer(new Spider(0, 0, 0)); // La victime n'est pas utilisée dans l'attaque de l'aventurier, on peut donc lui donner n'importe quelle position et nombre de points de vie
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
        for (Case c : cases) {
            if (c instanceof Amulette) {
                levelHasAmulet = true;
                break;
            }
        }
        boolean hasAmuletIfRequired = !levelHasAmulet || hero.haveItem("Amulette");
        return (this.hero.getX() == this.fin[0] && this.hero.getY() == this.fin[1] && hasAmuletIfRequired) || this.hero.etreMort();
    }

    // =========================================================
    // SECTION : Compatibilités Testes
    // =========================================================

    /**
     * Retourne le caractere representant l'element present a la position (x, y).
     * L'ordre de priorite est : mur, hero, vide.
     *
     * @param x la colonne a verifier
     * @param y la ligne a verifier
     * @return le caractere correspondant a l'element a cette position
     */
    public char getChar(int x, int y) {
        Case c = getCase(x, y);
        if (c instanceof Mur) return Jeu.MUR;
        else if (this.hero.getX() == x && this.hero.getY() == y) return Jeu.HERO;
        else if (this.fin[0] == x && this.fin[1] == y) return Jeu.FIN;
        else {
            if (c != null) {
                switch (c.getType()) {
                    case "Piege": return Jeu.PIEGE;
                    case "MurFriable": return Jeu.MUR_FRIABLE;
                    case "Bombe": return Jeu.BOMBE;
                    case "Amulette": return Jeu.AMULETTE;
                    case "PiegeDetruit": return Jeu.VIDE;
                    case "Vide": return Jeu.VIDE;
                    case "Aventurier": return Jeu.HERO;
                    case "Spider": return Jeu.SPIDER;
                    case "Troll": return Jeu.TROLL;
                    case "Ghost": return Jeu.GHOST;
                }
            }
            return Jeu.VIDE;
        }
    }

    /**
     * Genere une representation textuelle du jeu sous forme de chaine de caracteres.
     *
     * @return la representation textuelle du jeu
     */
    public String jeuToString() {
        String res = "";
        int[] coordonnee = this.getSize();
        if (coordonnee == null) return res;

        for (int y = 0; y < coordonnee[1]; y++) {
            for (int x = 0; x < coordonnee[0]; x++) res += this.getChar(x, y);
            res += "\n";
        }
        return res;
    }
}