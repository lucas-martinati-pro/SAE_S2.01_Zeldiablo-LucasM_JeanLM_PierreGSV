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
     * @param vie les points de vie
     */
    public Troll(int x, int y, int vie) {
        super(x, y, vie);
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
