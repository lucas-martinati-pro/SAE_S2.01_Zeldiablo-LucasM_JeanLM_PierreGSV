package jeu;

public class Aventurier extends Personnage {


    public Aventurier(int x, int y, int vie) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
        if (vie > 0) this.vie = vie;
        else this.vie = 0;
    }

    // ########## Méthodes ##########
    /**
     * Affiche les informations sur l'aventurier.
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Position : (" + x + ", " + y + ")\n");
        res.append("Vie : " + vie);
        return res.toString();
    }

}
