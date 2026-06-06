package zeldiablo.entite;

import moteurJeu.Commande;
import zeldiablo.Jeu;

/**
 * Represente un blob, un monstre multi-cases immobile.
 */
public class Blob extends Personnage {
    // ########## Constructeurs ##########
    /**
     * Cree un nouveau monstre.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Blob(int x, int y) {
        super(x, y);
        this.vie = 2;
        this.degats = 1; // Les blobs font moins de degats que les autres monstres
    }

    @Override
    public String getType() {
        return "Blob";
    }

    @Override
    public void deplacer(Jeu jeu, Commande commandeUser) {

    }
}
