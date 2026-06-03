package zeldiablo.Objet;

import zeldiablo.entite.Personnage;

public class Amulette extends ObjetPlaçable {

    public Amulette(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "amulette";
    }

    @Override
    public void effet(Personnage perso) {

    }

}
