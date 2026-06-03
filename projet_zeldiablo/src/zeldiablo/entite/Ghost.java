package zeldiablo.entite;

import moteurJeu.Commande;
import zeldiablo.Jeu;

/**
 * Represente un fantome, un type de monstre capable de traverser les murs.
 */
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

    /**
     * Deplace le fantome, en lui permettant de traverser les murs.
     *
     * @param jeu l'instance du jeu
     * @param commandeUser la commande de deplacement
     */
    @Override
    public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);
        for (Personnage m : jeu.getMonstres()) {
            if (m.getX() == coord[0] && m.getY() == coord[1]) {
                return;
            }
        }
        setPos(coord[0], coord[1]);
    }

    @Override
    public String getType() {
        return "Ghost";
    }
}

