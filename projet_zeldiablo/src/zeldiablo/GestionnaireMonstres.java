package zeldiablo;

import moteurJeu.Commande;
import zeldiablo.entite.Personnage;
import zeldiablo.entite.Troll;
import zeldiablo.environnement.Case;

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
                int random = (int) (Math.random() * 5); // 5 actions possibles : haut, bas, gauche, droite, attaque
                Commande commandeUser = new Commande();
                switch (random) {
                    case 0 -> commandeUser.haut = true;
                    case 1 -> commandeUser.bas = true;
                    case 2 -> commandeUser.gauche = true;
                    case 3 -> commandeUser.droite = true;
                    case 4 -> monstreAttaque(jeu.getHero().getX(), jeu.getHero().getY());
                }

                // On déplace le monstre s'il y a une commande de déplacement, sinon on le laisse attaquer
                if (!(commandeUser.droite == false && commandeUser.gauche == false && commandeUser.haut == false && commandeUser.bas == false)) {
                    if (!jeu.getMonstres().isEmpty()) {
                        int index = (int) Math.floor(Math.random() * jeu.getMonstres().size());
                        Personnage monstre = jeu.getMonstres().get(index);

                        monstre.deplacer(jeu, commandeUser);
                        verifMort();

                        for (Personnage m : jeu.getMonstres()) {
                            if (m instanceof Troll t && !t.etreMort()) {
                                if (Math.random() < 0.05) { // 5% de chance de régénérer à chaque évolution
                                    t.regenerer();
                                }
                            }
                        }
                    }
                }
            }
        }, 100L, 100L); // Exécute toutes les 100 ms (100 long)
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
     * Supprime les monstres morts.
     */
    public void verifMort() {
        for (int i = 0; i < jeu.getMonstres().size(); i++) {
            Personnage m = jeu.getMonstres().get(i);
            if (m.etreMort()) {
                switch (m.getClass().getSimpleName()) { // Récupéré le nom de chaque classe de monstre pour afficher un message différent selon le type
                    case "Spider" -> System.out.println("Vous avez tué une araignée ! \uD83D\uDD77️");
                    case "Troll" -> System.out.println("Vous avez tué un troll ! \uD83D\uDC79");
                    case "Ghost" -> System.out.println("Vous avez tué un fantôme ! \uD83D\uDC80");
                    default -> System.out.println("Vous avez tué un monstre ! \uD83D\uDC7E");
                }
                jeu.getMonstres().remove(m);
                i--;
            }
        }
    }
}
