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

        Color vide = new Color(144, 238, 144); // Vert clair pour les cases vides

        int[] coordonnee = jeu.getLaby().returnSize();

        for (int y = 0; y < coordonnee[1]; y++) {
            for (int x = 0; x < coordonnee[0]; x++) {
                switch (jeu.getChar(x, y)) {
                    case Labyrinthe.FIN -> addImageCube("sprite/fin.png", x, y, g, Color.GREEN);
                    case Labyrinthe.MUR -> addImageCube("sprite/mur.png", x, y, g, Color.BLACK);
                    default -> addImageCube("sprite/vide.png", x, y, g, vide);
                }
            }
        }

        for (Case c : jeu.getCases()) {
            int x = c.getCoord()[0];
            int y = c.getCoord()[1];
            switch (c.getType()) {
                case "Piege" -> {
                    if (((Piege) c).getIsRevele()) {
                        addImageCube("sprite/piege.png", x, y, g, Color.ORANGE);
                    }
                }
                case "MurFriable" -> {
                    addImageCube("sprite/murFriable.png", x, y, g, Color.GRAY);
                }
                case "Bombe" -> {
                    try {
                        BufferedImage bombeImage = ImageIO.read(new File("sprite/bombe.png"));
                        g.drawImage(bombeImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                    } catch (IOException e) {
                        g.setColor(Color.MAGENTA);
                        g.fillOval(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                        System.err.println("Erreur lors du chargement de l'image de bombe : " + e.getMessage());
                    }
                }
            }
        }

        // Dessiner les flammes d'explosion
        for (int[] coord : jeu.getExplosionAffichage()) {
            addImageCube("sprite/flamme.png", coord[0], coord[1], g, Color.RED);
        }

        for (Personnage m : jeu.getMonstres()) {
            int x = m.getX(), y = m.getY();
            try {
                BufferedImage monstreImage = ImageIO.read(new File("sprite/monstre.png"));
                g.drawImage(monstreImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
            } catch (IOException e) {
                g.setColor(Color.RED);
                g.fillOval(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
                System.err.println("Erreur lors du chargement de l'image de monstre : " + e.getMessage());
            }
        }

        // Coeurs de vie du héros
        for (int i = 0; i < jeu.getHero().getVie(); i++) {
            int x = coordonnee[0] * TAILLE - (i + 1) * 17, y = 3;
            try {
                BufferedImage coeurImage = ImageIO.read(new File("sprite/coeur.png"));
                g.drawImage(coeurImage, x, y, 15, 15, null);
            } catch (IOException e) {
                g.setColor(Color.RED);
                g.fillOval(x, y, 10, 10);
                System.err.println("Erreur lors du chargement de l'image de coeur : " + e.getMessage());
            }
        }

        // Mettre le héros après les monstres pour qu'il soit dessiné par-dessus
        Personnage hero = jeu.getHero();
        if (hero != null) {
            int x = hero.getX(), y = hero.getY();
            try {
                BufferedImage heroImage = ImageIO.read(new File("sprite/hero.png"));
                g.drawImage(heroImage, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
            } catch (IOException e) {
                g.setColor(Color.BLUE);
                g.fillOval(x * TAILLE + 2, y * TAILLE + 2, TAILLE - 3, TAILLE - 3);
                System.err.println("Erreur lors du chargement de l'image du héros : " + e.getMessage());
            }
        }

        if (hero.etreMort()) {
            try {
                BufferedImage gameOverImage = ImageIO.read(new File("sprite/gameOver.jpg"));
                g.drawImage(gameOverImage, 0, 0, image.getWidth(), image.getHeight(), null);
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de l'image de Game Over : " + e.getMessage());
            }
        } else if (jeu.etreFini()) {
            try {
                BufferedImage winImage = ImageIO.read(new File("sprite/win.png"));
                g.drawImage(winImage, 0, 0, image.getWidth(), image.getHeight(), null);
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de l'image de victoire : " + e.getMessage());
            }
        }
    }

    private void addImageCube(String image, int x, int y, Graphics2D g, Color fallbackColor) {
        try {
            BufferedImage img = ImageIO.read(new File(image));
            g.drawImage(img, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
        } catch (IOException e) {
            g.setColor(fallbackColor);
            g.fillRect(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}