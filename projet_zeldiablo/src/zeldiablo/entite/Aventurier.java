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

    public void setJeu(Jeu jeu) {
        this.jeu = jeu;
    }

    public ArrayList<Item> getInventaire() {
        return inventaire;
    }

    @Override
    public void attaquer(Personnage victime) {
        Bombe b = new Bombe(x, y);
        jeu.getCases().add(b);
        b.setJeu(jeu);
        b.exploser();

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
        jeu.getCases().remove(c);
    }

    @Override
    public String getType() {
        return "Aventurier";
    }
}
