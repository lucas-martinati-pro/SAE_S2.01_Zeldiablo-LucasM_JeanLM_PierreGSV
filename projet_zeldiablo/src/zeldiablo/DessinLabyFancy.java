package zeldiablo;

import moteurJeu.DessinJeu;
import zeldiablo.entite.Aventurier;
import zeldiablo.entite.Personnage;
import zeldiablo.environnement.Case;
import zeldiablo.environnement.Piege;
import zeldiablo.item.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gere le dessin du labyrinthe et des entites du jeu.
 * Architecture refondue pour séparer la logique visuelle du rendu.
 */
public class DessinLabyFancy implements DessinJeu {
    public static final int TAILLE = 30;
    private Jeu jeu;

    // --- ASSETS (Sprites & Fonts) ---
    private static final BufferedImage flamme = chargerImage("flamme.png");
    private static final BufferedImage gameOver = chargerImage("gameOver.jpg");
    private static final BufferedImage win = chargerImage("win.png");
    private static final BufferedImage porte = chargerImage("porte.png");
    private static final BufferedImage porteOuverte = chargerImage("porteOuverte.png");
    private static final BufferedImage mur = chargerImage("mur.png");
    private static final BufferedImage vide = chargerImage("vide.png");
    private static final BufferedImage soins = chargerImage("soins.png");
    private static final BufferedImage soinsDetruit = chargerImage("soinsDetruit.png");
    private static final BufferedImage piegeCache = chargerImage("piegeCache.png");
    private static final BufferedImage piege = chargerImage("piege.png");
    private static final BufferedImage piegeDetruit = chargerImage("piegeDetruit.png");
    private static final BufferedImage teleporteur = chargerImage("teleporteur.png");
    private static final BufferedImage teleporteurDetruit = chargerImage("teleporteurDetruit.png");
    private static final BufferedImage murFriable = chargerImage("murFriable.png");
    private static final BufferedImage bombe = chargerImage("bombe.png");
    private static final BufferedImage amulette = chargerImage("amulette.png");
    private static final BufferedImage spider = chargerImage("spider.png");
    private static final BufferedImage troll = chargerImage("troll.png");
    private static final BufferedImage ghost = chargerImage("ghost.png");
    private static final BufferedImage blob = chargerImage("blob.png");
    private static final BufferedImage artificier = chargerImage("artificier.png");
    private static final BufferedImage hero = chargerImage("hero.png");
    private static final BufferedImage spiderAttaque = chargerImage("spiderAttaque.png");
    private static final BufferedImage trollAttaque = chargerImage("trollAttaque.png");
    private static final BufferedImage ghostAttaque = chargerImage("ghostAttaque.png");
    private static final BufferedImage blobAttaque = chargerImage("blobAttaque.png");
    private static final BufferedImage artificierAttaque = chargerImage("artificierAttaque.png");
    private static final BufferedImage heroAttaque = chargerImage("heroAttaque.png");
    private static final BufferedImage coeur = chargerImage("coeur.png");

    private final List<FloatingText> floatingTexts = new ArrayList<>();
    private final List<Decal> decals = new ArrayList<>();
    private final HashMap<Personnage, Integer> lastMonsterHealth = new HashMap<>();

    private final Font fontWatermark = new Font("SansSerif", Font.PLAIN, 10);
    private final Font fontLevel = new Font("SansSerif", Font.BOLD, 11);

    // --- ETAT VISUEL (Visual State) ---
    private final HashMap<Object, VisualEntity> visualEntities = new HashMap<>();
    private final List<Particle> particles = new ArrayList<>();
    private final List<int[]> lastExplosions = new ArrayList<>();

    private double shakeIntensity = 0.0;
    private double shakeX = 0, shakeY = 0;
    private double fadeTimer = 1.0;
    private double gameOverFade = 0.0;
    private double winFade = 0.0;
    private int lastFinX = -1, lastFinY = -1;
    private long frameCount = 0;
    private int lastHeroVie = -1;
    private double damageFlash = 0.0;
    private BufferedImage lightmap;

    public DessinLabyFancy(Jeu jeu) {
        this.jeu = (jeu != null) ? jeu : new Jeu();
    }

