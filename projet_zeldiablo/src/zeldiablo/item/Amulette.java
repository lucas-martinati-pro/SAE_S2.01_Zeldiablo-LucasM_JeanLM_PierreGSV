package zeldiablo.item;

import zeldiablo.entite.Personnage;

/**
 * Represente l'amulette, un objet cle du jeu necessaire pour gagner.
 */
public class Amulette extends ItemPlacable {
    /**
     * Cree une nouvelle amulette aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Amulette(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    @Override
    public String getType() {
        return "Amulette";
    }

    /**
     * Utilise l'amulette (pas d'effet direct, elle doit simplement etre dans l'inventaire pour la victoire).
     *
     * @param personnage le personnage qui utilise l'amulette
     */
    @Override
    public void use(Personnage personnage) {

    }
}
