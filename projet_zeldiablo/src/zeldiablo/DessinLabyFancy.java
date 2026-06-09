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

/**
 * Gere le dessin du labyrinthe et des entites du jeu.
 * Note : Ce visuel amélioré (Fancy) a été fait par IA uniquement pour voir le rendu du jeu avec de beaux graphismes.
 */
public class DessinLabyFancy implements DessinJeu {
    public static final int TAILLE = 30;
    private Jeu jeu;

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

    // State maps for visual effects and animations
    private final HashMap<Object, VisualEntity> visualEntities = new HashMap<>();
    private final ArrayList<Particle> particles = new ArrayList<>();
    private final ArrayList<int[]> lastExplosions = new ArrayList<>();

    private double shakeIntensity = 0.0;
    private double fadeTimer = 1.0;
    private double gameOverFade = 0.0;
    private double winFade = 0.0;
    private int lastFinX = -1;
    private int lastFinY = -1;
    private long frameCount = 0;
    private int lastHeroVie = -1;
    private double damageFlash = 0.0;
    private BufferedImage lightmap;
    private final Font fontWatermark = new Font("SansSerif", Font.PLAIN, 10);
    private final Font fontLevel = new Font("SansSerif", Font.BOLD, 11);

    /**
     * Accède rapidement et de façon sûre aux cases de la grille locale.
     */
    private Case getCaseFromGrid(Case[][] grid, int x, int y) {
        if (grid != null && x >= 0 && y >= 0 && x < grid.length && y < grid[x].length) {
            return grid[x][y];
        }
        return null;
    }

    /**
     * Dessine une ombre au sol douce et progressive pour les entités (héros, monstres).
     */
    private void drawEntityShadow(Graphics2D g, double x, double y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float[] fractions = {0.0f, 1.0f};
        Color[] colors = {new Color(10, 8, 20, 110), new Color(0, 0, 0, 0)};

        float rx = TAILLE / 2.2f;
        RadialGradientPaint paint = new RadialGradientPaint(
            0f, 0f, rx, fractions, colors
        );
        g2.setPaint(paint);

        g2.translate(x + TAILLE / 2.0, y + TAILLE - 2);
        g2.scale(1.0, 0.35);
        g2.fillOval((int)-rx, (int)-rx, (int)(rx * 2), (int)(rx * 2));
        g2.dispose();
    }

    /**
     * Constructeur de DessinLaby
     * @param jeu un objet de Type jeu
     */
    public DessinLabyFancy(Jeu jeu) {
        if (jeu != null) this.jeu = jeu;
        else this.jeu = new Jeu();
    }

