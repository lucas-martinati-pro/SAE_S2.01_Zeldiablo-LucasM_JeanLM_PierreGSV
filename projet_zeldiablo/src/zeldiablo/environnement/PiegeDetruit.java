package zeldiablo.environnement;

public class PiegeDetruit extends Case {

    public PiegeDetruit(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    @Override
    public String getType() {
        return "PiegeDetruit";
    }
}
