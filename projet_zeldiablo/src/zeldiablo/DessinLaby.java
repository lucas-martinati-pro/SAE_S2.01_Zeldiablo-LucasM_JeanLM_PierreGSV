package zeldiablo;

import zeldiablo.Item.Item;
import zeldiablo.entite.Ghost;
import zeldiablo.entite.Spider;
import zeldiablo.entite.Troll;
import zeldiablo.environnement.Case;
import zeldiablo.entite.Personnage;
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

    private BufferedImage flamme;
    private BufferedImage gameOver;
    private BufferedImage win;
    private BufferedImage porte;
    private BufferedImage porteOuverte;
    private BufferedImage mur;
    private BufferedImage vide;
    private BufferedImage piege;
    private BufferedImage murFriable;
    private BufferedImage bombe;
    private BufferedImage piegeDetruit;
    private BufferedImage amulette;
    private BufferedImage spider;
    private BufferedImage troll;
    private BufferedImage ghost;
    private BufferedImage hero;
    private BufferedImage coeur;

    /**
     * Constructeur de DessinLaby
     * @param jeu un objet de Type jeu
     */
    public DessinLaby(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();

        try {
            flamme = ImageIO.read(new File("sprite/flamme.png"));
            gameOver = ImageIO.read(new File("sprite/gameOver.jpg"));
            win = ImageIO.read(new File("sprite/win.png"));
            porte = ImageIO.read(new File("sprite/porte.png"));
            porteOuverte = ImageIO.read(new File("sprite/porteOuverte.png"));
            mur = ImageIO.read(new File("sprite/mur.png"));
            vide = ImageIO.read(new File("sprite/vide.png"));
            piege = ImageIO.read(new File("sprite/piege.png"));
            murFriable = ImageIO.read(new File("sprite/murFriable.png"));
            bombe = ImageIO.read(new File("sprite/bombe.png"));
            piegeDetruit = ImageIO.read(new File("sprite/piegeDetruit.png"));
            amulette = ImageIO.read(new File("sprite/amulette.png"));
            spider = ImageIO.read(new File("sprite/spider.png"));
            troll = ImageIO.read(new File("sprite/troll.png"));
            ghost = ImageIO.read(new File("sprite/ghost.png"));
            hero = ImageIO.read(new File("sprite/hero.png"));
            coeur = ImageIO.read(new File("sprite/coeur.png"));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des images : " + e.getMessage());
        }
    }

    /**
     * Dessin tous les éléments du jeu
     * @param image image sur laquelle dessiner
     */
    @Override
    public void dessiner(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        int[] coordonnee = jeu.getSize();

        // Ajout des sols
        for (int i = 0; i < coordonnee[0]; i++) {
            for (int j = 0; j < coordonnee[1]; j++) {
                g.drawImage(vide, i * TAILLE, j * TAILLE, TAILLE, TAILLE, null);
            }
        }

        // Ajout des cases
        for (Case c : jeu.getCases()) {
            int x = c.getX();
            int y = c.getY();
            switch (c.getType()) {
                case "Mur" -> g.drawImage(mur, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                case "Piege" -> {
                    if (((Piege) c).getIsRevele()) g.drawImage(piege, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                }
                case "MurFriable" -> g.drawImage(murFriable, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                case "Bombe" -> g.drawImage(bombe, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                case "PiegeDetruit" -> g.drawImage(piegeDetruit, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                case "Amulette" -> g.drawImage(amulette, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
                default -> g.drawImage(vide, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
            }
        }

        // Ajout de la porte de fin (porte ouverte ou porte fermée selon si le héros a l'amulette ou pas)
        int[] fin = jeu.getFin();
        if (jeu.getHero().haveItem("Amulette")) {
            g.drawImage(porteOuverte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
        } else {
            g.drawImage(porte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des flammes d'explosion
        for (int[] coord : jeu.getExplosionAffichage()) {
            g.drawImage(flamme, coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des monstres
        for (Personnage m : jeu.getMonstres()) {
            if (m instanceof Spider) g.drawImage(spider, m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE, null);
            if (m instanceof Troll) g.drawImage(troll, m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE, null);
            if (m instanceof Ghost) g.drawImage(ghost, m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout du héros
        Personnage hero = jeu.getHero();
        if (hero != null) {
            g.drawImage(this.hero, hero.getX() * TAILLE, hero.getY() * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des pv du héros
        for (int i = 0; i < jeu.getHero().getVie(); i++) {
            int x = coordonnee[0] * TAILLE - (i + 1) * (TAILLE - 7), y = 7;
            g.drawImage(coeur, x, y, TAILLE - 10, TAILLE - 10, null);
        }

        // Ajout de l'inventaire du héros
        ArrayList<Item> inventaire = jeu.getHero().getInventaire();
        for (int i = 0; i < inventaire.size(); i++) {
            Item o = inventaire.get(i);
            g.setColor(Color.LIGHT_GRAY);
            switch (o.getType()) {
                case "Amulette" -> g.drawImage(amulette, TAILLE/6 + (i) * 20, TAILLE/6, TAILLE - 10, TAILLE - 10, null);
            }
        }

        // Affichage de l'écran de fin si le héros est mort ou a gagné
        if (hero.etreMort()) {
            g.drawImage(gameOver, 0, 0, image.getWidth(), image.getHeight(), null);
        } else if (jeu.etreFini()) {
            g.drawImage(win, 0, 0, image.getWidth(), image.getHeight(), null);
        }
    }
}