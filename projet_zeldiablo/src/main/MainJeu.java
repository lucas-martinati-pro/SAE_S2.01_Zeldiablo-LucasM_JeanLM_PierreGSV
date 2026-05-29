package main;

import jeu.ActionInconnueException;
import jeu.FichierIncorrectException;
import jeu.Jeu;

import java.io.IOException;
import java.util.Scanner;

/**
 * Classe principale permettant de lancer le jeu
 */
public class MainJeu {
    /**
     * main
     *
     * @param args arguments du main
     */
    public static void main(String[] args) {
        String laby = "laby/laby.txt";
        Scanner sc = new Scanner(System.in);

        System.out.println("Veuillez choisir un fichier (laby/laby.txt seras choisi automatiquement si aucune réponse) : ");
        String choix = sc.nextLine();
        if (choix != "") laby = choix;
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

        int nbDéplacements = 0;

        System.out.println("Voici les actions disponibles :" +
                "\n Haut (Z)" +
                "\n Bas (S)" +
                "\n Gauche (Q)" +
                "\n Droite (D)\n");

        while (!j.etreFini()) {
            System.out.println(j.jeuToString());
            System.out.println("Nombres de déplacements : " + nbDéplacements);
            System.out.println("Quelle action voulez-vous faire ? (Z/Q/S/D)");
            String action = sc.nextLine().toUpperCase();
            try {
                switch (action) {
                    case "Z" -> j.deplacerHero(Jeu.HAUT);
                    case "S" -> j.deplacerHero(Jeu.BAS);
                    case "Q" -> j.deplacerHero(Jeu.GAUCHE);
                    case "D" -> j.deplacerHero(Jeu.DROITE);
                }
                nbDéplacements++;
            } catch (ActionInconnueException e) {
                System.err.println("Action non valide !"); // On peut également faire un e.printStackTrace();
            }
        }
        String pluriel = "";
        if (nbDéplacements > 1) pluriel = "s";
        System.out.println("Félicitation, vous avez gagné en " + nbDéplacements + " déplacement" + pluriel + " !!!");
    }
}
