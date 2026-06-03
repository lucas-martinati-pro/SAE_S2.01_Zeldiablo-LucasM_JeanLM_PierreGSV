package zeldiablo.environnement;

import zeldiablo.entite.Personnage;

/**
 * Interface représentant une case dans le jeu.
 */
public abstract class Case {
    protected int x;
    protected int y;
    protected boolean isTraversable = false;

    public boolean getIsTraversable() {
        return isTraversable;
    }

    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public Case(int x, int y) {
        this.x = Math.max(0, x);
        this.y = Math.max(0, y);
    }

    /**
     * Retourne la coordonnee x de la case.
     *
     * @return la coordonnee x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnee y de la case.
     *
     * @return la coordonnee y
     */
    public int getY() {
        return y;
    }

    /**
    * Retourne le type de la case.
    *
    * @return le type de la case
    */
    public abstract String getType();
}
