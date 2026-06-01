package zeldiablo;

import zeldiablo.environnement.Case;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.environnement.Piege;

import moteurJeu.DessinJeu;

import java.awt.*;
import java.awt.image.BufferedImage;

import static arkanoidJeu.ArkanoidDessin.TAILLE;

public class DessinLaby implements DessinJeu {
    private Jeu jeu;

    /**
     * Constructeur de DessinLaby
     * @param jeu un objet de Type jeu
     */
    public DessinLaby(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }


    /**
     * Dessin tous les éléments du jeu
     * @param image image sur laquelle dessiner
     */
    @Override
    public void dessiner(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        int[] coordonnee = jeu.getLaby().returnSize();

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
                }
            }
        }

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
                case "Bombe" -> {
                    g.setColor(Color.MAGENTA);
                    g.fillOval(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                }
            }
        }

        for (Personnage m : jeu.getMonstres()) {
            g.setColor(Color.RED);
            g.fillOval(m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE);
        }

        // Mettre le héros après les monstres pour qu'il soit dessiné par-dessus
        Personnage hero = jeu.getHero();
        if (hero != null) {
            g.setColor(Color.BLUE);
            g.fillOval(hero.getX() * TAILLE, hero.getY() * TAILLE, TAILLE, TAILLE);
        }
    }
}
