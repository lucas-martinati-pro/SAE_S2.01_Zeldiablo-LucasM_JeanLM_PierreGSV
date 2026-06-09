package moteurJeu;

import javax.swing.*;


/**
 * cree une interface graphique avec son controleur et son afficheur
 * @author Graou
 *
 */
public class InterfaceGraphique  {

	/**
	 * la JFrame de l'interface
	 */
	private JFrame frame;

	/**
	 * le Panel lie a la JFrame
	 */
	private PanelDessin panel;

	/**
	 * le controleur lie a la JFrame
	 */
	private Controleur controleur;

	/**
	 * la construction de l'interface grpahique
	 * - construit la JFrame
	 * - construit les Attributs
	 *
	 * @param afficheurUtil l'afficheur a utiliser dans le moteur
	 *
	 */
	public InterfaceGraphique(DessinJeu afficheurUtil,int x,int y)
	{
		//creation JFrame
		this.frame=new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creation panel
		this.panel=new PanelDessin(x, y,afficheurUtil);
		this.frame.setContentPane(this.panel);

		//ajout du controleur
		Controleur controlleurGraph=new Controleur();
		this.controleur=controlleurGraph;
		this.panel.addKeyListener(controlleurGraph);

		//recuperation du focus
		this.frame.pack();
		this.frame.getContentPane().setFocusable(true);
		this.frame.getContentPane().requestFocus();

		this.frame.setVisible(true);

	}


	/**
	 * retourne le controleur de l'affichage construit
	 * @return
	 */
	public Controleur getControleur() {
		return controleur;
	}

	/**
	 * demande la mise a jour du dessin
	 */
	public void dessiner() {
		this.panel.dessinerJeu();
	}

	/**
	 * ferme la fenetre et libere les ressources associees.
	 */
	public void dispose() {
		this.frame.dispose();
	}

	/**
	 * Met a jour la taille du panel interne et re-pack la fenetre.
	 *
	 * @param x nouvelle largeur
	 * @param y nouvelle hauteur
	 */
	public void redimensionner(int x, int y) {
		if (this.panel != null) {
			this.panel.setTaille(x, y);
		}
		if (this.frame != null) {
			this.frame.pack();
			this.frame.getContentPane().requestFocus();
		}
	}

}
