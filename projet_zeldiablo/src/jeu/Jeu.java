package jeu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe principale gerant la logique du jeu Sokoban.
 */
public class Jeu {
    // ########## Variables ##########
    private Labyrinthe laby;
    private Aventurier hero;
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

    // ########## Getters/Setters ##########

    /**
     * Modifie le hero du jeu.
     *
     * @param hero le nouveau hero
     */
    public void setHero(Aventurier hero) {
        this.hero = hero;
    }

    // ########## Méthodes ##########

    /**
     * Charge un jeu Sokoban a partir d'un fichier texte.
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
                    case Labyrinthe.HERO -> hero = new Aventurier(j, i);
                    case Labyrinthe.VIDE -> {}
                    default -> throw new FichierIncorrectException("caractère inconnu " + line.charAt(j));
                }
            }
        }


        if (hero == null) throw new FichierIncorrectException("personnage inconnu"); // Si il y as 2 personnages, ça prend le dernière
        this.laby = lab;
        this.hero = hero;
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
    private char getChar(int x, int y) {
        if (this.laby.getCase(x, y)) return Labyrinthe.MUR;
        else if (this.hero.getX() == x && this.hero.getY() == y) return Labyrinthe.HERO;
        return Labyrinthe.VIDE;
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

    /**
     * Calcule la position suivante a partir d'une position (x, y) et d'une direction.
     *
     * @param x la colonne actuelle
     * @param y la ligne actuelle
     * @param action la direction du deplacement
     * @return un tableau {nouvelleColonne, nouvelleLigne} apres deplacement
     */
    public static int[] getSuivant(int x, int y, String action) {
        switch (action) {
            case HAUT -> y--;
            case BAS -> y++;
            case GAUCHE -> x--;
            case DROITE -> x++;
        }
        return new int[] {x, y};
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
     * Deplace le hero dans la direction indiquee.
     *
     * @param action la direction du deplacement
     * @throws ActionInconnueException si le deplacement est impossible
     */
    public void deplacerHero(String action) throws ActionInconnueException {
        int[] coord = getSuivant(hero.getX(), hero.getY(), action);
        verifierDeplacement(coord[0], coord[1], action);

        switch (this.getChar(coord[0], coord[1])) {
            case Labyrinthe.VIDE -> this.hero.setPos(coord[0], coord[1]);
            case Labyrinthe.MUR -> throw new ActionInconnueException("Vous ne pouvez pas vous déplacer dans cette direction : " + action);
        }
    }
}