package arkanoidJeu.modele;

public class Balle {

	/**
	 * position de la balle
	 */
	private int balleX;
	private int balleY;

	/**
	 * vitesse de la balle
	 */
	private int vBalleX;
	private int vBalleY;

	/**
	 * le plateau dans lequel la balle evolue
	 */
	Plateau plateau;

	/**
	 * constructeur d'une balle par defaut
	 * 
	 * @param p
	 *            plateau de jeu
	 */
	public Balle(Plateau p) {
		this.plateau = p;
		this.balleX = p.gettX() / 2;
		this.balleY = 0;
		this.vBalleX = 1;
		this.vBalleY = 1;
	}

	/**
	 * permet de faire evoluer la balle si elle sort du plateau en X, change sa
	 * vitesse
	 */
	public void deplacer() {
		this.balleX = this.balleX + this.vBalleX;
		this.balleY = this.balleY + this.vBalleY;

		// si la balle sort en X, on change direction
		if (this.balleX < 1 || this.balleX > this.plateau.gettX() - 2) {
			this.vBalleX = -vBalleX;
		}

		// si la balle arrive en haut, rebondit
		if (this.balleY < 1)
			this.vBalleY = -this.vBalleY;
	}

	/**
	 * permet de savoir si la balle sort du plateau et n'est pas rattrappee par
	 * le joueur
	 * 
	 * @param raquette
	 *            la raquette au cas ou
	 * @return true si la balle sort du plateau
	 */
	public boolean resterDansPlateau(Raquette raquette) {
		// si la balle sort en bas
		if (this.sortirPlateauParLeBas()) {
			// si raquette est la et la rattrappe
			if (this.etreRattrappee(raquette)) {
				// la balle rebondit (on inverse sa vitesse verticale)
				this.vBalleY = -this.vBalleY;
				return (true);
			} else {
				// sinon, la balle tombe et le joueur a perdu
				return (false);
			}
		}

		// sinon elle ne sort pas, la balle est bien rattrappee
		return (true);
	}

	/**
	 * permet de savoir si la balle sort du plateau en bas
	 * 
	 * @return true si elle sort par le bas
	 */
	public boolean sortirPlateauParLeBas() {
		return this.balleY > plateau.gettY() - 1;
	}

	/**
	 * permet de savoir si la raquette rattrappe a balle
	 * 
	 * @return true si la raquette rattrappe la balle
	 */
	private boolean etreRattrappee(Raquette raquette) {
		boolean auCentre = this.balleX == raquette.getRaquetteX();
		boolean aGauche = this.balleX == raquette.getRaquetteX() - 1;
		boolean aDroite = this.balleX == raquette.getRaquetteX() + 1;
		// on rattrappe la balle si
		// elle est au centre, juste a gauche ou juste a droite
		return auCentre || aGauche || aDroite;
	}

	public int getBalleX() {
		return balleX;
	}

	public int getBalleY() {
		return balleY;
	}

}
