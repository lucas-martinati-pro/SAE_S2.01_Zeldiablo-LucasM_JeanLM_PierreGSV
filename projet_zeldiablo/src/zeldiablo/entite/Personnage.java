package zeldiablo.entite;

import zeldiablo.Jeu;
import moteurJeu.Commande;
import zeldiablo.environnement.*;
import zeldiablo.exception.ActionInconnueException;

/**
 * Represente un personnage abstrait dans le jeu.
 */
public abstract class Personnage extends Case {
    protected int vie;
    protected boolean isAttaque = false;

    public boolean getIsAttaque() {
        return isAttaque;
    }

    /**
     * Retourne la coordonnee x du personnage.
     *
     * @return la coordonnee x
     */
    public int getX() {
        return x;
    }

    /**
     * Retourne la coordonnee y du personnage.
     *
     * @return la coordonnee y
     */
    public int getY() {
        return y;
    }

    /**
     * Retourne les points de vie restants.
     *
     * @return les points de vie
     */
    public int getVie() {
        return vie;
    }

    /**
     * Definit les points de vie du personnage.
     *
     * @param vie les points de vie a definir
     */
    public void setVie(int vie) {
        this.vie = vie;
    }

    /**
     * Cree un nouveau personnage.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     * @param vie les points de vie
     */
    public Personnage(int x, int y, int vie) {
        super(x, y);
        this.vie = vie;
    }

    /**
     * Definit la position du personnage.
     *
     * @param x la nouvelle coordonnee x
     * @param y la nouvelle coordonnee y
     */
    public void setPos(int x, int y) {
        if (x > 0) this.x = x;
        else this.x = 0;
        if (y > 0) this.y = y;
        else this.y = 0;
    }

    /**
     * Ajoute des points de vie au personnage.
     *
     * @param i les points de vie a ajouter (peut etre negatif)
     */
    public void addVie(int i) {
        this.vie += i;
    }

    /**
     * Indique si le personnage est mort.
     *
     * @return true s'il est mort, false sinon
     */
    public boolean etreMort() {
        return this.vie < 1;
    }

    /**
     * Fait attaquer le personnage contre une victime.
     *
     * @param victime le personnage cible
     */
    public void attaquer(Personnage victime) {
        if (!etreMort()) victime.addVie(-2);

        this.isAttaque = true;
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            this.isAttaque = false;
        }).start();
    }

    /**
     * Deplace le personnage dans la direction indiquee par la commande.
     *
     * @param jeu l'instance du jeu pour verifier les collisions et les cases
     * @param commandeUser la commande contenant les directions de deplacement
     */
    public void deplacer(Jeu jeu, Commande commandeUser) {
        int[] coord = jeu.getSuivant(this.x, this.y, commandeUser);

        try {
            jeu.verifierDeplacement(coord[0], coord[1], commandeUser);

            for (Personnage m : jeu.getMonstres()) {
                if (m.getX() == coord[0] && m.getY() == coord[1]) {
                    return;
                }
            }

            Aventurier hero = jeu.getHero();
            if (hero.getX() == coord[0] && hero.getY() == coord[1]) return;

            Case c = jeu.getCase(coord[0], coord[1]);

            if (c == null || c.getIsTraversable()) {
                if (c instanceof CaseEffet cE) {
                    if (this instanceof Troll) jeu.detruire(coord[0], coord[1]);
                    else cE.effet(this);
                }
                this.setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Si le deplacement est invalide, ne rien faire
        }
    }
}