    @Override
    public void dessiner(BufferedImage image) {
        Graphics2D g = setupGraphics(image);

        // Création d'une liste isolée pour cette frame spécifique
        List<Case> lightSources = new ArrayList<>();

        updateVisualState();

        g.translate(shakeX, shakeY);

        // On passe la liste aux méthodes
        renderBackgroundAndMap(g, jeu.getSize()[0], jeu.getSize()[1], lightSources);
        renderEntities(g);
        renderParticles(g);
        renderLightingAndVignette(image, g, lightSources);

        g.setFont(fontLevel);
        for (FloatingText txt : new ArrayList<>(floatingTexts)) {
            txt.draw(g);
        }

        g.translate(-shakeX, -shakeY);
        renderUI(image, g);

        g.dispose();
    }

    // ==========================================
    // MÉTHODES DE MISE À JOUR (LOGIQUE VISUELLE)
    // ==========================================

    private void updateVisualState() {
        frameCount++;
        Aventurier heroInstance = jeu.getHero();
        int[] currentFin = jeu.getFin();

        // Gestion des transitions de niveau
        if (currentFin != null && (lastFinX != currentFin[0] || lastFinY != currentFin[1])) {
            lastFinX = currentFin[0]; lastFinY = currentFin[1]; fadeTimer = 1.0;
            decals.clear();
            floatingTexts.clear();
            lastMonsterHealth.clear();
            visualEntities.clear();
            particles.clear();
        }
        if (fadeTimer > 0.0) fadeTimer = Math.max(0, fadeTimer - 0.05);

        // Screen Shake
        if (shakeIntensity > 0.1) {
            shakeX = (Math.random() - 0.5) * shakeIntensity; shakeY = (Math.random() - 0.5) * shakeIntensity; shakeIntensity *= 0.85;
        } else { shakeX = 0; shakeY = 0; shakeIntensity = 0.0; }

        // --- INTERPOLATION & TÉLÉPORTATION DU HÉROS ---
        VisualEntity vHero = visualEntities.computeIfAbsent(heroInstance, ent -> new VisualEntity(heroInstance.getX(), heroInstance.getY()));
        double oldHX = vHero.visualX, oldHY = vHero.visualY;
        if (vHero.update(heroInstance.getX(), heroInstance.getY())) {
            // S'il a sauté de plus de 2 cases, on déclenche l'animation de TP !
            triggerTeleportParticles(oldHX * TAILLE, oldHY * TAILLE, vHero.visualX * TAILLE, vHero.visualY * TAILLE);
            shakeIntensity = 8.0; 
        }

        // Poussière en marchant
        if (vHero.isWalking() && Math.random() < 0.2) {
            particles.add(new Particle(vHero.visualX * TAILLE + TAILLE / 2.0 + (Math.random() - 0.5) * 10, vHero.visualY * TAILLE + TAILLE - 4, (Math.random() - 0.5) * 0.5, -Math.random() * 0.3, new Color(139, 115, 85), 3 + Math.random() * 3, 10 + (int)(Math.random() * 10), false));
        }

        // --- GESTION DES DEGATS ET DES MORTS ---
        // 1. Le Héros
        if (lastHeroVie >= 0 && heroInstance.getVie() < lastHeroVie) {
            int degats = lastHeroVie - heroInstance.getVie();
            floatingTexts.add(new FloatingText(vHero.visualX * TAILLE + 10, vHero.visualY * TAILLE, "-" + degats, new Color(255, 50, 50)));
            spawnSplatter(vHero.visualX * TAILLE + TAILLE/2.0, vHero.visualY * TAILLE + TAILLE, new Color(200, 0, 0, 150));
            damageFlash = 1.0; shakeIntensity = Math.max(shakeIntensity, 6.0);
        }
        lastHeroVie = heroInstance.getVie();

        // 2. Les Monstres (On vérifie la liste de ce qu'on connaissait à la frame d'avant !)
        List<Personnage> monstresActuels = new ArrayList<>(jeu.getMonstres());
        
        for (Personnage m : new ArrayList<>(lastMonsterHealth.keySet())) {
            VisualEntity vM = visualEntities.get(m);
            if (vM == null) continue;

            if (!monstresActuels.contains(m)) {
                // LE MONSTRE EST MORT ! (Il a été retiré du jeu)
                int degatsFatals = lastMonsterHealth.get(m);
                floatingTexts.add(new FloatingText(vM.visualX * TAILLE + 10, vM.visualY * TAILLE, "-" + degatsFatals + "!", new Color(255, 200, 50)));
                Color bloodColor = m.getType() != null && m.getType().equals("Blob") ? new Color(50, 255, 100, 180) : new Color(150, 0, 0, 180);
                spawnSplatter(vM.visualX * TAILLE + TAILLE/2.0, vM.visualY * TAILLE + TAILLE, bloodColor);
                spawnSplatter(vM.visualX * TAILLE + TAILLE/2.0 + 5, vM.visualY * TAILLE + TAILLE - 5, bloodColor); // Extra sang pour la mort
                lastMonsterHealth.remove(m); // On l'oublie
            } else {
                // Il est vivant, on le met à jour
                vM.update(m.getX(), m.getY());
                int currentVie = m.getVie();
                int lastVie = lastMonsterHealth.get(m);
                if (currentVie < lastVie) {
                    floatingTexts.add(new FloatingText(vM.visualX * TAILLE + 10, vM.visualY * TAILLE, "-" + (lastVie - currentVie), new Color(255, 200, 50)));
                    Color bloodColor = m.getType() != null && m.getType().equals("Blob") ? new Color(50, 255, 100, 150) : new Color(150, 0, 0, 150);
                    spawnSplatter(vM.visualX * TAILLE + TAILLE/2.0, vM.visualY * TAILLE + TAILLE, bloodColor);
                }
                lastMonsterHealth.put(m, currentVie);
            }
        }
        
        // 3. Nouveaux monstres qui viennent d'apparaître
        for (Personnage m : monstresActuels) {
            if (!lastMonsterHealth.containsKey(m)) {
                lastMonsterHealth.put(m, m.getVie());
                visualEntities.putIfAbsent(m, new VisualEntity(m.getX(), m.getY()));
            }
        }

        visualEntities.keySet().removeIf(key -> key != heroInstance && !monstresActuels.contains(key) && !lastMonsterHealth.containsKey(key));
        floatingTexts.removeIf(txt -> !txt.update());
        handleExplosionsAndParticles(heroInstance, currentFin);
    }
    
