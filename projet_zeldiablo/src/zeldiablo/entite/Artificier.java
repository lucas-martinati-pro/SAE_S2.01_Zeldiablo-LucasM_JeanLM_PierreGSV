package zeldiablo.entite;

import zeldiablo.Jeu;
import zeldiablo.environnement.Bombe;


/**
 * Represente un artificier, un monstre capable de poser des bombes.
 */
public class Artificier extends Personnage {
    private Jeu jeu;

    /**
     * Créer un Artificier
     *
     * @param x coordonné x de l'artificier
     * @param y coordonné y de l'artificier
     */
    public Artificier(int x, int y) {
        super(x, y);
        this.vie = 15;
    }

    /**
     * Associe le jeu a l'artificier.
     *
     * @param jeu l'instance du jeu
     */
    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Retourne le type de l'entite (Artificier).
     *
     * @return le type de l'entite
     */
    @Override
    public String getType() {
        return "Artificier";
    }

    // ########## Methodes ##########

    /**
     * L'artificier attaque en posant une bombe à sa position actuelle.
     * Si le jeu est défini, il ajoute une bombe à la position de l'artificier et fait exploser la bombe.
     * Sinon, il inflige des dégâts directs à la victime.
     *
     * @param victime le personnage ciblé par l'attaque de l'artificier
     */
    @Override
    public void attaquer(Personnage victime) {
        Bombe b = new Bombe(x, y);
        jeu.addCase(b);
        b.exploser(jeu);

        this.isAttaque = true;
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            this.isAttaque = false;
        }).start();
    }

}
