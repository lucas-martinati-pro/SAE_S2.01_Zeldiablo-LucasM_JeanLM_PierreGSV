package main;

import java.util.Scanner;

import arkanoidJeu.ArkanoidDessin;
import arkanoidJeu.ArkanoidJeu;
import moteurJeu.MoteurGraphique;

public class MainArkanoid {
	public static void main(String[] args) throws InterruptedException {
		// creation du jeu particulier et de son afficheur
		ArkanoidJeu jeu = new ArkanoidJeu();
		ArkanoidDessin aff = new ArkanoidDessin(jeu);

		// classe qui lance le moteur de jeu generique
		MoteurGraphique moteur = new MoteurGraphique(jeu, aff);
		// lance la boucle de jeu qui tourne jusqu'à la fin du jeu
		moteur.lancerJeu(400, 400);

		// lorsque le jeu est fini
		System.out.println("Fin du Jeu - appuyer sur entree");
		Scanner sc=new Scanner(System.in);
		sc.nextLine();
		System.exit(1);
	}
}
