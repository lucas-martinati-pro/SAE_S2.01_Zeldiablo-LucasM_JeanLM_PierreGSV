package jeu;

public abstract class Personnage {
    protected int x;
    protected int y;
    protected int vie;

    public int getX() {
        return x;
    };

    public int getY() {
        return y;
    };

    public void setPos(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
    };

    public void addVie(int i) {
        this.vie += i;
    };

    public boolean etreMort () {
        return this.vie <= 0;
    }

}
