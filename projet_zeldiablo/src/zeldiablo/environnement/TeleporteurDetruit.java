package zeldiablo.environnement;

/**
 * Represente un teleporteur detruit.
 */
public class TeleporteurDetruit extends Case implements CaseDetruite {

    /**
     * Cree un teleporteur detruit aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public TeleporteurDetruit(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    @Override
    public String getType() {
        return "TeleporteurDetruit";
    }
}
