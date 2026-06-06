package zeldiablo.environnement;

/**
 * Represente une case de soins detruite.
 */
public class SoinsDetruit extends CaseDetruite {

    /**
     * Cree une case de soins detruite aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public SoinsDetruit(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "SoinsDetruit";
    }
}
