package zeldiablo.entite;

import moteurJeu.Commande;
import zeldiablo.Jeu;

public class Blob extends Personnage {
    // ########## Constructeurs ##########
    /**
     * Cree un nouveau monstre.
     *
     * @param x   la coordonnee x
     * @param y   la coordonnee y
     * @param vie les points de vie
     */
    public Blob(int x, int y, int vie) {
        super(x, y, vie);
    }

    @Override
    public String getType() {
        return "Blob";
    }

    @Override
    public void deplacer(Jeu jeu, Commande commandeUser) {

    }
}
