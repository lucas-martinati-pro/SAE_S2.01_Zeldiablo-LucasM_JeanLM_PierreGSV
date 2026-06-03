package zeldiablo.environnement;

/**
 * Represente un mur qui peut etre detruit (par exemple par une explosion).
 */
public class MurFriable extends Case {
    /**
     * Cree un nouveau mur friable aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public MurFriable(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "MurFriable";
    }
}
