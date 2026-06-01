package zeldiablo;

import java.sql.Time;

public class Bombe implements Case {
    private int x;
    private int y;
    private Jeu jeu;

    public void setJeu(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    public Bombe(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = y;
    }

    @Override
    public String getType() {
        return "Bombe";
    }

    @Override
    public int[] getCoord() {
        return new int[]{x, y};
    }

    @Override
    public void effet(Personnage perso) {
        Time time = new Time(1000);
        try {
            time.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        jeu.exploser(x, y);
        jeu.detruire(x, y);
    }
}
