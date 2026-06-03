package zeldiablo.environnement;

/**
 * Represente un mur infranchissable dans le labyrinthe.
 */
public class Mur extends Case {
    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public Mur(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Mur";
    }
}
