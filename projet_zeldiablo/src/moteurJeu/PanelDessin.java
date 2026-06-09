package moteurJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelDessin extends JPanel {

	/**
	 * la clase chargee de Dessiner
	 */
	private DessinJeu dessin;

	/**
	 * image suivante est l'image cachee sur laquelle dessiner
	 */
	private BufferedImage imageSuivante;

	/**
	 * image en cours est l'image entrain d'etre affichee
	 */
	private BufferedImage imageEnCours;

	/**
	 * la taille des images
	 */
	private int width, height;

	/**
	 * constructeur Il construit les images pour doublebuffering ainsi que le
	 * Panel associe. Les images stockent le dessin et on demande au panel la
	 * mise a jour quand le dessin est fini
	 *
	 * @param x largeur de l'image
	 * @param y hauteur de l'image
	 * @param affiche le dessin a utiliser pour dessiner le jeu
	 */
	public PanelDessin(int x, int y, DessinJeu affiche) {
		super();
		this.setPreferredSize(new Dimension(x, y));
		this.width = x;
		this.height = y;
		this.dessin = affiche;
		this.setBackground(Color.BLACK);

		// cree l'image buffer et son graphics
		this.imageSuivante = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		this.imageEnCours = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * demande de mettre a jour le rendu de l'image sur le Panel. Creer une
	 * nouvelle image vide sur laquelle dessiner
	 */
	public void dessinerJeu() {
		// generer la nouvelle image
		this.dessin.dessiner(this.imageSuivante);

		// inverses les images doublebuffereing
		BufferedImage temp = this.imageEnCours;
		// l'image a dessiner est celle qu'on a construite
		this.imageEnCours = this.imageSuivante;
		// l'ancienne image est videe
		this.imageSuivante = temp;
		this.imageSuivante.getGraphics()
				.fillRect(0, 0, this.width, this.height);
		// met a jour l'image a afficher sur le panel
		this.repaint();
	}

	/**
	 * redefinit la methode paint consiste a dessiner l'image en cours
	 *
	 * @param g graphics pour dessiner
	 */
	// HORS SAÉ : Utilisation de l'IA pour centrer l'image dans le panel et adapter la taille de l'image
	public void paint(Graphics g) {
		super.paint(g);
		int pWidth = getWidth();
		int pHeight = getHeight();

		double originalRatio = (double) this.width / this.height;
		double panelRatio = (double) pWidth / pHeight;

		int drawWidth = pWidth;
		int drawHeight = pHeight;
		int drawX = 0;
		int drawY = 0;

		// Ajustement selon le ratio le plus restrictif
		if (panelRatio > originalRatio) {
			drawWidth = (int) (pHeight * originalRatio);
			drawX = (pWidth - drawWidth) / 2; // Centrage horizontal
		} else {
			drawHeight = (int) (pWidth / originalRatio);
			drawY = (pHeight - drawHeight) / 2; // Centrage vertical
		}

		g.drawImage(this.imageEnCours, drawX, drawY, drawWidth, drawHeight, null);
	}

	/**
	 * Met a jour la taille du panel et recree les buffers d'images.
	 *
	 * @param x nouvelle largeur
	 * @param y nouvelle hauteur
	 */
	public void setTaille(int x, int y) {
		this.width = x;
		this.height = y;
		this.setPreferredSize(new Dimension(x, y));

		// Cree les nouvelles images avec la nouvelle taille
		this.imageSuivante = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.imageEnCours = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		this.revalidate();
	}

}
