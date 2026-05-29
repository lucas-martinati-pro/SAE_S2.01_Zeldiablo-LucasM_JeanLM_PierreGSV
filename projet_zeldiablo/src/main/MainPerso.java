package main;

import moteurJeu.MoteurGraphique;
import personnage.DessinPerso;
import personnage.JeuPerso;

import java.awt.image.BufferedImage;

public class MainPerso {
    static void main(String[] args) throws InterruptedException {
        JeuPerso perso = new JeuPerso();
        DessinPerso jeu = new DessinPerso(perso);

        MoteurGraphique moteur = new MoteurGraphique(perso, jeu);
        moteur.lancerJeu(400, 400);

        jeu.dessiner(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
    }
}
