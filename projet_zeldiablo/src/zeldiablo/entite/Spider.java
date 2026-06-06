package zeldiablo.entite;

/**
 * Represente un monstre dans le jeu.
 */
public class Spider extends Personnage {
    // ########## Constructeurs ##########
    /**
     * Cree un nouveau monstre.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Spider(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Spider";
    }
}
