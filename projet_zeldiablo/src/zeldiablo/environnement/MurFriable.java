package zeldiablo.environnement;

import zeldiablo.entite.Personnage;

import zeldiablo.entite.Personnage;

public class MurFriable implements Case {
    private int x;
    private int y;

    public MurFriable(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = y;
    }

    @Override
    public String getType() {
        return "MurFriable";
    }

    @Override
    public int[] getCoord() {
        return new int[]{x, y};
    }

    @Override
    public void effet(Personnage perso) {
    }
}
