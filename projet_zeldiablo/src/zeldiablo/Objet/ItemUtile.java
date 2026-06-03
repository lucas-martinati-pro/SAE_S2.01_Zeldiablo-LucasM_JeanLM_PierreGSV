package zeldiablo.Objet;

import zeldiablo.Item.Item;
import zeldiablo.entite.Personnage;

public interface ItemUtile extends Item {

    @Override
    void use(Personnage personnage);
}
