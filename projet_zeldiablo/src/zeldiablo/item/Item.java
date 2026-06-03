package zeldiablo.item;

import zeldiablo.entite.Personnage;

public interface Item {
    public String getType();

    public void use(Personnage personnage);
}
