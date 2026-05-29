package personnage;

import moteurJeu.Commande;
import moteurJeu.Jeu;

public class JeuPerso implements Jeu {
    private Personnage perso;

    public Personnage getPerso() {
        return perso;
    }

    public JeuPerso() {
        this.perso = new Personnage(0, 0);
    }

    @Override
    public void evoluer(Commande commandeUser) {
        this.perso.deplacer(commandeUser);
    }

    @Override
    public boolean etreFini() {
        return false;
    }
}
