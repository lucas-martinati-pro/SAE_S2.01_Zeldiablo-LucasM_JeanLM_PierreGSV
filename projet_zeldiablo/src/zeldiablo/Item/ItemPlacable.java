package zeldiablo.Item;

import zeldiablo.environnement.Case;

public abstract class ItemPlacable extends Case implements Item {

    /**
     * Construit une case en garantissant que les coordonnees ne sont pas negatives.
     *
     * @param x coordonnee x
     * @param y coordonnee y
     */
    public ItemPlacable(int x, int y) {
        super(x, y);
    }
}
