package zeldiablo;

import zeldiablo.entite.Ghost;
import zeldiablo.Item.Amulette;
import zeldiablo.entite.Spider;
import zeldiablo.entite.Troll;
import zeldiablo.environnement.Mur;
import zeldiablo.entite.Aventurier;
import zeldiablo.environnement.Piege;
import zeldiablo.environnement.MurFriable;
import zeldiablo.exception.FichierIncorrectException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe responsable du chargement d'un niveau depuis un fichier.
 */
public class Chargement {
    /**
     * Charge un jeu a partir d'un fichier texte.
     *
     * @param nomFichier le chemin vers le fichier de labyrinthe
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws IOException si une erreur de lecture survient
     * @throws FichierIncorrectException si le fichier contient des caracteres invalides, si le personnage est absent, ou si le nombre de caisses ne correspond pas au nombre de depots
     */
    public static void chargerNiveau(Jeu jeu, String nomFichier) throws FileNotFoundException, IOException, FichierIncorrectException {
        ArrayList<String> ligne = new ArrayList<>();
        convertLab(nomFichier, ligne);
        Aventurier hero = null;
        int[] fin = null;

        jeu.getCases().clear();
        jeu.getMonstres().clear();

        int max = 0;
        for (String line : ligne) {
            if (line.length() > max) max = line.length();
        }
        jeu.setSize(new int[]{max, ligne.size()});

        for (int i = 0; i < ligne.size(); i++) {
            String line = ligne.get(i);
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case Jeu.MUR -> jeu.getCases().add(new Mur(j, i));
                    case Jeu.HERO -> {
                        hero = new Aventurier(j, i, 5);
                        hero.setJeu(jeu);
                    }
                    case Jeu.FIN -> fin = new int[]{j, i};
                    case Jeu.VIDE -> {}
                    case Jeu.PIEGE -> jeu.getCases().add(new Piege(j, i));
                    case Jeu.MUR_FRIABLE -> jeu.getCases().add(new MurFriable(j, i));
                    case Jeu.AMULETTE -> jeu.getCases().add(new Amulette(j, i));
                    case Jeu.SPIDER -> jeu.getMonstres().add(new Spider(j, i, 3));
                    case Jeu.TROLL -> jeu.getMonstres().add(new Troll(j, i, 1));
                    case Jeu.GHOST -> jeu.getMonstres().add(new Ghost(j, i, 4));
                    default -> throw new FichierIncorrectException("caractère inconnu " + line.charAt(j));
                }
            }
        }

        if (hero == null) throw new FichierIncorrectException("hero inconnu");  // Si il y as 2 personnages, ça prend le dernière
        else if (fin == null) throw new FichierIncorrectException("case de fin inconnue");

        jeu.setHero(hero);
        jeu.setFin(fin);
    }

    /**
     * Convertit un fichier texte en labyrinthe.
     *
     * @param nomFichier nom du fichier
     * @param ligne recupere les lignes lues
     */
    public static void convertLab(String nomFichier, ArrayList<String> ligne) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(nomFichier));

        String currentLine;
        while ((currentLine = file.readLine()) != null) {
            ligne.add(currentLine);
        }
        file.close();
    }
}
