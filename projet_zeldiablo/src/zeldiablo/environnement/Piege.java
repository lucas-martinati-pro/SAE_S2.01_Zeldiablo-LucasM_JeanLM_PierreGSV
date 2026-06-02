package zeldiablo.environnement;

import zeldiablo.entite.Personnage;

/**
 * Represente un piege cache qui blesse le personnage s'il marche dessus.
 */
public class Piege extends Case {
    private boolean isRevele = false;

    /**
     * Cree un nouveau piege aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Piege(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Piege";
    }

    /**
     * Indique si le piege a ete revele (decouvert par un personnage).
     *
     * @return true si le piege est revele, false sinon
     */
    public boolean getIsRevele() {
        return isRevele;
    }

    @Override
    public void effet(Personnage perso) {
        System.out.println("Une personne à déclenché un piège ! -1pv ❤.");
        this.isRevele = true;
        perso.addVie(-1);
    }
}
