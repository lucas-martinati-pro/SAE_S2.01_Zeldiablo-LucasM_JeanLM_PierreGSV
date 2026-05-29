package personnage;

import moteurJeu.Commande;
import moteurJeu.Jeu;

public class Personnage implements Jeu {
    private int x;
    private int y;

    public void deplacer(Commande commandeUser) {
        if (commandeUser.droite) x++;
        if (commandeUser.gauche) x--;
        if (commandeUser.bas) y++;
        if (commandeUser.haut) y--;
    }

    @Override
    public void evoluer(Commande commandeUser) {

    }

    @Override
    public boolean etreFini() {
        return false;
    }
}
