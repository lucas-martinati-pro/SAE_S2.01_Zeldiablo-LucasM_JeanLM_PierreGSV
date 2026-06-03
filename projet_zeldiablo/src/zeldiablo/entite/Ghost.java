package zeldiablo.entite;

import moteurJeu.Commande;
import zeldiablo.Jeu;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.exception.ActionInconnueException;

public class Ghost extends Personnage {
    // ########## Constructeurs ##########

    /**
     * Cree un nouveau monstre.
     *
     * @param x   la coordonnee x
     * @param y   la coordonnee y
     * @param vie les points de vie
     */
    public Ghost(int x, int y, int vie) {
        super(x, y, vie);
    }

    // ########## Methodes ##########

    public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);
        setPos(coord[0], coord[1]);
    }
}

