package zeldiablo.environnement;

/**
 * Represente un piege detruit (desactive).
 */
public class PiegeDetruit extends CaseDetruite {

    /**
     * Cree un piege detruit aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public PiegeDetruit(int x, int y) {
        super(x, y);
    }

    /**
     * Retourne le type de la case (PiegeDetruit).
     *
     * @return le type de la case
     */
    @Override
    public String getType() {
        return "PiegeDetruit";
    }
}
