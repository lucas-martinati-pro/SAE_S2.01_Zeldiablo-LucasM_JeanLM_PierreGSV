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
     * Créer un avanturier
     * @param x coordonné x de l'avanturier
     * @param y coordonné y de l'avanturier
     * @param vie point de vie de l'avanturier
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
        if (jeu != null) {
            jeu.addBombe(x, y);
            Case bombe = jeu.getCase(x, y);
            if (bombe instanceof Bombe b) {
                b.setJeu(jeu);
                b.effet(this);
            }
        } else {
            victime.addVie(-2);
        }
    }

    /**
     * Verifie si le hero possede un item de type specifie dans son inventaire.
     *
     * @param nom le nom de l'item a verifier
     */
    public boolean haveItem(String nom) {
        for (Item o : this.inventaire) {
            if (o.getType().equals(nom)) return true;
        }
        return false;
    }

    /**
     * Ajoute un item de la case situee aux coordonnees (x, y) dans l'inventaire du hero et retire la case du jeu.
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
