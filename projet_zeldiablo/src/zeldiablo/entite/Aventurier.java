package zeldiablo.entite;

import zeldiablo.environnement.Case;
import zeldiablo.environnement.Bombe;
import zeldiablo.Jeu;

/**
 * Represente un aventurier, le hero du jeu.
 */
public class Aventurier extends Personnage {
    /**
     * Créer un avanturier
     * @param x coordonné x de l'avanturier
     * @param y coordonné y de l'avanturier
     * @param vie point de vie de l'avanturier
     */
    public Aventurier(int x, int y, int vie) {
        super(x, y, vie);
    }

    /**
     * Fait attaquer l'aventurier en posant une bombe.
     *
     * @param jeu l'instance du jeu en cours
     */
    public void attaquer(Jeu jeu) {
        jeu.addBombe(x, y);
        Case bombe = jeu.getCase(x, y);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(this);
    }
}
