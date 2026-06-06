package zeldiablo.entite;

/**
 * Represente un troll, un monstre capable de se regenerer.
 */
public class Troll extends Personnage {
    // ########## Constructeurs ##########
    /**
     * Cree un nouveau monstre.
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Troll(int x, int y) {
        super(x, y);
        this.vie = 6; // Les trolls ont plus de vie que les autres monstres
        this.degats = 3; // Les trolls font plus de degats que les autres monstres
    }

    @Override
    public String getType() {
        return "Troll";
    }

    // ########## Methodes ##########
    /**
     * si le troll est attaqué, il ne ce regenere pas, sinon il se regenere de 1 point de vie par tour
     *
     * @return void
     */
    public void regenerer() {
        if (this.vie < 6) { // Si le troll a été attaqué, il ne se régénère pas
            this.vie += 2;
        }
    }
}
