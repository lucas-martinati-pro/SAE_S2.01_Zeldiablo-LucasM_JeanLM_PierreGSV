package zeldiablo.Item;

import zeldiablo.entite.Personnage;

public class Amulette extends ItemPlacable {

    public Amulette(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Amulette";
    }

    @Override
    public void effet(Personnage perso) {

    }

    @Override
    public void use(zeldiablo.entite.Personnage personnage) {

    }
}
