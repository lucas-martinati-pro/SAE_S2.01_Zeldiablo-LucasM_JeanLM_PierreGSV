package main;

import zeldiablo.DessinLaby;
import zeldiablo.FichierIncorrectException;
import zeldiablo.Jeu;
import moteurJeu.Commande;
import moteurJeu.MoteurGraphique;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

import static arkanoidJeu.ArkanoidDessin.TAILLE;

/**
 * Classe principale permettant de lancer le jeu
 */
public class MainZeldiablo {
    /**
     * main
     *
     * @param args arguments du main
     */
    public static void main(String[] args) throws InterruptedException {
        String laby;
        Scanner sc = new Scanner(System.in);

        System.out.println("Veuillez Choisir le niveau :" +
                "\n 1, 2, 3, 4, 5, 6, 7, 8, 9, 10");

        String choix = sc.nextLine();
        int lvl = 1;
        if (!choix.isEmpty()) {
            try {
                lvl = Integer.parseInt(choix);
            } catch (NumberFormatException e) {
                System.err.println("Veuillez entrer un nombre valide !");
                main(args);
            }
        }

        laby = "laby/niveaux/lvl" + lvl + ".txt";
        Jeu j = new Jeu();

        try {
            j.chargerJeu(laby);
        } catch (FichierIncorrectException e) {
            System.err.println("Le fichier n'est pas valide, veuillez réessayer : " + e.getMessage());
            main(args);
        } catch (IOException e) {
            System.err.println("Une erreur s'est produite lors de la lecture du fichier : " + e.getMessage());
            main(args);
        }

        j.startMonsters();

        Commande c = new Commande();

        int nbDéplacements = 0;

        System.out.println("Voici les actions disponibles :" +
                "\n Haut (Z)" +
                "\n Bas (S)" +
                "\n Gauche (Q)" +
                "\n Droite (D)\n");

        DessinLaby jeu = new DessinLaby(j);

        int[] size = j.getLaby().returnSize();

        MoteurGraphique moteur = new MoteurGraphique(j, jeu);
        moteur.lancerJeu(TAILLE * size[0], TAILLE * size[1]);

        jeu.dessiner(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));

        /**
        while (!j.etreFini()) {
            c.reset();
            System.out.println(j.jeuToString());
            System.out.println("Nombres de déplacements : " + nbDéplacements);
            System.out.println("Quelle action voulez-vous faire ? (Z/Q/S/D)");
            String action = sc.nextLine().toUpperCase();
            try {
                switch (action) {
                    case "Z" -> c.haut = true;
                    case "S" -> c.bas = true;
                    case "Q" -> c.gauche = true;
                    case "D" -> c.droite = true;
                    default -> throw new ActionInconnueException("L'action " + action + " n'est pas reconnue.");
                }
                j.deplacerHero(c);
                nbDéplacements++;
            } catch (ActionInconnueException e) {
                System.err.println("Action non valide !"); // On peut également faire un e.printStackTrace();
            }
        }
        String pluriel = "";
        if (nbDéplacements > 1) pluriel = "s";
        System.out.println("Félicitation, vous avez gagné en " + nbDéplacements + " déplacement" + pluriel + " !!!");
         */
    }
}
