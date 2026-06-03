package zeldiablo.environnement;

public class PiegeDetruit extends Case {

    public PiegeDetruit(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "PiegeDetruit";
    }
}
