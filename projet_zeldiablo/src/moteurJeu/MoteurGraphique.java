package moteurJeu;


/**
 * classe MoteurGraphique represente un moteur de jeu generique.
 *
 * On lui passe un jeu et un afficheur et il permet d'executer un jeu.
 */
public class MoteurGraphique {

	/**
	 * le jeu a executer
	 */
	private Jeu jeu;

	/**
	 * l'interface graphique
	 */
	private InterfaceGraphique gui;

	/**
	 * l'afficheur a utiliser pour le rendu
	 */
	private DessinJeu dessin;

	/**
	 * construit un moteur
	 *
	 * @param pJeu
	 *            jeu a lancer
	 * @param pAffiche
	 *            afficheur a utiliser
	 */
	public MoteurGraphique(Jeu pJeu, DessinJeu pAffiche) {
		// creation du jeu
		this.jeu = pJeu;
		this.dessin = pAffiche;
	}

	/**
	 * permet de lancer le jeu
	 */
	public void lancerJeu(int width, int height) throws InterruptedException {

		// creation ou mise a jour de l'interface graphique
		if (this.gui == null) {
			this.gui = new InterfaceGraphique(this.dessin, width, height);
		} else {
			this.gui.redimensionner(width, height);
		}
		Controleur controle = this.gui.getControleur();

		// boucle de jeu
		long lastLogicTick = System.currentTimeMillis();
		while (!this.jeu.etreFini()) {
			long now = System.currentTimeMillis();
			if (now - lastLogicTick >= 100) {
				// demande controle utilisateur
				Commande c = controle.getCommande();
				// fait evoluer le jeu
				this.jeu.evoluer(c);
				lastLogicTick = now;
			}
			// affiche le jeu (60 FPS)
			this.gui.dessiner();
			// met en attente
			Thread.sleep(16);
		}
	}

}
