package jeu;

public class MurFriable implements Case {
    private int x;
    private int y;
    private boolean isBreak = false;

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

    public void detruire(){
        this.isBreak = true;
    }

    public boolean estDetruit(){
        return this.isBreak;
    }

    @Override
    public void effet(Personnage perso) {

    }
}
