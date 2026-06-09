package arkanoidJeu;

import arkanoidJeu.modele.Balle;
import arkanoidJeu.modele.Raquette;
import moteurJeu.DessinJeu;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ArkanoidDessin implements DessinJeu {

	public static final int TAILLE = 20;

	ArkanoidJeu jeu;

	/**
	 * constructeur d'un dessin
	 *
	 * @param j jeu que l'on souhaite dessiner a chaque iteration
	 */
	public ArkanoidDessin(ArkanoidJeu j) {
		this.jeu = j;
	}

	@Override
	/**
	 * methode dessiner appelee par le moteur de jeu
	 *
	 * @param image image dans laquelle dessiner, cette image est fournie par le moteur
	 */
	public void dessiner(BufferedImage image) {
		// recupere un objet graphics sur l'image
		// c'est l'equivalent d'un crayon avec lequel on peut dessiner
		Graphics2D g = (Graphics2D) image.getGraphics();

		// dessine l'image a afficher avec les primitives de graphics2D

		// dessine la balle
		g.setColor(Color.blue);
		Balle balle = jeu.getBalle();
		g.fillOval(balle.getBalleX() * TAILLE, balle.getBalleY() * TAILLE, TAILLE, TAILLE);

		// dessine la raquette
		g.setColor(Color.red);
		Raquette raquette = jeu.getRaquette();
		int raquetteX = (raquette.getRaquetteX() - 1) * TAILLE;
		int raquetteY = (raquette.getRaquetteY() + 1) * TAILLE;
		g.fillRect(raquetteX, raquetteY, 3 * TAILLE, TAILLE / 4);

		// rend le graphics
		g.dispose();
	}

}
