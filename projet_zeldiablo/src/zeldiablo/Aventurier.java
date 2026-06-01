package zeldiablo;

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

    public void depotBombe() {
    	// TODO : implémenter le dépôt de bombe
    	System.out.println("Bombe déposée !");
    }
}