    private void spawnSplatter(double cx, double cy, Color color) {
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * Math.PI * 2;
            double dist = Math.random() * 12;
            decals.add(new Decal(cx + Math.cos(angle)*dist - 3, cy + Math.sin(angle)*dist - 3, color, 3 + Math.random() * 4));
        }
    }

    private void updateEntity(Personnage p) {
        VisualEntity vEntity = visualEntities.computeIfAbsent(p, ent -> new VisualEntity(p.getX(), p.getY()));
        vEntity.update(p.getX(), p.getY());

        // Particules de poussière pour le héros en mouvement
        if (p instanceof Aventurier && vEntity.isWalking() && Math.random() < 0.2) {
            double px = vEntity.visualX * TAILLE + TAILLE / 2.0 + (Math.random() - 0.5) * 10;
            double py = vEntity.visualY * TAILLE + TAILLE - 4;
            particles.add(new Particle(px, py, (Math.random() - 0.5) * 0.5, -Math.random() * 0.3, new Color(139, 115, 85), 3 + Math.random() * 3, 10 + (int)(Math.random() * 10), false));
        }
    }

    private void handleExplosionsAndParticles(Aventurier heroInstance, int[] fin) {
        List<int[]> currentExplosions = new ArrayList<>(jeu.getExplosionAffichage());

        for (int[] coord : currentExplosions) {
            boolean isNew = lastExplosions.stream().noneMatch(last -> last[0] == coord[0] && last[1] == coord[1]);
            if (isNew) {
                triggerExplosionParticles(coord[0], coord[1]);
                shakeIntensity = 12.0;
            }
        }
        lastExplosions.clear();
        lastExplosions.addAll(currentExplosions);

        // Étincelles de la porte de fin
        if (fin != null && heroInstance.haveItem("Amulette") && Math.random() < 0.2) {
            double px = fin[0] * TAILLE + Math.random() * TAILLE;
            double py = fin[1] * TAILLE + Math.random() * TAILLE;
            particles.add(new Particle(px, py, (Math.random()-0.5)*0.3, -Math.random()*0.5-0.2, new Color(100, 255, 150), 4+Math.random()*4, 22+(int)(Math.random()*15), true));
        }

        // Mise à jour de toutes les particules
        particles.removeIf(p -> !p.update());
    }

    // ==========================================
    // PIPELINE DE RENDU
    // ==========================================

    private void renderBackgroundAndMap(Graphics2D g, int sizeX, int sizeY, List<Case> lightSources) {
        Case[][] casesGrid = jeu.getCases();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                g.drawImage(vide, i * TAILLE, j * TAILLE, TAILLE, TAILLE, null);

                Case c = getCaseFromGrid(casesGrid, i, j);
                if (c == null) continue;

                if (c.getCaseSousJacente() != null) drawCaseSprite(g, c.getCaseSousJacente());
                drawCaseSprite(g, c);

                renderWallShadows(g, casesGrid, c, i, j, sizeY);

                // On transmet la liste ici aussi
                handleLightSourceCase(c, i, j, lightSources);
            }
        }

        // Dessin des traces persistantes (Decals) sur le sol
        for (Decal d : new ArrayList<>(decals)) {
            d.draw(g);
        }

        int[] fin = jeu.getFin();
        if (fin != null) {
            BufferedImage porteImg = jeu.getHero().haveItem("Amulette") ? porteOuverte : porte;
            g.drawImage(porteImg, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
        }
    }

    private void renderEntities(Graphics2D g) {
        // Monstres
        for (Personnage m : new ArrayList<>(jeu.getMonstres())) {
            VisualEntity vM = visualEntities.get(m);
            if (vM == null) continue;
            drawSingleEntity(g, m, vM, getMonsterSprite(m));
        }

        // Explosions
        for (int[] coord : new ArrayList<>(jeu.getExplosionAffichage())) {
            g.drawImage(flamme, coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // Héros
        Aventurier heroInstance = jeu.getHero();
        VisualEntity vHero = visualEntities.get(heroInstance);
        if (vHero != null) {
            BufferedImage heroImg = heroInstance.getIsAttaque() ? heroAttaque : hero;
            drawSingleEntity(g, heroInstance, vHero, heroImg);
        }

        // Textes de dégâts flottants
        g.setFont(fontLevel);
        for (FloatingText txt : floatingTexts) {
            txt.draw(g);
        }
    }

    private void renderParticles(Graphics2D g) {
        for (Particle p : particles) p.draw(g);
    }

    private void renderLightingAndVignette(BufferedImage image, Graphics2D g, List<Case> lightSources) {
        if (lightmap == null || lightmap.getWidth() != image.getWidth() || lightmap.getHeight() != image.getHeight()) {
            lightmap = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D lg = lightmap.createGraphics();
        lg.setComposite(AlphaComposite.Clear);
        lg.fillRect(0, 0, lightmap.getWidth(), lightmap.getHeight());
        lg.setComposite(AlphaComposite.SrcOver);
        lg.setColor(new Color(15, 12, 28, 215)); // Obscurité ambiante
        lg.fillRect(0, 0, lightmap.getWidth(), lightmap.getHeight());
        lg.setComposite(AlphaComposite.DstOut);

        // Lumière du Héros (Torche)
        VisualEntity vHero = visualEntities.get(jeu.getHero());
        if (vHero != null) {
            double hX = vHero.visualX * TAILLE + TAILLE / 2.0;
            double hY = vHero.visualY * TAILLE + TAILLE / 2.0;
            double flicker = 1.0 + 0.08 * Math.sin(frameCount * 0.15) + 0.05 * Math.sin(frameCount * 0.37);
            drawLightHole(lg, hX, hY, (int)(135 * flicker), 1.0f);
            drawColoredGlow(g, hX, hY, (int)(60 * flicker), new Color(255, 200, 100, 25));
        }

        // Lumières environnementales qui utilise maintenant la liste passée en paramètre
        for (Case c : lightSources) {
            double cx = c.getX() * TAILLE + TAILLE / 2.0;
            double cy = c.getY() * TAILLE + TAILLE / 2.0;
            switch (c.getType()) {
                case "Teleporteur" -> applyLightAndGlow(lg, g, cx, cy, 90, 0.8f, 85, new Color(0, 180, 255, 45), 0.7);
                case "Amulette" -> applyLightAndGlow(lg, g, cx, cy, 75, 0.7f, 70, new Color(255, 215, 50, 40), 0.07);
                case "Bombe" -> drawLightHole(lg, cx, cy, 60, 0.6f);
                case "Soins" -> applyLightAndGlow(lg, g, cx, cy, 55, 0.5f, 55, new Color(50, 255, 120, 30), 1.0);
            }
        }

        // Explosions & Fin
        int[] fin = jeu.getFin();
        if (fin != null) {
            double doorPulse = 0.7 + 0.3 * Math.sin(frameCount * 0.04);
            Color doorColor = jeu.getHero().haveItem("Amulette") ? new Color(50, 255, 100, 40) : new Color(200, 150, 50, 30);
            drawLightHole(lg, fin[0] * TAILLE + TAILLE / 2.0, fin[1] * TAILLE + TAILLE / 2.0, 70, 0.6f);
            drawColoredGlow(g, fin[0] * TAILLE + TAILLE / 2.0, fin[1] * TAILLE + TAILLE / 2.0, (int)(65 * doorPulse), doorColor);
        }

        for (int[] coord : new ArrayList<>(jeu.getExplosionAffichage())) {
            drawLightHole(lg, coord[0] * TAILLE + TAILLE / 2.0, coord[1] * TAILLE + TAILLE / 2.0, 110, 0.9f);
            drawColoredGlow(g, coord[0] * TAILLE + TAILLE / 2.0, coord[1] * TAILLE + TAILLE / 2.0, 100, new Color(255, 100, 0, 45));
        }

        lg.dispose();
        g.drawImage(lightmap, 0, 0, null);

        // Effet Vignette
        RadialGradientPaint vigPaint = new RadialGradientPaint(
                (float)(image.getWidth() / 2.0), (float)(image.getHeight() / 2.0),
                (float)(Math.max(image.getWidth(), image.getHeight()) / 1.5),
                new float[]{0.0f, 0.6f, 1.0f},
                new Color[]{new Color(0,0,0,0), new Color(0,0,0,60), new Color(0,0,0,180)}
        );
        g.setPaint(vigPaint);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }

    private void renderUI(BufferedImage image, Graphics2D g) {
        Aventurier heroInstance = jeu.getHero();

        // 1. HUD: Cœurs
        int maxVie = heroInstance.getVie();
        if (maxVie > 0) {
            int hudWidth = maxVie * (TAILLE - 7) + 14;
            drawUIBox(g, image.getWidth() - hudWidth - 8, 4, hudWidth, TAILLE - 4);
            for (int i = 0; i < maxVie; i++) {
                double pulse = Math.sin(System.currentTimeMillis() * 0.007 - i * 0.5) * 0.1;
                int hw = (int) ((TAILLE - 10) * (1.0 + pulse));
                int hh = (int) ((TAILLE - 10) * (1.0 + pulse));
                g.drawImage(coeur, image.getWidth() - (i + 1) * (TAILLE - 7) - 14 - (hw - (TAILLE - 10)) / 2, 7 - (hh - (TAILLE - 10)) / 2, hw, hh, null);
            }
        }

        // 2. HUD: Inventaire
        List<Item> inventaire = new ArrayList<>(heroInstance.getInventaire());
        drawUIBox(g, 8, 4, 3 * 24 + 10, TAILLE - 4);
        for (int i = 0; i < 3; i++) {
            g.setColor(new Color(255, 255, 255, 12));
            g.fillRoundRect(13 + i * 24, 7, 20, 20, 5, 5);
            g.setColor(new Color(255, 255, 255, 25));
            g.drawRoundRect(13 + i * 24, 7, 20, 20, 5, 5);
            if (i < inventaire.size() && inventaire.get(i).getType().equals("Amulette")) {
                g.drawImage(amulette, 15 + i * 24, 9, 16, 16, null);
            }
        }

        // 3. Effet Dégâts & Transitions
        if (damageFlash > 0.01) {
            g.setColor(new Color(255, 0, 0, (int)(100 * damageFlash)));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            damageFlash *= 0.88;
        }
        if (fadeTimer > 0.0) {
            g.setColor(new Color(0, 0, 0, (int)(255 * fadeTimer)));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

        // 4. Écrans de fin et Filigrane
        handleEndScreens(image, g, heroInstance);
    }

    // ==========================================
    // MÉTHODES UTILITAIRES (Helpers)
    // ==========================================

    private Graphics2D setupGraphics(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        return g;
    }

    private void drawSingleEntity(Graphics2D g, Personnage p, VisualEntity v, BufferedImage img) {
        double drawX = v.visualX * TAILLE;
        double drawY = v.visualY * TAILLE + (v.bobTimer > 0 ? Math.abs(Math.sin(v.bobTimer)) * -4 : Math.sin(frameCount * 0.06 + p.hashCode()) * 1.5);

        drawEntityShadow(g, drawX, drawY);

        if (!v.faceRight) {
            g.drawImage(img, (int) drawX + TAILLE, (int) drawY, -TAILLE, TAILLE, null);
        } else {
            g.drawImage(img, (int) drawX, (int) drawY, TAILLE, TAILLE, null);
        }
    }

    private void drawEntityShadow(Graphics2D g, double x, double y) {
        Graphics2D g2 = (Graphics2D) g.create();
        float rx = TAILLE / 2.2f;
        g2.setPaint(new RadialGradientPaint(0f, 0f, rx, new float[]{0.0f, 1.0f}, new Color[]{new Color(10, 8, 20, 110), new Color(0, 0, 0, 0)}));
        g2.translate(x + TAILLE / 2.0, y + TAILLE - 2);
        g2.scale(1.0, 0.35);
        g2.fillOval((int)-rx, (int)-rx, (int)(rx * 2), (int)(rx * 2));
        g2.dispose();
    }

    private void drawCaseSprite(Graphics2D g, Case c) {
        BufferedImage img = switch (c.getType()) {
            case "Mur" -> mur;
            case "Soins" -> soins;
            case "SoinsDetruit" -> soinsDetruit;
            case "Piege" -> ((Piege) c).getIsRevele() ? piege : piegeCache;
            case "PiegeDetruit" -> piegeDetruit;
            case "MurFriable" -> murFriable;
            case "Bombe" -> bombe;
            case "Amulette" -> amulette;
            case "Teleporteur" -> teleporteur;
            case "TeleporteurDetruit" -> teleporteurDetruit;
            default -> vide;
        };
        g.drawImage(img, c.getX() * TAILLE, c.getY() * TAILLE, TAILLE, TAILLE, null);
    }

    private void renderWallShadows(Graphics2D g, Case[][] grid, Case c, int i, int j, int sizeY) {
        boolean isWall = c.getType().equals("Mur") || c.getType().equals("MurFriable");
        if (isWall && j < sizeY - 1) {
            Case below = getCaseFromGrid(grid, i, j + 1);
            if (below == null || (!below.getType().equals("Mur") && !below.getType().equals("MurFriable"))) {
                g.setPaint(new GradientPaint(i * TAILLE, (j + 1) * TAILLE, new Color(0, 0, 0, 120), i * TAILLE, (j + 1) * TAILLE + 12, new Color(0, 0, 0, 0)));
                g.fillRect(i * TAILLE, (j + 1) * TAILLE, TAILLE, 12);
            }
        } else if (!isWall) {
            for (int[] d : new int[][]{{-1,0},{1,0},{0,-1},{0,1}}) {
                Case adj = getCaseFromGrid(grid, i + d[0], j + d[1]);
                if (adj != null && (adj.getType().equals("Mur") || adj.getType().equals("MurFriable"))) {
                    g.setColor(new Color(15, 12, 28, 30));
                    g.fillRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE);
                    break;
                }
            }
        }
    }

    private void handleLightSourceCase(Case c, int i, int j, List<Case> lightSources) {
        String type = c.getType();
        if (type.equals("Teleporteur") || type.equals("Soins") || type.equals("Amulette") || type.equals("Bombe")) {
            lightSources.add(c); // Ajout sécurisé à la liste locale

            if (Math.random() < 0.1) {
                Color pCol = type.equals("Teleporteur") ? new Color(100, 220, 255) : (type.equals("Soins") ? new Color(50, 255, 120) : new Color(255, 215, 50));
                particles.add(new Particle(i * TAILLE + Math.random() * TAILLE, j * TAILLE + Math.random() * TAILLE, (Math.random()-0.5)*0.3, -Math.random()*0.4-0.1, pCol, 3+Math.random()*4, 15+(int)(Math.random()*10), true));
            }
        }
    }

    private void drawUIBox(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(new Color(15, 12, 28, 180));
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setColor(new Color(255, 255, 255, 30));
        g.drawRoundRect(x, y, w, h, 10, 10);
    }

    private void handleEndScreens(BufferedImage image, Graphics2D g, Aventurier hero) {
        if (hero.etreMort()) {
            gameOverFade = Math.min(1.0, gameOverFade + 0.025);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)gameOverFade));
            g.drawImage(gameOver, 0, 0, image.getWidth(), image.getHeight(), null);
        } else if (jeu.etreFini()) {
            winFade = Math.min(1.0, winFade + 0.025);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)winFade));
            g.drawImage(win, 0, 0, image.getWidth(), image.getHeight(), null);
        }
    }

    // [Toutes les méthodes mathématiques/graphiques simplifiées (drawLightHole, getCaseFromGrid, etc.) vont ici]
    private Case getCaseFromGrid(Case[][] grid, int x, int y) {
        return (grid != null && x >= 0 && y >= 0 && x < grid.length && y < grid[x].length) ? grid[x][y] : null;
    }

    private BufferedImage getMonsterSprite(Personnage m) {
        if (m.getIsAttaque()) {
            return switch (m.getType()) { case "Spider"->spiderAttaque; case "Troll"->trollAttaque; case "Ghost"->ghostAttaque; case "Blob"->blobAttaque; case "Artificier"->artificierAttaque; default->null; };
        }
        return switch (m.getType()) { case "Spider"->spider; case "Troll"->troll; case "Ghost"->ghost; case "Blob"->blob; case "Artificier"->artificier; default->null; };
    }

    private void applyLightAndGlow(Graphics2D lg, Graphics2D g, double cx, double cy, int radHole, float intHole, int radGlow, Color colGlow, double speedMod) {
        drawLightHole(lg, cx, cy, radHole, intHole);
        drawColoredGlow(g, cx, cy, (int)(radGlow * (0.8 + 0.2 * Math.sin(frameCount * speedMod))), colGlow);
    }

    private void drawLightHole(Graphics2D lg, double x, double y, double radius, float intensity) {
        if (radius <= 0) return;
        lg.setPaint(new RadialGradientPaint((float)x, (float)y, (float)radius, new float[]{0f, 0.15f, 0.5f, 1f}, new Color[]{new Color(0,0,0,(int)(255*intensity)), new Color(0,0,0,(int)(230*intensity)), new Color(0,0,0,(int)(110*intensity)), new Color(0,0,0,0)}));
        lg.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }

    private void drawColoredGlow(Graphics2D g, double x, double y, double radius, Color color) {
        if (radius <= 0) return;
        Color core = new Color(Math.min(255, color.getRed() + 30), Math.min(255, color.getGreen() + 30), Math.min(255, color.getBlue() + 30), Math.min(255, (int)(color.getAlpha() * 1.5)));
        g.setPaint(new RadialGradientPaint((float)x, (float)y, (float)radius, new float[]{0f, 0.25f, 1f}, new Color[]{core, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)}));
        g.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }

    private void triggerExplosionParticles(int cx, int cy) {
        for (int i = 0; i < 20; i++) {
            double angle = Math.random() * 2.0 * Math.PI, speed = 1.0 + Math.random() * 4.0;
            particles.add(new Particle(cx * TAILLE + TAILLE / 2, cy * TAILLE + TAILLE / 2, Math.cos(angle) * speed, Math.sin(angle) * speed, new Color(255, 100 + (int)(Math.random() * 155), (int)(Math.random() * 50)), 6 + Math.random() * 6, 15 + (int)(Math.random() * 15), false));
        }
    }

    private void triggerTeleportParticles(double oldX, double oldY, double newX, double newY) {
        // 1. DÉSINTÉGRATION (Particules bleues qui s'envolent à l'ancienne position)
        for (int i = 0; i < 30; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double speed = 1.0 + Math.random() * 4.0;
            // vy négatif pour qu'elles montent
            particles.add(new Particle(oldX + TAILLE / 2.0, oldY + TAILLE / 2.0, Math.cos(angle) * speed, -Math.abs(Math.sin(angle) * speed) - 2.0, new Color(0, 200, 255), 4 + Math.random() * 4, 20 + (int)(Math.random() * 15), true));
        }

        // 2. RECONSTITUTION (Particules qui convergent vers la nouvelle position)
        for (int i = 0; i < 30; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double dist = 30 + Math.random() * 40; // Apparaissent loin en cercle
            double startX = newX + TAILLE / 2.0 + Math.cos(angle) * dist;
            double startY = newY + TAILLE / 2.0 + Math.sin(angle) * dist;
            // Vitesses orientées vers le centre pour l'implosion
            double vx = (newX + TAILLE / 2.0 - startX) * 0.12;
            double vy = (newY + TAILLE / 2.0 - startY) * 0.12;
            particles.add(new Particle(startX, startY, vx, vy, new Color(100, 255, 255), 5 + Math.random() * 4, 18, false));
        }
    }

    private static BufferedImage chargerImage(String chemin) {
        try { return ImageIO.read(new File("sprite/" + chemin)); }
        catch (IOException e) { return null; }
    }

    // ==========================================
    // CLASSES INTERNES (Logique encapsulée)
    // ==========================================

    private static class VisualEntity {
        double visualX, visualY, bobTimer = 0;
        boolean faceRight = true;

        VisualEntity(double x, double y) { this.visualX = x; this.visualY = y; }

        boolean update(double targetX, double targetY) {
            boolean teleported = false; // NOUVEAU
            double dx = targetX - visualX, dy = targetY - visualY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > 2.0) {
                visualX = targetX; visualY = targetY;
                teleported = true; // Téléportation détectée !
            } else if (dist < 0.08) {
                visualX = targetX; visualY = targetY;
            } else {
                visualX += dx * 0.55; visualY += dy * 0.55;
            }

            if (isWalking()) {
                bobTimer += 0.25;
                if (Math.abs(dx) > 0.02) faceRight = dx > 0;
            } else bobTimer = 0;
            
            return teleported; // NOUVEAU
        }

        boolean isWalking() { return Math.abs(visualX - Math.round(visualX)) > 0.05 || Math.abs(visualY - Math.round(visualY)) > 0.05; }
    }

    private static class Particle {
        double x, y, vx, vy, size;
        Color color;
        int life, maxLife;
        boolean isSparkle;

        Particle(double x, double y, double vx, double vy, Color c, double s, int l, boolean sparkle) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy; this.color = c; this.size = s; this.maxLife = l; this.life = l; this.isSparkle = sparkle;
        }

        boolean update() {
            x += vx; y += vy;
            if (isSparkle) { vy -= 0.05; vx *= 0.95; } else { vx *= 0.9; vy *= 0.9; }
            return --life > 0;
        }

        void draw(Graphics2D g) {
            double ratio = (double) life / maxLife;
            int s = (int) (size * ratio);
            if (s <= 0) return;
            Composite old = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(ratio * 0.8f)));
            g.setColor(color);
            if (isSparkle) g.fillRect((int)(x - s / 2.0), (int)(y - s / 2.0), s, s);
            else g.fillOval((int)(x - s / 2.0), (int)(y - s / 2.0), s, s);
            g.setComposite(old);
        }
    }

    private static class Decal {
        double x, y, size;
        Color color;
        Decal(double x, double y, Color color, double size) {
            this.x = x; this.y = y; this.color = color; this.size = size;
        }
        void draw(Graphics2D g) {
            g.setColor(color);
            g.fillOval((int)x, (int)y, (int)size, (int)size);
        }
    }

    private static class FloatingText {
        double x, y, vy;
        String text;
        Color color;
        int life, maxLife;

        FloatingText(double x, double y, String text, Color color) {
            this.x = x; this.y = y; this.text = text; this.color = color;
            this.vy = -1.5; // Vitesse de montée
            this.maxLife = 40; this.life = 40;
        }

        boolean update() {
            y += vy;
            vy *= 0.92; // Ralentit en montant
            return --life > 0;
        }

        void draw(Graphics2D g) {
            float alpha = (float) life / maxLife;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.setColor(Color.BLACK);
            g.drawString(text, (int)x + 1, (int)y + 1); // Ombre portée
            g.setColor(color);
            g.drawString(text, (int)x, (int)y);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}