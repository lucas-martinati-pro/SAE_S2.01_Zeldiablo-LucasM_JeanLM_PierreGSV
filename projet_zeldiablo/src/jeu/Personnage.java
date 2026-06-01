package jeu;

public abstract class Personnage {
    // attribut //
    protected int x;
    protected int y;
    protected int pv;


    public int getX(){
        return this.x;
    };

    public int getY(){
        return this.y;
    };

    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    };

    public int getVie(){
        return pv;
    };
    public void setVie(int Vie){

    };
    public boolean estMorts(){};
}
