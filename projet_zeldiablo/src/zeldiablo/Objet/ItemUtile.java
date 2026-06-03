package zeldiablo.Objet;

import zeldiablo.entite.Personnage;

public interface ItemUtile extends Item {

    @Override
    void use(Personnage personnage);
}
