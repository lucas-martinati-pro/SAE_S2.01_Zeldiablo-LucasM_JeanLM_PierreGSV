package personnage;

import moteurJeu.Commande;
import moteurJeu.Jeu;

public class Personnage implements Jeu {
    @Override
    public void evoluer(Commande commandeUser) {

    }

    @Override
    public boolean etreFini() {
        return false;
    }
}