    /**
     * Dessine tous les éléments du jeu
     * @param image image sur laquelle dessiner
     */
    @Override
    public void dessiner(BufferedImage image) {
        Graphics2D g = (Graphics2D) image.getGraphics();

        // Clear the canvas with solid black to prevent background/white borders showing during camera shake
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Enable high quality rendering hints and pixel-art interpolation for sprites
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int[] coordonnee = jeu.getSize();
        if (coordonnee == null) return;
        int sizeX = coordonnee[0];
        int sizeY = coordonnee[1];

        // Cache cases array locally to avoid method call overhead in loops
        Case[][] casesGrid = jeu.getCases();
        ArrayList<Case> lightCases = new ArrayList<>();

        // 1. Level transition detection & handling
        int[] currentFin = jeu.getFin();
        if (currentFin != null && (lastFinX != currentFin[0] || lastFinY != currentFin[1])) {
            lastFinX = currentFin[0];
            lastFinY = currentFin[1];
            fadeTimer = 1.0; // Reset fade-in
        }
        if (fadeTimer > 0.0) {
            fadeTimer -= 0.05;
            if (fadeTimer < 0.0) fadeTimer = 0.0;
        }

        // 2. Screen shake calculation
        double shakeX = 0;
        double shakeY = 0;
        if (shakeIntensity > 0.1) {
            shakeX = (Math.random() - 0.5) * shakeIntensity;
            shakeY = (Math.random() - 0.5) * shakeIntensity;
            shakeIntensity *= 0.85;
        } else {
            shakeIntensity = 0.0;
        }

        // Apply shake transform
        g.translate(shakeX, shakeY);

        // 3. Update visual entities (lerp and animations)
        Aventurier hero = jeu.getHero();
        VisualEntity vHero = visualEntities.computeIfAbsent(hero, h -> {
            Aventurier av = (Aventurier) h;
            return new VisualEntity(av.getX(), av.getY());
        });
        vHero.update(hero.getX(), hero.getY());

        ArrayList<Personnage> monstres = new ArrayList<>(jeu.getMonstres());
        for (Personnage m : monstres) {
            VisualEntity vM = visualEntities.computeIfAbsent(m, mon -> {
                Personnage p = (Personnage) mon;
                return new VisualEntity(p.getX(), p.getY());
            });
            vM.update(m.getX(), m.getY());
        }
        visualEntities.keySet().removeIf(key -> key != hero && !monstres.contains(key));

        // 4. Particle spawners
        // A. Explosions
        ArrayList<int[]> currentExplosions = new ArrayList<>(jeu.getExplosionAffichage());
        for (int[] coord : currentExplosions) {
            boolean found = false;
            for (int[] last : lastExplosions) {
                if (last[0] == coord[0] && last[1] == coord[1]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                triggerExplosionParticles(coord[0], coord[1]);
                shakeIntensity = 12.0; // Start shake on new explosion
            }
        }
        lastExplosions.clear();
        lastExplosions.addAll(currentExplosions);

        // B. Dynamic door sparkles when open
        int[] fin = jeu.getFin();
        if (fin != null && hero.haveItem("Amulette") && Math.random() < 0.2) {
            double px = fin[0] * TAILLE + Math.random() * TAILLE;
            double py = fin[1] * TAILLE + Math.random() * TAILLE;
            particles.add(new Particle(px, py, (Math.random()-0.5)*0.3, -Math.random()*0.5-0.2, new Color(100, 255, 150), 4+Math.random()*4, 22+(int)(Math.random()*15), true));
        }

        // C. Hero walking dust
        if (Math.abs(vHero.visualX - hero.getX()) > 0.05 || Math.abs(vHero.visualY - hero.getY()) > 0.05) {
            if (Math.random() < 0.2) {
                double px = vHero.visualX * TAILLE + TAILLE / 2.0 + (Math.random() - 0.5) * 10;
                double py = vHero.visualY * TAILLE + TAILLE - 4;
                particles.add(new Particle(px, py, (Math.random() - 0.5) * 0.5, -Math.random() * 0.3, new Color(139, 115, 85), 3 + Math.random() * 3, 10 + (int)(Math.random() * 10), false));
            }
        }

        // D. Damage detection
        int currentVie = hero.getVie();
        if (lastHeroVie >= 0 && currentVie < lastHeroVie) {
            damageFlash = 1.0;
            shakeIntensity = Math.max(shakeIntensity, 6.0);
        }
        lastHeroVie = currentVie;
        frameCount++;

        // 5. Single loop for background floor rendering, case rendering, and sparkling particles
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                // Background floor
                g.drawImage(vide, i * TAILLE, j * TAILLE, TAILLE, TAILLE, null);

                // Case
                Case c = getCaseFromGrid(casesGrid, i, j);
                if (c != null) {
                    if (c.getCaseSousJacente() != null) {
                        drawCase(g, c.getCaseSousJacente());
                    }
                    drawCase(g, c);

                    // Gather light sources and spawn passive sparkles
                    String type = c.getType();
                    if (type.equals("Teleporteur")) {
                        lightCases.add(c);
                        if (Math.random() < 0.15) {
                            double px = i * TAILLE + Math.random() * TAILLE;
                            double py = j * TAILLE + Math.random() * TAILLE;
                            double vx = (Math.random() - 0.5) * 0.4;
                            double vy = -Math.random() * 0.5 - 0.2;
                            particles.add(new Particle(px, py, vx, vy, new Color(100, 220, 255), 4 + Math.random() * 4, 20 + (int)(Math.random() * 20), true));
                        }
                    } else if (type.equals("Soins")) {
                        lightCases.add(c);
                        if (Math.random() < 0.1) {
                            double px = i * TAILLE + Math.random() * TAILLE;
                            double py = j * TAILLE + Math.random() * TAILLE;
                            particles.add(new Particle(px, py, (Math.random()-0.5)*0.3, -Math.random()*0.4-0.1, new Color(50, 255, 120), 3+Math.random()*3, 18+(int)(Math.random()*12), true));
                        }
                    } else if (type.equals("Amulette")) {
                        lightCases.add(c);
                        if (Math.random() < 0.12) {
                            double px = i * TAILLE + Math.random() * TAILLE;
                            double py = j * TAILLE + Math.random() * TAILLE;
                            particles.add(new Particle(px, py, (Math.random()-0.5)*0.3, -Math.random()*0.3-0.1, new Color(255, 215, 50), 3+Math.random()*3, 20+(int)(Math.random()*15), true));
                        }
                    } else if (type.equals("Bombe")) {
                        lightCases.add(c);
                    }
                }
            }
        }

