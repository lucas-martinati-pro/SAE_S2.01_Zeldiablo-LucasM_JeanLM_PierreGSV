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
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Ghost(int x, int y) {
        super(x, y);
        this.vie = 4;
        this.degats = 1; // Les fantomes font moins de degats que les autres monstres
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
        int[] size = jeu.getSize();
        if (coord[0] >= 0 && coord[0] < size[0] && coord[1] >= 0 && coord[1] < size[1]) {
            for (Personnage m : jeu.getMonstres()) {
                if (m.getX() == coord[0] && m.getY() == coord[1]) {
                    return;
                }
            }
            Aventurier hero = jeu.getHero();
            if (hero != null && hero.getX() == coord[0] && hero.getY() == coord[1]) {
                return;
            }
            setPos(coord[0], coord[1]);
        }
    }

    @Override
    public String getType() {
        return "Ghost";
    }
}

