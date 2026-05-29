package jeu;

import moteurJeu.DessinJeu;

import java.awt.image.BufferedImage;

public class DessinLaby implements DessinJeu {
    private Jeu jeu;

    public DessinLaby(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    @Override
    public void dessiner(BufferedImage image) {
        String game = jeu.jeuToString();

        for (char c : game.toCharArray()) {

        }
    }
}
