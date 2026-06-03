package zeldiablo.Objet;

import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Case;

public abstract class ObjetPlaçable extends Case implements Item   {
    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public ObjetPlaçable(int x, int y) {
        super(x, y);
    }

    @Override
    public void use(Personnage personnage) {

    }
}
