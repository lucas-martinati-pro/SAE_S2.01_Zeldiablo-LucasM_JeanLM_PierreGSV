package zeldiablo.entite;

import zeldiablo.item.Item;
import zeldiablo.environnement.Case;
import zeldiablo.environnement.Bombe;
import zeldiablo.Jeu;

import java.util.ArrayList;

/**
 * Represente un aventurier, le hero du jeu.
 */
public class Aventurier extends Personnage {
    private ArrayList<Item> inventaire = new ArrayList<>();
    private Jeu jeu;

    /**
     * Créer un aventurier
     *
     * @param x coordonné x de l'aventurier
     * @param y coordonné y de l'aventurier
     * @param vie point de vie de l'aventurier
     */
    public Aventurier(int x, int y, int vie) {
        super(x, y, vie);
    }

    /**
     * Associe le jeu a l'aventurier.
     *
     * @param jeu l'instance du jeu
     */
    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Retourne l'inventaire de l'aventurier.
     *
     * @return la liste des items de l'inventaire
     */
    public ArrayList<Item> getInventaire() {
        return inventaire;
    }

    /**
     * Fait attaquer l'aventurier en posant une bombe.
     *
     * @param victime le personnage cible (non utilise ici car l'attaque pose une bombe)
     */
    @Override
    public void attaquer(Personnage victime) {
        Bombe b = new Bombe(x, y);
        jeu.addCase(b, x, y);
        b.exploser(jeu);

        this.isAttaque = true;
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            this.isAttaque = false;
        }).start();
    }

    /**
     * Verifie si le hero possede un item de type specifie dans son inventaire.
     *
     * @param nom le nom de l'item a verifier
     */
    public boolean haveItem(String nom) {
        for (Item o : this.inventaire) {
            if (o.getType().equals(nom))
                return true;
        }
        return false;
    }

    /**
     * Ajoute un item de la case situee aux coordonnees (x, y) dans l'inventaire du
     * hero et retire la case du jeu.
     *
     * @param x la colonne de la case contenant l'item a ajouter
     * @param y la ligne de la case contenant l'item a ajouter
     */
    public void addInventaire(int x, int y) {
        Case c = jeu.getCase(x, y);
        inventaire.add((Item) c);
        jeu.removeCase(x, y);
    }

    @Override
    public String getType() {
        return "Aventurier";
    }
}
