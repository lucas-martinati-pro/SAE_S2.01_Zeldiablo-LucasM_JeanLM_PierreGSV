package zeldiablo.environnement;

public class TeleporteurDetruit extends Case implements CaseDetruite {

    public TeleporteurDetruit(int x, int y) {
        super(x, y);
        this.isTraversable = true;
    }

    @Override
    public String getType() {
        return "TeleporteurDetruit";
    }
}
