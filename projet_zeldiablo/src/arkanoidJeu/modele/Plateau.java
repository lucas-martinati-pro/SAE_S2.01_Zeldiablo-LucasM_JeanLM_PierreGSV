package arkanoidJeu.modele;

public class Plateau {

	/**
	 * la taille du plateau
	 */
	private int tX;
	private int tY;

	/**
	 * construit un plateau par defaut
	 */
	public Plateau() {
		this.tX=20;
		this.tY=18;
	}

	public int gettX() {
		return tX;
	}

	
	public int gettY() {
		return tY;
	}

	

}
