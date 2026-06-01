package zeldiablo;

public class Piege implements Case {
    private int x;
    private int y;
    private boolean isRevele = false;

    public Piege(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = y;
    }

    @Override
    public String getType() {
        return "Piege";
    }

    @Override
    public int[] getCoord() {
        return new int[]{x, y};
    }

    public boolean getIsRevele() {
        return isRevele;
    }

    @Override
    public void effet(Personnage perso) {
        System.out.println("🪤 : Vous avez déclenché un piège ! -1pv ❤.");
        this.isRevele = true;
        perso.addVie(-1);
    }
}
