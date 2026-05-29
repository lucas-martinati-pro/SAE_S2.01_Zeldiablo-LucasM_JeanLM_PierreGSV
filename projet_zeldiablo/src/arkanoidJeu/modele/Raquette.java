package arkanoidJeu.modele;

import moteurJeu.Commande;

public class Raquette {

	/**
	 * position de la raquette
	 */
	private int raquetteX;
	private int raquetteY;

	/**
	 * plateau dans lequel la raquette bouge;
	 */
	Plateau plateau;

	/**
	 * construit une raquette en bas du plateau
	 * 
	 * @param p
	 *            plateau de jeu
	 */
	public Raquette(Plateau p) {
		this.plateau = p;
		this.raquetteX = this.plateau.gettX() / 2;
		this.raquetteY = this.plateau.gettY();
	}

	/**
	 * on fait evoluer la raquette en fonction de la commande
	 * 
	 * @param c
	 *            la commande correspondante
	 */
	public void evoluer(Commande c) {
		// on regarde si on deplace la raquette
		if (c.gauche && this.raquetteX > 1)
			this.raquetteX--;
		if (c.droite && this.raquetteX < plateau.gettX() - 2)
			this.raquetteX++;
	}

	public int getRaquetteX() {
		return raquetteX;
	}

	public int getRaquetteY() {
		return raquetteY;
	}

	
}
