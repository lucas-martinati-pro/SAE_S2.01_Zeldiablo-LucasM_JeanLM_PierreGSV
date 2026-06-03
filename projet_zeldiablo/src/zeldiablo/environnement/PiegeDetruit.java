package zeldiablo.environnement;

public class PiegeDetruit extends Case implements CaseDetruite {

    public PiegeDetruit(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "PiegeDetruit";
    }
}
