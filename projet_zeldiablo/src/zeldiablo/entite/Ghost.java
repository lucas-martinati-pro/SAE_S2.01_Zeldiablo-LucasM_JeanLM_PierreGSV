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
     * @param x la coordonnee x
     * @param y la coordonnee y
     * @param vie les points de vie
     */
    public Ghost(int x, int y, int vie) {
        super(x, y, vie);
    }

    // ########## Methodes ##########

    public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);
        try {

            // Le Ghost peut se déplacer à travers les murs, donc on ne vérifie pas les déplacements
            if (jeu.getChar(coord[0], coord[1]) == Labyrinthe.VIDE) {
               this.setPos(coord[0], coord[1]);
                setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
        }

    }
}
