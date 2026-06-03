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
        int x = this.x, y = this.y;
        for (Case c : jeu.getCases()) {
            if (c instanceof Teleporteur && (c.getX() != this.x || c.getY() != this.y)) {
                for (Personnage m : jeu.getMonstres()) {
                    if (m.getX() == c.getX() && m.getY() == c.getY()) {
                        break;
                    }
                    x = c.getX();
                    y = c.getY();
                }
            }
        }
       perso.setPos(x, y);
    }


}
