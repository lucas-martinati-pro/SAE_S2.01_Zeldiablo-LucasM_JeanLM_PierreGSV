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

    public void setJeu(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    public void attaquer() {
        jeu.addBombe(x, y);
        jeu.getCase(x, y).effet(this);
    }
}
