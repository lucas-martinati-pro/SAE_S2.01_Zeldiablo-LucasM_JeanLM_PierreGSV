package zeldiablo.entite;

import moteurJeu.Commande;
import zeldiablo.Jeu;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.exception.ActionInconnueException;

public class Troll extends Personnage {
    // ########## Constructeurs ##########
    /**
     * Cree un nouveau monstre.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     * @param vie les points de vie
     */
    public Troll(int x, int y, int vie) {
        super(x, y, vie);
    }

    // ########## Methodes ##########

    /**
     * si le troll est attaqué, il ne ce regenere pas, sinon il se regenere de 1 point de vie par tour
     *
     * @return void
     */
    public void regenerer() {
        if (this.vie < 6) { // Si le troll a été attaqué, il ne se régénère pas
            this.vie += 2;
        }
    }

     @Deprecated
     public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);
        try {
            jeu.verifierDeplacement(coord[0], coord[1], commandeUser);
            switch (jeu.getChar(coord[0], coord[1])) {
                case Labyrinthe.PIEGE -> {
                    jeu.getCases().remove(jeu.getCase(coord[0], coord[1])); // Le Troll détruit le piège en marchant dessus
                    this.setPos(coord[0], coord[1]);
                }
                case Labyrinthe.VIDE, Labyrinthe.FIN, Labyrinthe.AMULETTE -> this.setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
        }
    }
}
