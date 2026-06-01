package zeldiablo;

import moteurJeu.Commande;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Classe principale gerant la logique du jeu.
 */
public class Jeu implements moteurJeu.Jeu {
    // ########## Variables ##########
    private Labyrinthe laby;
    private Aventurier hero;
    private ArrayList<Personnage> monstres = new ArrayList<>();
    private int[] fin;
    private int sense = 0;
    private ArrayList<Case> cases = new ArrayList<>();
    /**
     * Constantes pour se déplacer en haut
     */
    public static final String HAUT = "Haut";
    /**
     * Constantes pour se déplacer en bas
     */
    public static final String BAS = "Bas";
    /**
     * Constantes pour se déplacer à gauche
     */
    public static final String GAUCHE = "Gauche";
    /**
     * Constantes pour se déplacer à droite
     */
    public static final String DROITE = "Droite";

    /**
    * Constantes pour attaquer
    */
    public static final String SPACE = "Space";

    // ########## Getters/Setters ##########

    /**
     * Modifie le hero du jeu.
     *
     * @param hero le nouveau hero
     */
    public void setHero(Aventurier hero) {
        this.hero = hero;
    }

    /**
     * Modifie le labyrinthe du jeu.
     *
     * @param laby le nouveau labyrinthe
     */
    public void setLaby(Labyrinthe laby) {
        this.laby = laby;
    }

    /**
     * Retourne le hero du jeu.
     *
     * @return le hero du jeu
     */
    public Aventurier getHero() {
        return this.hero;
    }

    /**
     * Retourne le labyrinthe du jeu.
     *
     * @return le labyrinthe du jeu
     */
    public Labyrinthe getLaby() {
        return this.laby;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public ArrayList<Personnage> getMonstres() {
        return monstres;
    }

    // ########## Méthodes ##########

    public Case getCase(int x, int y) {
        for (Case c : cases) {
            if (c.getCoord()[0] == x && c.getCoord()[1] == y) return c;
        }
        return null;
    }

    /**
     * Charge un jeu a partir d'un fichier texte.
     * Lit le fichier ligne par ligne pour construire le labyrinthe,
     * positionner les murs, les caisses, les depots et le personnage.
     *
     * @param nomFichier le chemin vers le fichier de labyrinthe
     * @return le jeu charge et pret a etre joue
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException si une erreur de lecture survient
     * @throws FichierIncorrectException si le fichier contient des caracteres invalides, si le personnage est absent, ou si le nombre de caisses ne correspond pas au nombre de depots
     */
    public void chargerJeu(String nomFichier) throws FileNotFoundException, IOException, FichierIncorrectException {
        ArrayList<String> ligne = new ArrayList<>();
        Labyrinthe lab = convertLab(nomFichier, ligne);
        Aventurier hero = null;

        for (int i = 0; i < ligne.size(); i++) {
            String line = ligne.get(i);
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case Labyrinthe.MUR -> lab.addMur(j, i);
                    case Labyrinthe.HERO -> hero = new Aventurier(j, i, 5);
                    case Labyrinthe.FIN -> this.fin = new int[]{j, i};
                    case Labyrinthe.VIDE -> {}
                    case Labyrinthe.PIEGE -> cases.add(new Piege(j, i));
                    case Labyrinthe.MurFriable -> cases.add(new MurFriable(j,i));
                    case Labyrinthe.MONSTRE -> monstres.add(new Monstre(j, i, 3));
                    default -> throw new FichierIncorrectException("caractère inconnu " + line.charAt(j));
                }
            }
        }

