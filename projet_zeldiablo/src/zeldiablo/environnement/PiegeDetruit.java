package zeldiablo.environnement;

public class PiegeDetruit extends Case {
    public PiegeDetruit(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "PiegeDetruit";
    }

    @Override
    public void effet(zeldiablo.entite.Personnage perso) {
        // Aucun effet, le piège est déjà détruit
    }
}
