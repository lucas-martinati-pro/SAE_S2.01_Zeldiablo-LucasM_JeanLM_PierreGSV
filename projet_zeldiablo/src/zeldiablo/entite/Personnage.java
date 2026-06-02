package zeldiablo.entite;

import zeldiablo.Jeu;
import moteurJeu.Commande;
import zeldiablo.environnement.Case;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.exception.ActionInconnueException;

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
        this.vie = vie;
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

    /**
     * Deplace le personnage dans la direction indiquee par la commande.
     *
     * @param jeu          l'instance du jeu pour verifier les collisions et les cases
     * @param commandeUser la commande contenant les directions de deplacement
     */
    public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);
        try {
            jeu.verifierDeplacement(coord[0], coord[1], commandeUser);
            switch (jeu.getChar(coord[0], coord[1])) {
                case Labyrinthe.PIEGE -> {
                    for (Case c : jeu.getCases()) {
                        if (c.getX() == coord[0] && c.getY() == coord[1]) {
                            if (this.x != coord[0] || this.y != coord[1]) {
                                c.effet(this);
                            }
                            break;
                        }
                    }
                    this.setPos(coord[0], coord[1]);
                }
                case Labyrinthe.VIDE, Labyrinthe.FIN -> this.setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
        }
    }
}
