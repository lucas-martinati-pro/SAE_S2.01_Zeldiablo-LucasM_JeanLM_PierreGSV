package personnage;

import moteurJeu.DessinJeu;

import java.awt.*;
import java.awt.image.BufferedImage;

import static arkanoidJeu.ArkanoidDessin.TAILLE;

public class DessinPerso implements DessinJeu {
    private JeuPerso jeuPerso;

    public DessinPerso(JeuPerso jeuPerso) {
        if (jeuPerso != null) this.jeuPerso = jeuPerso;
        else this.jeuPerso = new JeuPerso();
    }

    @Override
    public void dessiner(BufferedImage image) {
        // recupere un objet graphics sur l'image
        // c'est l'equivalent d'un crayon avec lequel on peut dessiner
        Graphics2D g = (Graphics2D) image.getGraphics();

        // dessine l'image a afficher avec les primitives de graphics2D

        // dessine le perso
        g.setColor(Color.blue);
        Personnage perso = jeuPerso.getPerso();
        g.fillOval(perso.getX() * TAILLE, perso.getY() * TAILLE, TAILLE, TAILLE);

        // rend le graphics
        g.dispose();
    }
}
