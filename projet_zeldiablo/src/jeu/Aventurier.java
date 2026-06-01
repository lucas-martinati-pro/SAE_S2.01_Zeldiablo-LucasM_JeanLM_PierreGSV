package jeu;

public class Aventurier extends Personnage {
    // ########## Variables ##########
<<<<<<< HEAD
    int x;
    int y;
    int pv;
=======
>>>>>>> e1d7563322e53ec6978fa04b51a0579338258484

    public Aventurier(int x, int y, int vie) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
        if (vie > 0) this.vie = vie;
        else this.vie = 0;
    }

     // ########## Méthodes ##########

<<<<<<< HEAD
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
=======
     /**
      * Affiche les informations sur l'aventurier.
      */
     public String toString() {
         StringBuilder res = new StringBuilder();
         res.append("Position : (" + x + ", " + y + ")\n");
         res.append("Vie : " + vie);
         return res.toString();
     }
>>>>>>> e1d7563322e53ec6978fa04b51a0579338258484
}
