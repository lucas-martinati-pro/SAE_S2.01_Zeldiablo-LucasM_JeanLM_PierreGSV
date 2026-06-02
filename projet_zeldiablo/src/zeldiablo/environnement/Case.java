package zeldiablo.environnement;

import zeldiablo.entite.Personnage;

/**
 * Interface représentant une case dans le jeu.
 */
public interface Case {
    /**
    * Retourne le type de la case.
    *
    * @return le type de la case
    */
    public String getType();

    /**
    * Retourne les coordonnées de la case.
    *
    * @return les coordonnées de la case
    */
    public int[] getCoord();

    /**
     * Applique l'effet de la case sur le héros.
     */
    public void effet(Personnage perso);
}
