package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

public class Teleporteur extends CaseEffet {

    public Teleporteur(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Teleporteur";
    }

    @Override
    public void effet(Jeu jeu, Personnage perso) {
        for (Case c : jeu.getCases()) {
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
