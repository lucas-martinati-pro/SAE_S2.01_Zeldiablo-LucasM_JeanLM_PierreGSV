import personnage.DessinPerso;
import personnage.JeuPerso;

import java.awt.image.BufferedImage;

public class MainPerso {
    static void main(String[] args) {
        DessinPerso jeu = new DessinPerso(new JeuPerso());

        jeu.dessiner(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
    }
}
