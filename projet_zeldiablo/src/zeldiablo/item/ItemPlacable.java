package zeldiablo.item;

import zeldiablo.Jeu;
import zeldiablo.entite.Aventurier;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.CaseEffet;

public abstract class ItemPlacable extends CaseEffet implements Item {

    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public ItemPlacable(int x, int y) {
        super(x, y);
    }

    @Override
    public void effet(Jeu jeu, Personnage perso) {
        if ((perso instanceof Aventurier)) ((Aventurier) perso).addInventaire(x, y);
    }
}
