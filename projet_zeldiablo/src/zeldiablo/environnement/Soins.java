package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

public class Soins extends CaseEffet {

    public Soins(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Soins";
    }

    @Override
    public void effet(Jeu jeu, Personnage perso) {
        System.out.println("Une personne a trouvé une potion de vie ! +2pv ❤.");
        perso.addVie(2);
        jeu.detruire(x, y);
    }
}
