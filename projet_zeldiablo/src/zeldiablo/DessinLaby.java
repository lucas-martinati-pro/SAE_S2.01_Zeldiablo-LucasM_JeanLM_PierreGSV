package zeldiablo;

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

        int[] coordonnee = jeu.getLaby().returnSize();

        for (Case c : jeu.getCases()) {
            int x = c.getCoord()[0];
            int y = c.getCoord()[1];
            switch (c.getType()) {
                case "Piege" -> {
                    if (((Piege) c).getIsRevele()) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                }
                case "MurFriable" -> {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                }
            }
        }

        for (int y = 0; y < coordonnee[1]; y++) {
            for (int x = 0; x < coordonnee[0]; x++) {
                switch (jeu.getChar(x, y)) {
                    case Labyrinthe.FIN -> {
                        g.setColor(Color.GREEN);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                    case Labyrinthe.MUR -> {
                        g.setColor(Color.BLACK);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                    case Labyrinthe.VIDE -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                    case Labyrinthe.HERO -> {
                        g.setColor(Color.BLUE);
                        g.fillOval(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                }
            }
        }
    }
}
