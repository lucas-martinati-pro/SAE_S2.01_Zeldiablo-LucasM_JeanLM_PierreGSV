package zeldiablo.item;

import zeldiablo.entite.Personnage;

public interface Item {
    /**
     * Retourne le type de l'item.
     *
     * @return le type de l'item en String
     */
    public String getType();

    /**
     * Utilise l'item sur le personnage specifie.
     *
     * @param personnage le personnage qui utilise l'item
     */
    public void use(Personnage personnage);
}
