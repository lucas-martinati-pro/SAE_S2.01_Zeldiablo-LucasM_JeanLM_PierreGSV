package jeu;

public class Aventurier implements Personnage {
    // ########## Variables ##########
    int x;
    int y;
    int pv;

    public Aventurier(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
    }

    // ########## Getters/Setters ##########
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // ########## Méthodes ##########
    public void setPos(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
    }

    @Override
    public int getVie() {
        return this.pv;
    }

    @Override
    public void setVie(int vie) {
        this.pv = vie;
    }

    @Override
    public boolean estMorts() {
        boolean res;

        if (this.pv <0){
            res = true;
        } else {
            res = false;
        }

        return res;
    }
}
