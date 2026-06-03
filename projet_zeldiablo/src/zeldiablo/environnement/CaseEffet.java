package zeldiablo.environnement;

import zeldiablo.entite.Personnage;

public abstract class CaseEffet extends Case {
    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public CaseEffet(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    /**
     * Applique l'effet de la case sur le héros.
     */
    public abstract void effet(Personnage perso);
}