package zeldiablo.environnement;

import zeldiablo.Jeu;
import zeldiablo.entite.Personnage;

import java.util.ArrayList;

/**
 * Represente une bombe dans le jeu, capable d'exploser et de detruire des elements ou blesser des personnages.
 */
public class Bombe extends Case {

    /**
     * Cree une nouvelle bombe aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Bombe(int x, int y) {
        super(x, y);
        this.isTraversable = false; // La bombe n'est pas traversable
    }

    @Override
    public String getType() {
        return "Bombe";
    }

    /**
     * Fait exploser la bombe, appliquant des degats dans les 4 directions sur 3 cases et detruisant les obstacles destructibles.
     *
     * @param jeu l'instance du jeu
     */
    public void exploser(Jeu jeu) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int degâts = -5;
            ArrayList<int[]> casesTouchees = new ArrayList<>();
            casesTouchees.add(new int[]{x, y}); // Case centrale de la bombe

            // 4 directions : Droite, Gauche, Bas, Haut
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                for (int i = 1; i <= 3; i++) { // Extension jusqu'à 3 cases (langues de feu)
                    int cx = x + dir[0] * i;
                    int cy = y + dir[1] * i;

                    int[] size = jeu.getSize();
                    if (size != null && (cx < 0 || cy < 0 || cx >= size[0] || cy >= size[1])) break;
                    if (jeu.getCase(cx, cy) instanceof Mur) break;

                    casesTouchees.add(new int[]{cx, cy});

                    // Si on détruit un objet comme un mur friable, la flamme s'arrête
                    Case c = jeu.getCase(cx, cy);
                    if (c instanceof MurFriable) break;
                }
            }

            // Montrer l'explosion pendant 300ms (une seule fois en dehors de la boucle)
            jeu.getExplosionAffichage().addAll(casesTouchees); // Ajoute toutes les cases touchées à l'affichage de l'explosion
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
                jeu.getExplosionAffichage().clear();
            }).start();

            // Appliquer l'explosion et les dégâts sur les cases touchées
            for (int[] coord : casesTouchees) {
                int cx = coord[0];
                int cy = coord[1];

                // Dégâts au héro
                if (jeu.getHero().getX() == cx && jeu.getHero().getY() == cy) {
                    jeu.getHero().addVie(degâts);
                }

                // Dégâts aux monstres
                for (Personnage m : jeu.getMonstres()) {
                    if (m.getX() == cx && m.getY() == cy) {
                        m.addVie(degâts);
                        jeu.getGestionnaireMonstres().verifMort();
                        break; // Un même monstre ne prend les dégâts qu'une fois
                    }
                }

                // Détruire la case si destructible (Mur friable, autre bombe...)
                jeu.detruire(cx, cy);
            }
        }).start();
    }
}
