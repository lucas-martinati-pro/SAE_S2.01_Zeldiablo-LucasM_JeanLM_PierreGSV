package zeldiablo;

import zeldiablo.environnement.Case;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.environnement.Piege;

import moteurJeu.DessinJeu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static arkanoidJeu.ArkanoidDessin.TAILLE;

/**
 * Gere le dessin du labyrinthe et des entites du jeu.
 */
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
                        try {
                            BufferedImage finImage = ImageIO.read(new File("sprite/fin.png"));
                            g.drawImage(finImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                        try {
                            BufferedImage piegeImage = ImageIO.read(new File("sprite/piege.png"));
                            g.drawImage(piegeImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                    }
                }
                case "MurFriable" -> {
                    try {
                        BufferedImage murFriableImage = ImageIO.read(new File("sprite/murFriable.png"));
                        g.drawImage(murFriableImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "Bombe" -> {
                    try {
                        BufferedImage bombeImage = ImageIO.read(new File("sprite/bombe.png"));
                        g.drawImage(bombeImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Dessiner les flammes d'explosion
        for (int[] coord : jeu.getExplosionAffichage()) {
            g.setColor(Color.ORANGE);
            g.fillRect(coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE);
        }

        for (Personnage m : jeu.getMonstres()) {
            try {
                BufferedImage monstreImage = ImageIO.read(new File("sprite/monstre.png"));
                g.drawImage(monstreImage, m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < jeu.getHero().getVie(); i++) {
            try {
                BufferedImage coeurImage = ImageIO.read(new File("sprite/coeur.png"));
                g.drawImage(coeurImage, coordonnee[0] * TAILLE - (i + 1) * 15, 5, 15, 15, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mettre le héros après les monstres pour qu'il soit dessiné par-dessus
        Personnage hero = jeu.getHero();
        if (hero != null) {
            try {
                BufferedImage heroImage = ImageIO.read(new File("sprite/hero.png"));
                g.drawImage(heroImage, hero.getX() * TAILLE, hero.getY() * TAILLE, TAILLE, TAILLE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}