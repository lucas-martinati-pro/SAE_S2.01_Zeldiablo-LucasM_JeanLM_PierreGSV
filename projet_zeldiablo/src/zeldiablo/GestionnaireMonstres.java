package zeldiablo;

import moteurJeu.Commande;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Case;
import zeldiablo.environnement.Labyrinthe;
import zeldiablo.exception.ActionInconnueException;

import java.util.Timer;

/**
 * Gere le comportement, les deplacements et le cycle de vie des monstres du jeu.
 */
public class GestionnaireMonstres {

    private Jeu jeu;
    private Timer timer;

    /**
     * Cree un gestionnaire pour les monstres du jeu specifie.
     *
     * @param jeu l'instance du jeu
     */
    public GestionnaireMonstres(Jeu jeu) {
        this.jeu = jeu;
    }

    /**
     * Demarre le comportement automatique des monstres.
     */
    public void startMonsters() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                if (jeu.etreFini()) {
                    timer.cancel();
                    return;
                }
                int random = (int) (Math.random() * 4);
                Commande commandeUser = new Commande();
                switch (random) {
                    case 0 -> commandeUser.haut = true;
                    case 1 -> commandeUser.bas = true;
                    case 2 -> commandeUser.gauche = true;
                    case 3 -> commandeUser.droite = true;
                }
                monstreAttaque(jeu.getHero().getX(), jeu.getHero().getY());
                evoluerMonster(commandeUser);
            }
        }, 100L, 100L);
    }

    /**
     * Fait attaquer les monstres autour d'une cible.
     *
     * @param x la coordonnee x centrale
     * @param y la coordonnee y centrale
     */
    public void monstreAttaque(int x, int y) {
        int[][] rayon = {{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
                {x - 1, y}, {x + 1, y},
                {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}};
        for (int[] coord : rayon) {
            int xRayon = coord[0];
            int yRayon = coord[1];
            for (Personnage m : jeu.getMonstres()) {
                if (m.getX() == xRayon && m.getY() == yRayon) {
                    m.attaquer(jeu.getHero());
                }
            }
        }
    }

    /**
     * Gere le deplacement des monstres.
     *
     * @param commandeUser la direction de deplacement
     */
    public void evoluerMonster(Commande commandeUser) {
        if (!jeu.getMonstres().isEmpty()) {
            int index = (int) Math.floor(Math.random() * jeu.getMonstres().size());
            Personnage m = jeu.getMonstres().get(index);

            m.deplacer(jeu, commandeUser);
            verifMort();
        }
    }

    /**
     * Supprime les monstres morts.
     */
    public void verifMort() {
        jeu.getMonstres().removeIf(m -> {
            if (m.etreMort()) {
                System.out.println("Vous avez tué un monstre ! \uD83D\uDC7E");
                return true;
            }
            return false;
        });
    }
}
