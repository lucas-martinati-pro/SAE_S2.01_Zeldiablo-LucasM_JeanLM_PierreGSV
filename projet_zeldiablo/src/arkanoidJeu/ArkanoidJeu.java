package arkanoidJeu;

import arkanoidJeu.modele.Balle;
import arkanoidJeu.modele.Plateau;
import arkanoidJeu.modele.Raquette;
import moteurJeu.Commande;
import moteurJeu.Jeu;

/**
 * exemple utilisation de la libraire propose un jeu arkanoid
 * 
 * @author vthomas
 *
 */
public class ArkanoidJeu implements Jeu {

	/**
	 * la raquette du jeu
	 */
	private Raquette raquette;

	/**
	 * le plateau du jeu
	 */
	private Plateau plateau;

	/**
	 * balle qui se deplace
	 */
	private Balle balle;

	/**
	 * savoir si la balle est sortie
	 */
	boolean balleSortie = false;

	/**
	 * construit un jeu par defaut
	 */
	public ArkanoidJeu() {
		// on construit le plateau par defaut
		this.plateau = new Plateau();
		// une nouvelle balle au centre du plateau
		this.balle = new Balle(this.plateau);
		// la raquette que le joueur va controler
		this.raquette = new Raquette(this.plateau);
	}

	@Override
	/**
	 * methode appelee par le moteur pour faire evoluer le jeu
	 * 
	 * @param c commande faite par l'utilisateur (vient du moteur)
	 */
	public void evoluer(Commande c) {
		// on fait avancer la balle
		balle.deplacer();

		// on fait evoluer la raquette en fonction de la commande
		raquette.evoluer(c);

		// on regarde si la raquette rattrape la balle
		this.balleSortie = !this.balle.resterDansPlateau(this.raquette);
	}

	@Override
	/**
	 * methode appelee par le moteur de jeu pour savoir si le jeu est fini
	 * 
	 * @return true si le jeu est fini
	 */
	public boolean etreFini() {
		// ici le jeu est fini si la balle est sortie
		return this.balleSortie;
	}

	/**
	 * @return l'attribut balle du jeu
	 */
	public Balle getBalle() {
		return balle;
	}

	/**
	 * @return l'attribut raquette du jeu
	 */
	public Raquette getRaquette() {
		return raquette;
	}

}
