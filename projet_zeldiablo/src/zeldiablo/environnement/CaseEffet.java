package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

/**
 * Represente une case ayant un effet specifique lorsqu'un personnage marche dessus.
 */
public abstract class CaseEffet extends Case {
    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    /**
     * Cree une case effet aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public CaseEffet(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    /**
     * Applique l'effet de la case sur le héros.
     */
    public abstract void effet(Jeu jeu, Personnage perso);
}