        if (hero == null) throw new FichierIncorrectException("hero inconnu"); // Si il y as 2 personnages, ça prend le dernière
        else if (this.fin == null) throw new FichierIncorrectException("case de fin inconnue");
        this.laby = lab;
        this.hero = hero;
    }

    public void exploser(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (this.getChar(i, j) == Labyrinthe.HERO) this.hero.addVie(-2);
                for (Personnage m : this.monstres) {
                    if (m.getX() == i && m.getY() == j) {
                        m.addVie(-2);
                        break;
                    }
                }
                for (Case c : cases) {
                    int[] coordCase = c.getCoord();
                    if (coordCase[0] == i && coordCase[1] == j) {
                        detruire(i, j);
                    }
                }
            }
        }
    }

    public void startMonsters() {
        Timer t = new Timer();
        t.schedule(new java.util.TimerTask() {
                       @Override
                       public void run() {
                           int random = (int) (Math.random() * 4);
                           Commande commandeUser = new Commande();
                           switch (random) {
                               case 0 -> commandeUser.haut = true;
                               case 1 -> commandeUser.bas = true;
                               case 2 -> commandeUser.gauche = true;
                               case 3 -> commandeUser.droite = true;
                           }
                           evoluerMonster(commandeUser);
                       }
                   }, Duration.ofSeconds(1).toMillis(), Duration.ofSeconds(1).toMillis());
    }

    /**
     * Lit un fichier texte et construit un labyrinthe vide aux bonnes dimensions.
     * Les lignes du fichier sont stockees dans la liste passee en parametre
     * pour etre traitees ensuite par chargerJeu.
     * Le labyrinthe retourne a les dimensions correspondant au nombre de lignes
     * et a la largeur maximale des lignes du fichier.
     *
     * @param nomFichier le chemin vers le fichier de labyrinthe
     * @param ligne la liste dans laquelle stocker les lignes lues
     * @return un labyrinthe vide aux bonnes dimensions
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException si une erreur de lecture survient
     */
    public static Labyrinthe convertLab(String nomFichier, ArrayList<String> ligne) throws FileNotFoundException, IOException {
        BufferedReader file = new BufferedReader(new FileReader(nomFichier));

        String currentLine;
        while ((currentLine = file.readLine()) != null) {
            ligne.add(currentLine);
        }
        // Fin du fichier
        int max = 0;
        for (String line : ligne) {
            if (line.length() > max) max = line.length();
        }
        Labyrinthe lab = new Labyrinthe(max, ligne.size());

        file.close();
        return lab;
    }

    /**
     * Retourne le caractere representant l'element present a la position (x, y).
     * L'ordre de priorite est : mur, hero, vide.
     *
     * @param x la colonne a verifier
     * @param y la ligne a verifier
     * @return le caractere correspondant a l'element a cette position
     */
    public char getChar(int x, int y) {
        if (this.laby.getCase(x, y)) return Labyrinthe.MUR;
        else if (this.fin[0] == x && this.fin[1] == y) return Labyrinthe.FIN;
        else {
            for (Case c : this.cases) {
                if (c.getCoord()[0] == x && c.getCoord()[1] == y) {
                    if (c instanceof Piege) return Labyrinthe.PIEGE;
                    if (c instanceof MurFriable) return Labyrinthe.MurFriable;
                }
            }
            for (Personnage m : this.monstres) {
                if (m.getX() == x && m.getY() == y) return Labyrinthe.MONSTRE;
            }
            if (this.hero.getX() == x && this.hero.getY() == y) return Labyrinthe.HERO;
            return Labyrinthe.VIDE;
        }
    }

    public void detruire(int x, int y) {
        this.cases.remove(getCase(x, y));
    }

    /**
     * Calcule la position suivante a partir d'une position (x, y) et d'une direction.
     *
     * @param x la colonne actuelle
     * @param y la ligne actuelle
     * @param commandeUser la direction du deplacement
     * @return un tableau {nouvelleColonne, nouvelleLigne} apres deplacement
     */
    public int[] getSuivant(int x, int y, Commande commandeUser) {
        if (commandeUser.haut) {
            y--;
            this.sense = 0;
        }
        if (commandeUser.bas) {
            y++;
            this.sense = 1;
        }
        if (commandeUser.gauche) {
            x--;
            this.sense = 2;
        }
        if (commandeUser.droite) {
            x++;
            this.sense = 3;
        }
        if (commandeUser.space) {
            this.hero.attaquer();
        }
        return new int[] {x, y};
    }

    public void addBombe(int x, int y) {
        cases.add(new Bombe(x, y));
    }

    public void monstreAttaque(int x, int y) {
        int[][] rayon = {{x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
                {x - 1, y}, {x + 1, y},
                {x -1, y + 1}, {x, y + 1}, {x + 1, y + 1}};
        for (int[] coord : rayon) {
            int xRayon = coord[0];
            int yRayon = coord[1];
            for (Personnage m : this.monstres) {
                if (m.getX() == xRayon && m.getY() == yRayon) {
                    this.hero.attaquer();
                }
            }
        }
    }

    /**
     * Verifie si un deplacement vers la position (x, y) est possible.
     * Lance une exception si la case est un mur ou hors limites.
     *
     * @param x la colonne de destination
     * @param y la ligne de destination
     * @param commandeUser la direction du deplacement (pour le message d'erreur)
     * @throws ActionInconnueException si la case est un mur ou hors limites
     */
    public void verifierDeplacement(int x, int y, Commande commandeUser) throws ActionInconnueException {
        try {
            if (this.laby.getCase(x, y)) throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + commandeUser);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + commandeUser);
        }
    }

    /**
     * Deplace le hero dans la direction indiquee.
     *
     * @param commandeUser la direction du deplacement
     */
    @Override
    public void evoluer(Commande commandeUser) {
        int[] coord = getSuivant(hero.getX(), hero.getY(), commandeUser);
        try {
            verifierDeplacement(coord[0], coord[1], commandeUser);
            switch (this.getChar(coord[0], coord[1])) {
                case Labyrinthe.PIEGE -> {
                    for (Case c : cases) {
                        int[] coordCase = c.getCoord();
                        if (coordCase[0] == coord[0] && coordCase[1] == coord[1]) {
                            c.effet(this.hero);
                            break;
                        }
                    }
                    this.hero.setPos(coord[0], coord[1]);
                }
                case Labyrinthe.VIDE, Labyrinthe.FIN -> this.hero.setPos(coord[0], coord[1]);
            }
        } catch (ActionInconnueException e) {
            // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
        }
    }

    public void evoluerMonster(Commande commandeUser) {
        for (Personnage m : this.monstres) {
            int[] coord = getSuivant(m.getX(), m.getY(), commandeUser);
            try {
                verifierDeplacement(coord[0], coord[1], commandeUser);
                switch (this.getChar(coord[0], coord[1])) {
                    case Labyrinthe.PIEGE -> {
                        for (Case c : cases) {
                            int[] coordCase = c.getCoord();
                            if (coordCase[0] == coord[0] && coordCase[1] == coord[1]) {
                                c.effet(m);
                                break;
                            }
                        }
                        m.setPos(coord[0], coord[1]);
                    }
                    case Labyrinthe.VIDE, Labyrinthe.FIN -> m.setPos(coord[0], coord[1]);
                }
            } catch (ActionInconnueException e) {
                // Ignorer le déplacement si c'est un mur ou un mur friable ou un monstre
            }
        }
    }

    /**
     * Verifie si le jeu est fini, c'est-à-dire si le hero est sur la case de fin.
     *
     * @return true si le hero est sur la case de fin, false sinon
     */
    public boolean etreFini() {
        if (this.hero.getX() == this.fin[0] && this.hero.getY() == this.fin[1]) {
            System.out.println("Félicitation, vous avez gagné !!! \uD83C\uDFC6");
            return true;
        } else if (this.hero.etreMort()) {
            System.out.println("Vous êtes mort ! Vous avez perdu ! \uD83D\uDC80");
            return true;
        } else {
            return false;
        }
    }

    // ==================== Compatibilités Testes ====================
    /**
     * Deplace le hero dans la direction indiquee.
     *
     * @param action la direction du deplacement
     * @throws ActionInconnueException si le deplacement est impossible
     */
    public void evoluer(String action) throws ActionInconnueException {
        Commande commandeUser = new Commande();
        switch (action) {
            case HAUT -> commandeUser.haut = true;
            case BAS -> commandeUser.bas = true;
            case GAUCHE -> commandeUser.gauche = true;
            case DROITE -> commandeUser.droite = true;
            default -> throw new ActionInconnueException("L'action " + action + " n'est pas reconnue.");
        }
        evoluer(commandeUser);
    }

    /**
     * Verifie si un deplacement vers la position (x, y) est possible.
     * Lance une exception si la case est un mur ou hors limites.
     *
     * @param x la colonne de destination
     * @param y la ligne de destination
     * @param action la direction du deplacement (pour le message d'erreur)
     * @throws ActionInconnueException si la case est un mur ou hors limites
     */
    public void verifierDeplacement(int x, int y, String action) throws ActionInconnueException {
        try {
            if (this.laby.getCase(x, y)) throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + action);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + action);
        }
    }

    /**
     * Genere une representation textuelle du jeu sous forme de chaine de caracteres.
     *
     * @return la representation textuelle du jeu
     */
    public String jeuToString() {
        String res = "";
        int[] coordonnee = this.laby.returnSize();

        for (int y = 0; y < coordonnee[1]; y++) {
            for (int x = 0; x < coordonnee[0]; x++) res += this.getChar(x, y);
            res += "\n";
        }
        return res;
    }
}