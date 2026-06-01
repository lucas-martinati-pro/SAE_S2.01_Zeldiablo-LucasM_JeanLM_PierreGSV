package zeldiablo;

public class Aventurier extends Personnage {
    private Jeu jeu;


    /**
     * Créer un avanturier
     * @param x coordonné x de l'avanturier
     * @param y coordonné y de l'avanturier
     * @param vie point de vie de l'avanturier
     */
    public Aventurier(int x, int y, int vie) {
        super(x, y, vie);
    }

    public void attaquer(Jeu jeu) {
        jeu.addBombe(x, y);
        Case bombe = jeu.getCase(x, y);
        ((Bombe) bombe).setJeu(jeu);
        bombe.effet(this);
    }
}
