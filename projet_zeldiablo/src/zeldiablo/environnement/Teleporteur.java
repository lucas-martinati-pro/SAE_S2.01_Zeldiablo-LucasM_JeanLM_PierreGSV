package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

/**
 * Represente un teleporteur qui teleporte les personnages qui marchent dessus vers un autre teleporteur libre.
 */
public class Teleporteur extends CaseEffet {

    /**
     * Cree un teleporteur aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Teleporteur(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Teleporteur";
    }

    /**
     * Teleporte le personnage sur un autre teleporteur libre dans le labyrinthe.
     *
     * @param jeu l'instance du jeu
     * @param perso le personnage qui marche sur le teleporteur
     */
    @Override
    public void effet(Jeu jeu, Personnage perso) {
        int[] size = jeu.getSize();
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                Case c = jeu.getCase(i, j);
                if (c != null) {
                    if (c instanceof Teleporteur && (c.getX() != this.x || c.getY() != this.y)) {
                        boolean libre = true;
                        for (Personnage m : jeu.getMonstres()) {
                            if (m.getX() == c.getX() && m.getY() == c.getY()) {
                                libre = false;
                                break;
                            }
                        }
                        if (jeu.getHero() != perso && jeu.getHero().getX() == c.getX() && jeu.getHero().getY() == c.getY()) {
                            libre = false;
                        }
                        if (libre) {
                            perso.setPos(c.getX(), c.getY());
                            return;
                        }
                    }
                }
            }
        }
    }
}
