package personnage;

import moteurJeu.Commande;

public class Personnage {
    private int x;
    private int y;

    public Personnage(int x, int y) {
        if (x < 0) this.x = 0;
        else this.x = x;
        if (y < 0) this.y = 0;
        else this.y = y;
    }

    public void deplacer(Commande commandeUser) {
        if (commandeUser.droite) x++;
        if (commandeUser.gauche) x--;
        if (commandeUser.bas) y++;
        if (commandeUser.haut) y--;
    }
}