        // 6. Draw end gate
        if (fin != null) {
            if (hero.haveItem("Amulette")) {
                g.drawImage(porteOuverte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
            } else {
                g.drawImage(porte, fin[0] * TAILLE, fin[1] * TAILLE, TAILLE, TAILLE, null);
            }
        }

        // 7. Combined pass for wall drop shadows and ambient occlusion
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Case c = getCaseFromGrid(casesGrid, i, j);
                boolean isWall = c != null && (c.getType().equals("Mur") || c.getType().equals("MurFriable"));

                if (isWall) {
                    // Wall drop shadow
                    if (j < sizeY - 1) {
                        Case below = getCaseFromGrid(casesGrid, i, j + 1);
                        if (below == null || (!below.getType().equals("Mur") && !below.getType().equals("MurFriable"))) {
                            int sy = (j + 1) * TAILLE;
                            int sx = i * TAILLE;
                            GradientPaint shadowPaint = new GradientPaint(
                                sx, sy, new Color(0, 0, 0, 120),
                                sx, sy + 12, new Color(0, 0, 0, 0)
                            );
                            g.setPaint(shadowPaint);
                            g.fillRect(sx, sy, TAILLE, 12);
                        }
                    }
                } else {
                    // Ambient occlusion shade near walls
                    boolean adjWall = false;
                    for (int[] d : new int[][]{{-1,0},{1,0},{0,-1},{0,1}}) {
                        Case adj = getCaseFromGrid(casesGrid, i + d[0], j + d[1]);
                        if (adj != null && (adj.getType().equals("Mur") || adj.getType().equals("MurFriable"))) {
                            adjWall = true;
                            break;
                        }
                    }
                    if (adjWall) {
                        g.setColor(new Color(15, 12, 28, 30));
                        g.fillRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE);
                    }
                }
            }
        }

        // 8. Draw monsters at their visual coordinates
        for (Personnage m : monstres) {
            VisualEntity vM = visualEntities.get(m);
            if (vM == null) continue;

            double mDrawX = vM.visualX * TAILLE;
            double mDrawY = vM.visualY * TAILLE;

            // Idle breathing animation when not walking
            double breathe = Math.sin(frameCount * 0.06 + m.hashCode()) * 1.5;
            if (vM.bobTimer > 0) {
                mDrawY += Math.abs(Math.sin(vM.bobTimer)) * -4;
            } else {
                mDrawY += breathe;
            }

            // Entity soft drop shadow
            drawEntityShadow(g, mDrawX, mDrawY);

            BufferedImage img;
            if (m.getIsAttaque()) {
                img = switch (m.getType()) {
                    case "Spider" -> spiderAttaque;
                    case "Troll" -> trollAttaque;
                    case "Ghost" -> ghostAttaque;
                    case "Blob" -> blobAttaque;
                    case "Artificier" -> artificierAttaque;
                    default -> null;
                };
            } else {
                img = switch (m.getType()) {
                    case "Spider" -> spider;
                    case "Troll" -> troll;
                    case "Ghost" -> ghost;
                    case "Blob" -> blob;
                    case "Artificier" -> artificier;
                    default -> null;
                };
            }

            if (img != null) {
                if (!vM.faceRight) {
                    g.drawImage(img, (int) mDrawX + TAILLE, (int) mDrawY, -TAILLE, TAILLE, null);
                } else {
                    g.drawImage(img, (int) mDrawX, (int) mDrawY, TAILLE, TAILLE, null);
                }
            }
        }

        // 9. Draw flames of explosion
        for (int[] coord : currentExplosions) {
            g.drawImage(flamme, coord[0] * TAILLE, coord[1] * TAILLE, TAILLE, TAILLE, null);
        }

        // 10. Draw hero
        double heroDrawX = vHero.visualX * TAILLE;
        double heroDrawY = vHero.visualY * TAILLE;
        double heroBreathe = Math.sin(frameCount * 0.05) * 1.2;
        if (vHero.bobTimer > 0) {
            heroDrawY += Math.abs(Math.sin(vHero.bobTimer)) * -4;
        } else {
            heroDrawY += heroBreathe;
        }

        // Hero soft drop shadow
        drawEntityShadow(g, heroDrawX, heroDrawY);

        BufferedImage heroImg = hero.getIsAttaque() ? heroAttaque : this.hero;
        if (!vHero.faceRight) {
            g.drawImage(heroImg, (int) heroDrawX + TAILLE, (int) heroDrawY, -TAILLE, TAILLE, null);
        } else {
            g.drawImage(heroImg, (int) heroDrawX, (int) heroDrawY, TAILLE, TAILLE, null);
        }

        // 11. Draw particles (backward loop for safe, fast removals)
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if (!p.update()) {
                particles.remove(i);
            } else {
                p.draw(g);
            }
        }

        // 12. Ambient darkness & Dynamic Lighting Overlay (optimized to reuse lightmap buffer)
        if (lightmap == null || lightmap.getWidth() != image.getWidth() || lightmap.getHeight() != image.getHeight()) {
            lightmap = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D lg = lightmap.createGraphics();
        lg.setComposite(AlphaComposite.Clear);
        lg.fillRect(0, 0, lightmap.getWidth(), lightmap.getHeight());
        lg.setComposite(AlphaComposite.SrcOver);
        lg.setColor(new Color(15, 12, 28, 215)); // Deep blue-black dungeon ambient light
        lg.fillRect(0, 0, lightmap.getWidth(), lightmap.getHeight());
        lg.setComposite(AlphaComposite.DstOut);

        // Draw lights (carve out darkness)
        double hX = vHero.visualX * TAILLE + TAILLE / 2.0;
        double hY = vHero.visualY * TAILLE + TAILLE / 2.0;
        // Flickering torch effect
        double flicker = 1.0 + 0.08 * Math.sin(frameCount * 0.15) + 0.05 * Math.sin(frameCount * 0.37) + 0.03 * Math.cos(frameCount * 0.53);
        drawLightCircle(lg, hX, hY, (int)(135 * flicker), 1.0f);

        // Loop over the gathered small list of light sources (huge performance win over whole-grid traversal)
        for (Case c : lightCases) {
            double cx = c.getX() * TAILLE + TAILLE / 2.0;
            double cy = c.getY() * TAILLE + TAILLE / 2.0;
            switch (c.getType()) {
                case "Teleporteur" -> drawLightCircle(lg, cx, cy, 90, 0.8f);
                case "Amulette" -> drawLightCircle(lg, cx, cy, 75, 0.7f);
                case "Bombe" -> drawLightCircle(lg, cx, cy, 60, 0.6f);
                case "Soins" -> drawLightCircle(lg, cx, cy, 55, 0.5f);
            }
        }

        // Door light
        if (fin != null) {
            drawLightCircle(lg, fin[0] * TAILLE + TAILLE / 2.0, fin[1] * TAILLE + TAILLE / 2.0, 70, 0.6f);
        }
        for (int[] coord : currentExplosions) {
            drawLightCircle(lg, coord[0] * TAILLE + TAILLE / 2.0, coord[1] * TAILLE + TAILLE / 2.0, 110, 0.9f);
        }
        lg.dispose();
        g.drawImage(lightmap, 0, 0, null);

        // 13. Colored Glow Overlay (tints, optimized using gathered light sources)
        for (Case c : lightCases) {
            double cx = c.getX() * TAILLE + TAILLE / 2.0;
            double cy = c.getY() * TAILLE + TAILLE / 2.0;
            int cxX = c.getX();
            int cxY = c.getY();
            switch (c.getType()) {
                case "Teleporteur" -> {
                    double tpPulse = 0.8 + 0.2 * Math.sin(frameCount * 0.05 + cxX * 0.7 + cxY * 1.3);
                    drawColoredGlow(g, cx, cy, (int)(85 * tpPulse), new Color(0, 180, 255, 45));
                }
                case "Amulette" -> {
                    double amPulse = 0.7 + 0.3 * Math.sin(frameCount * 0.07);
                    drawColoredGlow(g, cx, cy, (int)(70 * amPulse), new Color(255, 215, 50, 40));
                }
                case "Soins" -> {
                    double sPulse = 0.8 + 0.2 * Math.sin(frameCount * 0.06 + cxX + cxY);
                    drawColoredGlow(g, cx, cy, (int)(55 * sPulse), new Color(50, 255, 120, 30));
                }
            }
        }
        for (int[] coord : currentExplosions) {
            drawColoredGlow(g, coord[0] * TAILLE + TAILLE / 2.0, coord[1] * TAILLE + TAILLE / 2.0, 100, new Color(255, 100, 0, 45));
        }
        // Door glow
        if (fin != null) {
            double doorPulse = 0.7 + 0.3 * Math.sin(frameCount * 0.04);
            Color doorColor = hero.haveItem("Amulette") ? new Color(50, 255, 100, 40) : new Color(200, 150, 50, 30);
            drawColoredGlow(g, fin[0] * TAILLE + TAILLE / 2.0, fin[1] * TAILLE + TAILLE / 2.0, (int)(65 * doorPulse), doorColor);
        }
        // Hero warm glow
        drawColoredGlow(g, hX, hY, (int)(60 * flicker), new Color(255, 200, 100, 25));

        // 14. Vignette Overlay
        float[] vigFractions = {0.0f, 0.6f, 1.0f};
        Color[] vigColors = {new Color(0, 0, 0, 0), new Color(0, 0, 0, 60), new Color(0, 0, 0, 180)};
        double vigRad = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight()) / 1.7;
        RadialGradientPaint vigPaint = new RadialGradientPaint(
            (float)(image.getWidth() / 2.0), (float)(image.getHeight() / 2.0),
            (float)vigRad, vigFractions, vigColors
        );
        g.setPaint(vigPaint);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Reset shake translation before drawing UI
        g.translate(-shakeX, -shakeY);

        // 15. HUD: Hearts with pulsation & background panel
        int maxVie = hero.getVie();
        int heartIconSize = TAILLE - 10;
        int spacing = TAILLE - 7;
        if (maxVie > 0) {
            int hudWidth = maxVie * spacing + 14;
            g.setColor(new Color(15, 12, 28, 180));
            g.fillRoundRect(image.getWidth() - hudWidth - 8, 4, hudWidth, TAILLE - 4, 10, 10);
            g.setColor(new Color(255, 255, 255, 30));
            g.drawRoundRect(image.getWidth() - hudWidth - 8, 4, hudWidth, TAILLE - 4, 10, 10);

            for (int i = 0; i < maxVie; i++) {
                double pulse = Math.sin(System.currentTimeMillis() * 0.007 - i * 0.5) * 0.1;
                int heartW = (int) (heartIconSize * (1.0 + pulse));
                int heartH = (int) (heartIconSize * (1.0 + pulse));
                int hx = image.getWidth() - (i + 1) * spacing - 14 - (heartW - heartIconSize) / 2;
                int hy = 7 - (heartH - heartIconSize) / 2;
                g.drawImage(coeur, hx, hy, heartW, heartH, null);
            }
        }

        // 16. HUD: Inventory with background panel
        ArrayList<Item> inventaire = new ArrayList<>(hero.getInventaire());
        int numItems = inventaire.size();
        if (numItems > 0) {
            int invWidth = numItems * 22 + 10;
            g.setColor(new Color(15, 12, 28, 180));
            g.fillRoundRect(8, 4, invWidth, TAILLE - 4, 10, 10);
            g.setColor(new Color(255, 255, 255, 30));
            g.drawRoundRect(8, 4, invWidth, TAILLE - 4, 10, 10);

            for (int i = 0; i < numItems; i++) {
                Item o = inventaire.get(i);
                switch (o.getType()) {
                    case "Amulette" -> g.drawImage(amulette, 14 + i * 22, 7, heartIconSize, heartIconSize, null);
                }
            }
        }

        // 17. Damage red flash overlay
        if (damageFlash > 0.01) {
            g.setColor(new Color(255, 0, 0, (int)(100 * damageFlash)));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            damageFlash *= 0.88;
        }

        // 18. Level transitions (Fade from black)
        if (fadeTimer > 0.0) {
            g.setColor(new Color(0, 0, 0, (int)(255 * fadeTimer)));
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

        // 19. GameOver and Win Screen smooth fade-in
        if (hero.etreMort()) {
            gameOverFade = Math.min(1.0, gameOverFade + 0.025);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)gameOverFade));
            g.drawImage(gameOver, 0, 0, image.getWidth(), image.getHeight(), null);
        } else if (jeu.etreFini()) {
            winFade = Math.min(1.0, winFade + 0.025);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)winFade));
            g.drawImage(win, 0, 0, image.getWidth(), image.getHeight(), null);
        } else {
            gameOverFade = 0.0;
            winFade = 0.0;
        }

        // Draw level indicator and IA watermark (only when playing)
        if (!hero.etreMort() && !jeu.etreFini()) {
            // Level indicator
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g.setFont(fontLevel);
            String lvlText = "Niveau : " + jeu.getNiveau();
            FontMetrics lvlf = g.getFontMetrics();
            int lvlW = lvlf.stringWidth(lvlText);
            int lvlH = lvlf.getHeight();
            int lvlX = 8;
            int lvlY = image.getHeight() - 8;

            g.setColor(new Color(15, 12, 28, 180));
            g.fillRoundRect(lvlX, lvlY - lvlH - 2, lvlW + 16, lvlH + 6, 10, 10);
            g.setColor(new Color(255, 255, 255, 30));
            g.drawRoundRect(lvlX, lvlY - lvlH - 2, lvlW + 16, lvlH + 6, 10, 10);

            g.setColor(new Color(220, 220, 240));
            g.drawString(lvlText, lvlX + 8, lvlY - 4);

            // IA watermark
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g.setFont(fontWatermark);
            String text = "Fait par IA - Uniquement pour voir le rendu du jeu avec de beaux graphismes";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();
            int xPos = (image.getWidth() - textWidth) / 2;
            int yPos = image.getHeight() - 6;

            // Draw background pill for readability
            g.setColor(new Color(15, 12, 28, 160));
            g.fillRoundRect(xPos - 8, yPos - textHeight + 2, textWidth + 16, textHeight + 4, 8, 8);
            g.setColor(new Color(255, 255, 255, 40));
            g.drawRoundRect(xPos - 8, yPos - textHeight + 2, textWidth + 16, textHeight + 4, 8, 8);

            // Draw text
            g.setColor(new Color(220, 220, 240));
            g.drawString(text, xPos, yPos - 2);
        }

        g.dispose();
    }

    /**
     * Dessine une case specifique a l'ecran.
     *
     * @param g le contexte graphique 2D
     * @param c la case a dessiner
     */
    public void drawCase(Graphics2D g, Case c) {
        int x = c.getX();
        int y = c.getY();
        BufferedImage img = switch (c.getType()) {
            case "Mur" -> mur;
            case "Soins" -> soins;
            case "SoinsDetruit" -> soinsDetruit;
            case "Piege" -> {
                if (((Piege) c).getIsRevele()) yield piege;
                else yield piegeCache;
            }
            case "PiegeDetruit" -> piegeDetruit;
            case "MurFriable" -> murFriable;
            case "Bombe" -> bombe;
            case "Amulette" -> amulette;
            case "Teleporteur" -> teleporteur;
            case "TeleporteurDetruit" -> teleporteurDetruit;
            default -> vide;
        };
        g.drawImage(img, x * TAILLE, y * TAILLE, TAILLE, TAILLE, null);
    }

    private void drawLightCircle(Graphics2D lg, double x, double y, double radius, float intensity) {
        if (radius <= 0) return;
        float[] fractions = {0.0f, 0.15f, 0.5f, 1.0f};
        Color[] colors = {
            new Color(0, 0, 0, (int)(255 * intensity)),
            new Color(0, 0, 0, (int)(230 * intensity)),
            new Color(0, 0, 0, (int)(110 * intensity)),
            new Color(0, 0, 0, 0)
        };
        RadialGradientPaint paint = new RadialGradientPaint(
            (float) x, (float) y, (float) radius, fractions, colors
        );
        lg.setPaint(paint);
        lg.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }

    private void drawColoredGlow(Graphics2D g, double x, double y, double radius, Color color) {
        if (radius <= 0) return;
        float[] fractions = {0.0f, 0.25f, 1.0f};
        Color coreColor = new Color(
            Math.min(255, color.getRed() + 30),
            Math.min(255, color.getGreen() + 30),
            Math.min(255, color.getBlue() + 30),
            Math.min(255, (int)(color.getAlpha() * 1.5))
        );
        Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        Color[] colors = {coreColor, color, endColor};
        RadialGradientPaint paint = new RadialGradientPaint(
            (float) x, (float) y, (float) radius, fractions, colors
        );
        g.setPaint(paint);
        g.fillOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }

    private void triggerExplosionParticles(int cx, int cy) {
        int centerX = cx * TAILLE + TAILLE / 2;
        int centerY = cy * TAILLE + TAILLE / 2;
        for (int i = 0; i < 20; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            double speed = 1.0 + Math.random() * 4.0;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;

            int r = 255;
            int gr = 100 + (int)(Math.random() * 155);
            int b = (int)(Math.random() * 50);
            Color col = new Color(r, gr, b);

            particles.add(new Particle(centerX, centerY, vx, vy, col, 6 + Math.random() * 6, 15 + (int)(Math.random() * 15), false));
        }
    }

    /**
     * Charge une image a partir d'un chemin specifique.
     *
     * @param chemin le chemin de l'image a charger
     * @return l'image chargee ou null en cas d'erreur
     */
    private static BufferedImage chargerImage(String chemin) {
        try {
            return ImageIO.read(new File("sprite/" + chemin));
        } catch (IOException e) {
            System.err.println("Erreur chargement : " + chemin + " - " + e.getMessage());
            return null; // Ou retourne une image par défaut (placeholder)
        }
    }

    // Helper classes for visual effects
    private static class VisualEntity {
        double visualX;
        double visualY;
        boolean faceRight = true;
        double bobTimer = 0;

        VisualEntity(double x, double y) {
            this.visualX = x;
            this.visualY = y;
        }

        void update(double targetX, double targetY) {
            double dx = targetX - visualX;
            double dy = targetY - visualY;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist > 2.0) {
                // Teleportation / Snapping
                visualX = targetX;
                visualY = targetY;
            } else if (dist < 0.08) {
                // Snap to target when very close (precision)
                visualX = targetX;
                visualY = targetY;
            } else {
                // Fast lerp towards target (snappy movement)
                visualX += dx * 0.55;
                visualY += dy * 0.55;
            }

            // Determine if walking
            boolean isWalking = Math.abs(dx) > 0.03 || Math.abs(dy) > 0.03;
            if (isWalking) {
                bobTimer += 0.25;
                if (Math.abs(dx) > 0.02) {
                    faceRight = dx > 0;
                }
            } else {
                bobTimer = 0; // Reset bobbing
            }
        }
    }

    private static class Particle {
        double x, y;
        double vx, vy;
        Color color;
        double size;
        int life;
        int maxLife;
        boolean isSparkle; // Cyan/blue teleporter sparkles

        Particle(double x, double y, double vx, double vy, Color color, double size, int maxLife, boolean isSparkle) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.size = size;
            this.life = maxLife;
            this.maxLife = maxLife;
            this.isSparkle = isSparkle;
        }

        boolean update() {
            x += vx;
            y += vy;
            if (isSparkle) {
                vy -= 0.05; // Float upwards
                vx *= 0.95;
            } else {
                vx *= 0.9;
                vy *= 0.9;
            }
            life--;
            return life > 0;
        }

        void draw(Graphics2D g) {
            double ratio = (double) life / maxLife;
            int currentSize = (int) (size * ratio);
            if (currentSize <= 0) return;

            Composite oldComp = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(ratio * 0.8f)));
            g.setColor(color);

            if (isSparkle) {
                g.fillRect((int) (x - currentSize / 2.0), (int) (y - currentSize / 2.0), currentSize, currentSize);
            } else {
                g.fillOval((int) (x - currentSize / 2.0), (int) (y - currentSize / 2.0), currentSize, currentSize);
            }
            g.setComposite(oldComp);
        }
    }
}