package zeldiablo;

import zeldiablo.Item.Item;
import zeldiablo.entite.Ghost;
import zeldiablo.entite.Spider;
import zeldiablo.entite.Troll;
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
import java.util.ArrayList;

/**
 * Gere le dessin du labyrinthe et des entites du jeu.
 */
public class DessinLaby implements DessinJeu {
    public static final int TAILLE = 30;
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
                    case Labyrinthe.MUR -> addImageCube("sprite/mur.png", x, y, g, Color.BLACK);
                    case Labyrinthe.FIN -> {
                        if (jeu.haveItem("Amulette")) addImageCube("sprite/porte.png", x, y, g, Color.GREEN);
                        else addImageCube("sprite/porteOuverte.png", x, y, g, Color.GREEN);
                    }
                    default -> addImageCube("sprite/vide.png", x, y, g, vide);
                }
            }
        }

        // ajout des cases
        for (Case c : jeu.getCases()) {
            int x = c.getX();
            int y = c.getY();
            switch (c.getType()) {
                case "Piege" -> {
                    if (((Piege) c).getIsRevele()) addImageCube("sprite/piege.png", x, y, g, Color.ORANGE);
                }
                case "MurFriable" -> addImageCube("sprite/murFriable.png", x, y, g, Color.GRAY);
                case "Bombe" -> addImageOval("sprite/bombe.png", x, y, g, Color.MAGENTA);
                case "PiegeDetruit" -> addImageCube("sprite/piegeDetruit.png", x, y, g, Color.DARK_GRAY);
                case "Amulette" -> addImageOval("sprite/amulette.png", x, y, g, Color.YELLOW);
            }
        }

        // Dessiner les flammes d'explosion
        for (int[] coord : jeu.getExplosionAffichage()) {
            addImageCube("sprite/flamme.png", coord[0], coord[1], g, Color.RED);
        }

        for (Personnage m : jeu.getMonstres()) {
            if (m instanceof Spider) addImageOval("sprite/spider.png", m.getX(), m.getY(), g, Color.RED);
            if (m instanceof Troll) addImageOval("sprite/troll.png", m.getX(), m.getY(), g, Color.DARK_GRAY);
            if (m instanceof Ghost) addImageOval("sprite/ghost.png", m.getX(), m.getY(), g, Color.LIGHT_GRAY);
        }

        // Coeurs de vie du héros
        for (int i = 0; i < jeu.getHero().getVie(); i++) {
            int x = coordonnee[0] * TAILLE - (i + 1) * 17, y = 3;
            try {
                BufferedImage coeurImage = ImageIO.read(new File("sprite/coeur.png"));
                g.drawImage(coeurImage, x, y, 15, 15, null); // Pas la même taille, donc pas de addImageOval()
            } catch (IOException e) {
                g.setColor(Color.RED);
                g.fillOval(x, y, 10, 10);
                System.err.println("Erreur lors du chargement de l'image de coeur : " + e.getMessage());
            }
        }

        // Mettre le héros après les monstres pour qu'il soit dessiné par-dessus
        Personnage hero = jeu.getHero();
        if (hero != null) {
            addImageOval("sprite/hero.png", hero.getX(), hero.getY(), g, Color.BLUE);
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

        ArrayList<Item> inventaire = jeu.getInventaire();
        for (int i = 0; i < inventaire.size(); i++) {
            Item o = inventaire.get(i);
            g.setColor(Color.LIGHT_GRAY);

            switch (o.getType()) {
                case "Amulette" -> addImageInventaire("sprite/amulette.png", TAILLE * i, i, g, Color.YELLOW);
                case "Bombe" -> addImageInventaire("sprite/bombe.png", TAILLE * i, TAILLE * i, g, Color.MAGENTA);
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

    private void addImageOval(String image, int x, int y, Graphics2D g, Color fallbackColor) {
        try {
            BufferedImage img = ImageIO.read(new File(image));
            g.drawImage(img, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
        } catch (IOException e) {
            g.setColor(fallbackColor);
            g.fillOval(x * TAILLE, y * TAILLE, TAILLE, TAILLE);
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }

    private void addImageInventaire(String image, int x, int y, Graphics2D g, Color fallbackColor) {
        try {
            BufferedImage img = ImageIO.read(new File(image));
            g.drawImage(img, x * TAILLE, y * TAILLE, 15, 15, null);
        } catch (IOException e) {
            g.setColor(fallbackColor);
            g.fillOval(x * TAILLE, y * TAILLE, 15, 15);
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
        }
    }
}