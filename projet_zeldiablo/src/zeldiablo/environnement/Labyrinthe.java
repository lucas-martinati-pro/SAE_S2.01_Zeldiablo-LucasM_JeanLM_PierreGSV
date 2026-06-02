package zeldiablo.environnement;

/**
 * Classe representant le labyrinthe du jeu.
 */
public class Labyrinthe {
    // ########## Variables ##########
    private boolean[][] murs;
    /**
     * Constante pour les murs dans un fichier de labyrinthe
     */
    public static final char MUR = '#';
    /**
     * Constante pour le hero dans un fichier de labyrinthe
     */
    public static final char HERO = '@';
    /**
     * Constante pour les monstres dans un fichier de labyrinthe
     */
    public static final char MONSTRE = '£';
    /**
     * Constante pour le vide dans un fichier de labyrinthe
     */
    public static final char FIN = '&';
    /**
     * Constante pour le vide dans un fichier de labyrinthe
     */
    public static final char VIDE = ' ';
    /**
     * Constante pour un piège dans un fichier de labyrinthe
     */
    public static final char PIEGE = '$';
    /**
     * Constante pour un MurFriable dans un fichier de labyrinthe
     */
    public static final char MurFriable = '*';
    /**
     * Constante pour une Bombe dans un fichier de labyrinthe
     */
    public static final char BOMBE = 'B';

    // ########## Constructeur ##########

    /**
     * Construit un labyrinthe de dimensions x * y, sans aucun mur.
     *
     * @param x le nombre de colonnes du labyrinthe (largeur)
     * @param y le nombre de lignes du labyrinthe (hauteur)
     */
    public Labyrinthe(int x, int y) {
        this.murs = new boolean[y][x];
    }

    // ########## Méthodes ##########
    /**
     * Ajoute un mur a la position (x, y) dans le labyrinthe.
     *
     * @param x la colonne du mur
     * @param y la ligne du mur
     */
    public void addMur(int x, int y) {
        this.murs[y][x] = true;
    }

    /**
     * Retourne les dimensions du labyrinthe.
     *
     * @return un tableau de deux entiers : {nombre de colonnes, nombre de lignes}
     */
    public int[] returnSize() {
        int y = murs.length;     // nombre de lignes
        int x;
        if (y != 0) x = murs[0].length;    // nombre de colonnes
        else x = 0;
        return new int[] {x, y};
    }

    /**
     * Verifie si la case (x, y) est un mur.
     *
     * @param x la colonne a verifier
     * @param y la ligne a verifier
     * @return true si la case est un mur, false sinon
     */
    public boolean getCase(int x, int y) {
        return this.murs[y][x];
    }
}