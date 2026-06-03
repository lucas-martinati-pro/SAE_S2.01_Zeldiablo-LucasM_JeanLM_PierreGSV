package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

public class Soins extends CaseEffet {

    public Soins(int x, int y) {
        super(x, y);
        this.isTraversable = true; // La potion est traversable
    }

    @Override
    public String getType() {
        return "Soins";
    }

    @Override
    public void effet(Jeu jeu, Personnage perso) {
        System.out.println("Une personne a trouvé une potion de vie ! +1pv ❤.");
        perso.addVie(5);
        jeu.detruire(x, y);
    }
}
