package zeldiablo.environnement;

/**
 * Interface représentant une case dans le jeu.
 */
public abstract class Case {
    protected int x;
    protected int y;
    protected boolean isTraversable = false;
    private Case caseSousJacente = null;

    /**
     * Retourne la case sous-jacente.
     *
     * @return la case sous-jacente
     */
    public Case getCaseSousJacente() {
        return caseSousJacente;
    }

    /**
     * Definit la case sous-jacente.
     *
     * @param caseSousJacente la case sous-jacente
     */
    public void setCaseSousJacente(Case caseSousJacente) {
        this.caseSousJacente = caseSousJacente;
    }

    /**
     * Indique si la case est traversable par les entites.
     *
     * @return true si la case est traversable, false sinon
     */
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
