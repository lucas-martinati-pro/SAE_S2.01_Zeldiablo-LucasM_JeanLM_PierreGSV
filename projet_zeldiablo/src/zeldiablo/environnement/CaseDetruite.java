package zeldiablo.environnement;

/**
 * Classe abstraite representant une case detruite dans le labyrinthe.
 * Par defaut, une case detruite est traversable par les personnages.
 */
public abstract class CaseDetruite extends Case {
    /**
     * Cree une case detruite aux coordonnees (x, y) et la definit comme traversable.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public CaseDetruite(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }
}
