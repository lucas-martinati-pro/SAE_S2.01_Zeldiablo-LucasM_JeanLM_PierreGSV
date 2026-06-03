package zeldiablo.environnement;

public abstract class CaseUtils extends Case {
    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public CaseUtils(int x, int y) {
        super(x, y);
    }

    /**
     * Applique l'effet de la case sur le héros.
     */
    public abstract void effet(zeldiablo.entite.Personnage perso);
}