package zeldiablo.item;

import zeldiablo.entite.Personnage;

public class Amulette extends ItemPlacable {
    public Amulette(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    @Override
    public String getType() {
        return "Amulette";
    }

    @Override
    public void use(Personnage personnage) {

    }
}
