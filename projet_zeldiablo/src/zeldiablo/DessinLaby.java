package zeldiablo;

import zeldiablo.entite.*;
import zeldiablo.item.Item;
import zeldiablo.environnement.Case;
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

    private BufferedImage flamme = chargerImage("flamme.png");
    private BufferedImage gameOver = chargerImage("gameOver.png");
    private BufferedImage win = chargerImage("win.png");
    private BufferedImage porte = chargerImage("porte.png");
    private BufferedImage porteOuverte = chargerImage("porteOuverte.png");
    private BufferedImage mur = chargerImage("mur.png");
    private BufferedImage vide = chargerImage("vide.png");
    private BufferedImage soins = chargerImage("soins.png");
    private BufferedImage soinsDetruit = chargerImage("soinsDetruit.png");
    private BufferedImage piegeCache = chargerImage("piegeCache.png");
    private BufferedImage piege = chargerImage("piege.png");
    private BufferedImage piegeDetruit = chargerImage("piegeDetruit.png");
    private BufferedImage teleporteur = chargerImage("teleporteur.png");
    private BufferedImage teleporteurDetruit = chargerImage("teleporteurDetruit.png");
    private BufferedImage murFriable = chargerImage("murFriable.png");
    private BufferedImage bombe = chargerImage("bombe.png");
    private BufferedImage amulette = chargerImage("amulette.png");
    private BufferedImage spider = chargerImage("spider.png");
    private BufferedImage troll = chargerImage("troll.png");
    private BufferedImage ghost = chargerImage("ghost.png");
    private BufferedImage blob = chargerImage("blob.png");
    private BufferedImage artificier = chargerImage("artificier.png");
    private BufferedImage hero = chargerImage("hero.png");
    private BufferedImage spiderAttaque = chargerImage("spiderAttaque.png");
    private BufferedImage trollAttaque = chargerImage("trollAttaque.png");
    private BufferedImage ghostAttaque = chargerImage("ghostAttaque.png");
    private BufferedImage blobAttaque = chargerImage("blobAttaque.png");
    private BufferedImage artificierAttaque = chargerImage("artificierAttaque.png");
    private BufferedImage heroAttaque = chargerImage("heroAttaque.png");
    private BufferedImage coeur = chargerImage("coeur.png");

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

        int[] coordonnee = jeu.getSize();

        // Ajout des sols
        for (int i = 0; i < coordonnee[0]; i++) {
            for (int j = 0; j < coordonnee[1]; j++) {
                g.drawImage(vide, i * TAILLE, j * TAILLE, TAILLE, TAILLE, null);
            }
        }

        // Ajout des cases
        int[] size = jeu.getSize();
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                Case c = jeu.getCase(i, j);
                if (c != null) {
                    if (c.getCaseSousJacente() != null) {
                        drawCase(g, c.getCaseSousJacente());
                    }
                    drawCase(g, c);
                }
            }
        }

        // Ajout de la porte de fin (porte ouverte ou porte fermée selon si le héros a l'amulette ou pas)
        int[] fin = jeu.getFin();
        if (jeu.getHero().haveItem("Amulette")) {
            g.drawImage(porteOuverte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
        } else {
            g.drawImage(porte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des monstres
        ArrayList<Personnage> monstres = new ArrayList<>(jeu.getMonstres());
        for (Personnage m : monstres) {
            BufferedImage img;

            if (m.getIsAttaque()) {
                img = switch (m.getType()) {
                    case "Spider" -> spiderAttaque;
                    case "Troll" -> trollAttaque;
                    case "Ghost" -> ghostAttaque;
                    case "Blob" -> blobAttaque;
                    case "Artificier" -> artificierAttaque;
                    default -> null;
                };
            } else {
                img = switch (m.getType()) {
                    case "Spider" -> spider;
                    case "Troll" -> troll;
                    case "Ghost" -> ghost;
                    case "Blob" -> blob;
                    case "Artificier" -> artificier;
                    default -> null;
                };
            }
            if (img != null) g.drawImage(img, m.getX() * TAILLE, m.getY() * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des flammes d'explosion
        ArrayList<int[]> explosionAffichage = new ArrayList<>(jeu.getExplosionAffichage());
        for (int[] coord : explosionAffichage) {
            g.drawImage(flamme, coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout du héros
        Personnage hero = jeu.getHero();
        int x = hero.getX(), y = hero.getY();
        if (hero.getIsAttaque()) {
            g.drawImage(heroAttaque, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
        } else {
            g.drawImage(this.hero,x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
        }

        // Ajout des pv du héros
        for (int i = 0; i < jeu.getHero().getVie(); i++) {
            x = coordonnee[0] * TAILLE - (i + 1) * (TAILLE - 7);
            y = 7;
            g.drawImage(coeur, x, y, TAILLE - 10, TAILLE - 10, null);
        }

        // Ajout de l'inventaire du héros
        ArrayList<Item> inventaire = new ArrayList<>(jeu.getHero().getInventaire());
        for (int i = 0; i < inventaire.size(); i++) {
            Item o = inventaire.get(i);
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

    /**
     * Dessine une case specifique a l'ecran.
     *
     * @param g le contexte graphique 2D
     * @param c la case a dessiner
     */
    public void drawCase(Graphics2D g, Case c) {
        int x = c.getX();
        int y = c.getY();
        BufferedImage img = switch (c.getType()) {
            case "Mur" -> mur;
            case "Soins" -> soins;
            case "SoinsDetruit" -> soinsDetruit;
            case "Piege" -> {
                if (((Piege) c).getIsRevele()) yield piege;
                else yield piegeCache;
            }
            case "PiegeDetruit" -> piegeDetruit;
            case "MurFriable" -> murFriable;
            case "Bombe" -> bombe;
            case "Amulette" -> amulette;
            case "Teleporteur" -> teleporteur;
            case "TeleporteurDetruit" -> teleporteurDetruit;
            default -> vide;
        };
        g.drawImage(img, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
    }

    /**
     * Charge une image a partir d'un chemin specifique.
     *
     * @param chemin le chemin de l'image a charger
     * @return l'image chargee ou null en cas d'erreur
     */
    private BufferedImage chargerImage(String chemin) {
        try {
            return ImageIO.read(new File("sprite/" + chemin));
        } catch (IOException e) {
            System.err.println("Erreur chargement : " + chemin + " - " + e.getMessage());
            return null; // Ou retourne une image par défaut (placeholder)
        }
    }
}