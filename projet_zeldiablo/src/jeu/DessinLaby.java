package jeu;

import moteurJeu.DessinJeu;

import java.awt.*;
import java.awt.image.BufferedImage;

import static arkanoidJeu.ArkanoidDessin.TAILLE;

public class DessinLaby implements DessinJeu {
    private Jeu jeu;

    public DessinLaby(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    @Override
    public void dessiner(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        String game = jeu.jeuToString();

        int x = 0, y = 0;
        for (char c : game.toCharArray()) {
            switch (c) {
                case Labyrinthe.FIN -> {
                    g.setColor(Color.GREEN);
                    g.fillRect(x*TAILLE, y*TAILLE, TAILLE, TAILLE);
                }
                case Labyrinthe.MUR -> {
                    g.setColor(Color.GRAY);
                    g.fillRect(x*TAILLE, y*TAILLE, TAILLE, TAILLE);
                }
                case Labyrinthe.VIDE -> {
                    g.fillRect(x*TAILLE, y*TAILLE, TAILLE, TAILLE);
                    g.setColor(new Color(0, 0, 0, 50));
                    g.setStroke(new BasicStroke(2));
                }
                case Labyrinthe.HERO -> {
                    g.setColor(Color.BLUE);
                    g.fillRect(x*TAILLE, y*TAILLE, TAILLE, TAILLE);
                }
                case '\n' -> {
                    y++;
                    x = -1;
                }
            }
            x++;
        }
    }
}
