package zeldiablo.Item;

import zeldiablo.Jeu;
import zeldiablo.entite.Aventurier;
import zeldiablo.entite.Personnage;

public class Amulette extends ItemPlacable {
    private Jeu jeu;

    public Amulette(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Amulette";
    }

    @Override
    public void effet(Personnage perso) {
        if ((perso instanceof Aventurier)) ((Aventurier) perso).addInventaire(x, y);
    }

    @Override
    public void use(Personnage personnage) {

    }
}
