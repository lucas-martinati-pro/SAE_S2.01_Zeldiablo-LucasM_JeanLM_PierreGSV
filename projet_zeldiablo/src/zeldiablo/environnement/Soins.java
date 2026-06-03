package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

/**
 * Represente une case de soins qui redonne de la vie au personnage qui marche dessus.
 */
public class Soins extends CaseEffet {

    /**
     * Cree une case de soins aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Soins(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Soins";
    }

    /**
     * Soigne le personnage qui marche dessus en lui redonnant 2 points de vie, puis detruit la case de soins.
     *
     * @param jeu l'instance du jeu
     * @param perso le personnage soigne
     */
    @Override
    public void effet(Jeu jeu, Personnage perso) {
        System.out.println("Une personne a trouvé une potion de vie ! +2pv ❤.");
        perso.addVie(2);
        jeu.detruire(x, y);
    }
}
