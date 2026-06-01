package main;

import zeldiablo.DessinLaby;
import zeldiablo.exception.FichierIncorrectException;
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

        System.out.println("Voici les actions disponibles :" +
                "\n Haut (Z)" +
                "\n Bas (S)" +
                "\n Gauche (Q)" +
                "\n Droite (D)\n" +
                " Space (Espace) : pour poser une bombe !");

        DessinLaby jeu = new DessinLaby(j);

        int[] size = j.getLaby().returnSize();

        MoteurGraphique moteur = new MoteurGraphique(j, jeu);
        moteur.lancerJeu(TAILLE * size[0], TAILLE * size[1]);

        jeu.dessiner(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));

        boolean enCours = true;

        while (enCours) {
            if (lvl < 12) {
                if (j.etreFini() && !(j.getHero().etreMort())) {
                    System.out.println("next level");
                    lvl++;

                    String path = "laby/niveaux/lvl" + lvl + ".txt";

                    try {
                        j.chargerJeu(path);
                    } catch (IOException e) {
                        System.err.println("Une erreur s'est produite lors de la lecture du fichier : " + e.getMessage());
                        enCours = false; // On arrête si on ne trouve plus de niveau (fin du jeu)
                    } catch (FichierIncorrectException e) {
                        System.err.println("Le fichier n'est pas valide : " + e.getMessage());
                        enCours = false;
                    }

                    if (enCours) { // Si le fichier a bien chargé
                        j.getHero().addVie(5);
                        jeu = new DessinLaby(j);
                        size = j.getLaby().returnSize();
                        moteur = new MoteurGraphique(j, jeu);
                        moteur.lancerJeu(TAILLE * size[0], TAILLE * size[1]);
                    }

                } else if (j.getHero().etreMort()) {
                    System.out.println("Game Over");
                    enCours = false; // Le héros est mort, on sort de la boucle
                }
            } else {
                System.out.println("Félicitations, vous avez terminé tous les niveaux !");
                enCours = false; // Tous les niveaux sont terminés, on sort de la boucle
            }
        }
    }
}
