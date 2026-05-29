package personnage;

import moteurJeu.Jeu;

public class JeuPerso implements Jeu {
    private Personnage perso;

    public JeuPerso(Personnage perso) {
        if (perso != null) this.perso = perso;
        else this.perso = new Personnage(0, 0);
    }
}
