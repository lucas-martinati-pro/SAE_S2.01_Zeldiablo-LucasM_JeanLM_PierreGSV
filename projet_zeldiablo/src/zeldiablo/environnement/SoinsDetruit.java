package zeldiablo.environnement;

public class SoinsDetruit extends Case implements CaseDetruite {

    public SoinsDetruit(int x, int y) {
        super(x, y);
        isTraversable = true;
    }

    @Override
    public String getType() {
        return "SoinsDetruit";
    }
}
