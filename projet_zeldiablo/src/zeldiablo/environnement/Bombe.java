package zeldiablo.environnement;

import zeldiablo.entite.Personnage;
import zeldiablo.Jeu;

/**
 * Represente une bombe dans le jeu, capable d'exploser et de detruire des elements ou blesser des personnages.
 */
public class Bombe extends Case {
    private Jeu jeu;

    /**
     * Assigne l'instance du jeu a la bombe.
     *
     * @param jeu l'instance du jeu
     */
    public void setJeu(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    /**
     * Cree une nouvelle bombe aux coordonnees (x, y).
     *
     * @param x la coordonnee x
     * @param y la coordonnee y
     */
    public Bombe(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Bombe";
    }

    @Override
    public void effet(Personnage perso) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int degâts = -5;
            java.util.ArrayList<int[]> casesTouchees = new java.util.ArrayList<>();
            casesTouchees.add(new int[]{x, y}); // Case centrale de la bombe

            // 4 directions : Droite, Gauche, Bas, Haut
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] dir : directions) {
                for (int i = 1; i <= 3; i++) { // Extension jusqu'à 3 cases (langues de feu)
                    int cx = x + dir[0] * i;
                    int cy = y + dir[1] * i;

                    try {
                        // Si on touche un mur incassable, la flamme s'arrête
                        if (jeu.getLaby().getCase(cx, cy)) break;
                    } catch (Exception e) { break; } // Hors des limites

                    casesTouchees.add(new int[]{cx, cy});

                    // Si on détruit un objet comme un mur friable, la flamme s'arrête
                    Case c = jeu.getCase(cx, cy);
                    if (c instanceof zeldiablo.environnement.MurFriable) break;
                }
            }

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
                // Montrer l'explosion pendant 300ms
                jeu.getExplosionAffichage().addAll(casesTouchees);
                new Thread(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }
                    jeu.getExplosionAffichage().clear();
                }).start();

                // Détruire la case si destructible (Mur friable, autre bombe...)
                Case c = jeu.getCase(cx, cy);
                if (c != null) {
                    jeu.detruire(cx, cy);
                }
            }
        }).start();
    }
}
