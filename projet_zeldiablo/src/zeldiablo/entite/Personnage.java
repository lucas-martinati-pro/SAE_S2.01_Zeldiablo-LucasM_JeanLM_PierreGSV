package zeldiablo.entite;

/**
 * Represente un personnage abstrait dans le jeu.
 */
public abstract class Personnage {
    protected int x;
    protected int y;
    protected int vie;

    /**
     * Retourne la coordonnee x du personnage.
     *
     * @return la coordonnee x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnee y du personnage.
     *
     * @return la coordonnee y
     */
    public int getY() {
        return y;
    }

    /**
     * Retourne les points de vie restants.
     *
     * @return les points de vie
     */
    public int getVie() {
        return vie;
    }

    /**
     * Cree un nouveau personnage.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     * @param vie les points de vie
     */
    public Personnage(int x, int y, int vie) {
        setPos(x, y);
        if (vie > 0) this.vie = vie;
        else this.vie = vie;
    }

    /**
     * Definit la position du personnage.
     *
     * @param x la nouvelle coordonnee x
     * @param y la nouvelle coordonnee y
     */
    public void setPos(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
    }

    /**
     * Ajoute des points de vie au personnage.
     *
     * @param i les points de vie a ajouter (peut etre negatif)
     */
    public void addVie(int i) {
        this.vie += i;
    }

    /**
     * Indique si le personnage est mort.
     *
     * @return true s'il est mort, false sinon
     */
    public boolean etreMort() {
        return this.vie < 1;
    }

    /**
     * Fait attaquer le personnage contre une victime.
     *
     * @param victime le personnage cible
     */
    public void attaquer(Personnage victime) {
        if (!etreMort()) victime.addVie(-2);
    }
}
