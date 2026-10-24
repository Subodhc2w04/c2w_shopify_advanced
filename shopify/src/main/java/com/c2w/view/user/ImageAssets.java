package com.c2w.view.user;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public final class ImageAssets {
    private static final Map<String, Image> CACHE = new HashMap<>();

    private static final String BASE_RES_DIR = "src/main/resources";
    private static final String ASSETS_DIR = "src/main/resources/assets/images";

    static {
        
        ensureStaticCategoryAssets();
    }

    private ImageAssets() {
    }

    public static Image getImage(String path, double width, double height, String label, String category) {
        String cacheKey = (path != null ? path : "def") + "_" + (int) width + "_" + (int) height + "_"
                + (label != null ? label : "");
        if (CACHE.containsKey(cacheKey)) {
            return CACHE.get(cacheKey);
        }

        if (path != null && !path.isEmpty()) {
            // Support HTTP / HTTPS remote URLs (e.g. from Firebase Storage)
            if (path.startsWith("http://") || path.startsWith("https://")) {
                try {
                    Image webImg = new Image(path, width, height, true, true, true);
                    if (!webImg.isError()) {
                        CACHE.put(cacheKey, webImg);
                        return webImg;
                    }
                } catch (Exception ignored) {
                }
            }

            String clean = path.startsWith("/") ? path.substring(1) : path;
            String[] candidatePaths = {
                    "/assets/images/" + clean,
                    "/recources/assets/images/" + clean,
                    "/" + clean,
                    "/assets/images/"
                            + clean.replace("products/", "").replace("categories/", "").replace("banners/", ""),
                    clean
            };

            for (String p : candidatePaths) {
                try {
                    InputStream is = ImageAssets.class.getResourceAsStream(p.startsWith("/") ? p : "/" + p);
                    if (is != null) {
                        Image img = new Image(is, width, height, true, true);
                        if (!img.isError() && img.getWidth() > 0) {
                            CACHE.put(cacheKey, img);
                            return img;
                        }
                    }
                } catch (Exception ignored) {
                }
            }

            // Also check directly on filesystem if running in IDE/workspace
            String[] filePaths = {
                    "src/main/resources/assets/images/" + clean,
                    "src/main/resources/" + clean,
                    "target/classes/assets/images/" + clean,
                    "target/classes/" + clean
            };
            for (String fp : filePaths) {
                java.io.File file = new java.io.File(fp);
                if (file.exists() && file.isFile()) {
                    try {
                        Image img = new Image(file.toURI().toString(), width, height, true, true);
                        if (!img.isError() && img.getWidth() > 0) {
                            CACHE.put(cacheKey, img);
                            return img;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        Image generated = generateFashionGraphic(width, height, label, category);
        CACHE.put(cacheKey, generated);
        return generated;
    }

   
    public static Image generateFashionGraphic(double width, double height, String label, String category) {
        int w = Math.max((int) width, 80);
        int h = Math.max((int) height, 80);
        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        String l = (label != null ? label : "").toLowerCase();
        String c = (category != null ? category : "").toLowerCase();

        // 1. Studio Backdrop Palette
        javafx.scene.paint.Color bgTop;
        javafx.scene.paint.Color bgBot;
        javafx.scene.paint.Color glowColor;
        javafx.scene.paint.Color accentColor;
        String mainIcon;
        String subType;

        if (c.contains("electronic") || l.contains("headphone") || l.contains("audio") || l.contains("laptop") || l.contains("phone") || l.contains("tablet")) {
            bgTop = Color.web("#141923");
            bgBot = Color.web("#0A0E15");
            glowColor = Color.web("#06B6D4");
            accentColor = Color.web("#38BDF8");
            mainIcon = "🎧";
            subType = "SMART TECH";
        } else if (c.contains("book") || l.contains("book") || l.contains("novel") || l.contains("journal") || l.contains("edition")) {
            bgTop = Color.web("#2B1D20");
            bgBot = Color.web("#170F11");
            glowColor = Color.web("#E11D48");
            accentColor = Color.web("#FDA4AF");
            mainIcon = "📚";
            subType = "LITERARY EDITION";
        } else if (c.contains("shoe") || c.contains("footwear") || l.contains("sneaker") || l.contains("boot") || l.contains("heel") || l.contains("sandal") || l.contains("shoe")) {
            bgTop = Color.web("#1F2525");
            bgBot = Color.web("#101515");
            glowColor = Color.web("#14B8A6");
            accentColor = Color.web("#5EEAD4");
            mainIcon = l.contains("heel") ? "👠" : (l.contains("boot") ? "🥾" : "👟");
            subType = "FOOTWEAR";
        } else if (c.contains("accessori") || l.contains("watch") || l.contains("bag") || l.contains("tote") || l.contains("sunglasses") || l.contains("wallet")) {
            bgTop = Color.web("#2A2219");
            bgBot = Color.web("#18120B");
            glowColor = Color.web("#F59E0B");
            accentColor = Color.web("#FCD34D");
            mainIcon = l.contains("watch") ? "⌚" : (l.contains("sunglasses") ? "🕶️" : "👜");
            subType = "ACCESSORIES";
        } else if (c.contains("cloth") || c.contains("men") || c.contains("women") || c.contains("kid") || l.contains("shirt") || l.contains("jean") || l.contains("jacket") || l.contains("dress") || l.contains("tshirt") || l.contains("pant")) {
            bgTop = Color.web("#1E2230");
            bgBot = Color.web("#131620");
            glowColor = Color.web("#3B82F6");
            accentColor = Color.web("#60A5FA");
            mainIcon = l.contains("dress") ? "👗" : (l.contains("jeans") ? "👖" : (l.contains("jacket") ? "🧥" : "👔"));
            subType = "CURATED APPAREL";
        } else {
            bgTop = Color.web("#212028");
            bgBot = Color.web("#121117");
            glowColor = Color.web("#FF3E6C");
            accentColor = Color.web("#FF85A1");
            mainIcon = "✦";
            subType = "COLLECTION";
        }

        // 2. Render Gradient Background
        LinearGradient bgGrad = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, bgTop),
                new Stop(1.0, bgBot));
        gc.setFill(bgGrad);
        gc.fillRect(0, 0, w, h);

        // 3. Central Ambient Radial Studio Light
        RadialGradient glow = new RadialGradient(
                0, 0, w * 0.5, h * 0.48, Math.min(w, h) * 0.46, false, CycleMethod.NO_CYCLE,
                new Stop(0.0,
                        Color.rgb((int) (glowColor.getRed() * 255), (int) (glowColor.getGreen() * 255),
                                (int) (glowColor.getBlue() * 255), 0.28)),
                new Stop(0.7,
                        Color.rgb((int) (glowColor.getRed() * 255), (int) (glowColor.getGreen() * 255),
                                (int) (glowColor.getBlue() * 255), 0.08)),
                new Stop(1.0, Color.TRANSPARENT));
        gc.setFill(glow);
        gc.fillOval(w * 0.1, h * 0.08, w * 0.8, h * 0.8);

        // 4. Subtle Architectural Geometric Frames
        gc.setStroke(Color.rgb(255, 255, 255, 0.07));
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(w * 0.06, h * 0.06, w * 0.88, h * 0.88, 12, 12);

        // Circular luxury spotlight ring
        gc.setStroke(Color.rgb(255, 255, 255, 0.12));
        gc.strokeOval(w * 0.2, h * 0.18, w * 0.6, w * 0.6);

        // 5. Stylized Center Piece Icon / Motif
        double iconSize = Math.min(w, h) * 0.36;
        gc.setFont(Font.font("Segoe UI Emoji", FontWeight.NORMAL, iconSize));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(mainIcon, w * 0.5, h * 0.54);

        // 6. Subtle Editorial Subtitle Pill
        double pillW = Math.min(w * 0.64, 140);
        double pillH = 18;
        double pillX = (w - pillW) * 0.5;
        double pillY = h - pillH - 10;

        gc.setFill(Color.rgb(20, 20, 26, 0.85));
        gc.fillRoundRect(pillX, pillY, pillW, pillH, 9, 9);
        gc.setStroke(Color.rgb(255, 255, 255, 0.15));
        gc.setLineWidth(0.8);
        gc.strokeRoundRect(pillX, pillY, pillW, pillH, 9, 9);

        gc.setFill(accentColor);
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 8.5));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(subType, w * 0.5, pillY + 12.5);

        // Top Subtle C2W Watermark
        gc.setFill(Color.rgb(255, 255, 255, 0.25));
        gc.setFont(Font.font("Segoe UI", FontWeight.BOLD, 8));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("C2W STORE", w - 12, 16);

        WritableImage snap = new WritableImage(w, h);
        canvas.snapshot(null, snap);
        return snap;
    }

  
    public static Image generateHeroBanner(double width, double height, String headline, String subheadline) {
        int w = Math.max((int) width, 400);
        int h = Math.max((int) height, 180);
        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        LinearGradient bg = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.web("#111218")),
                new Stop(0.45, Color.web("#1E1524")),
                new Stop(0.85, Color.web("#2E1022")),
                new Stop(1.0, Color.web("#3A132B")));
        gc.setFill(bg);
        gc.fillRect(0, 0, w, h);

        RadialGradient rightGlow = new RadialGradient(
                0, 0, w * 0.85, h * 0.45, Math.min(w, h) * 0.7, false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(255, 62, 108, 0.28)),
                new Stop(0.5, Color.rgb(255, 62, 108, 0.10)),
                new Stop(1.0, Color.TRANSPARENT));
        gc.setFill(rightGlow);
        gc.fillOval(w * 0.5, -h * 0.2, w * 0.6, h * 1.4);

        RadialGradient leftGlow = new RadialGradient(
                0, 0, w * 0.15, h * 0.85, Math.min(w, h) * 0.6, false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(240, 192, 64, 0.16)),
                new Stop(0.5, Color.rgb(240, 192, 64, 0.05)),
                new Stop(1.0, Color.TRANSPARENT));
        gc.setFill(leftGlow);
        gc.fillOval(-w * 0.1, h * 0.3, w * 0.5, h * 1.0);

        gc.setStroke(Color.rgb(255, 255, 255, 0.06));
        gc.setLineWidth(1.5);
        for (int i = 0; i < 4; i++) {
            gc.strokeOval(w * 0.65 + (i * 24), -h * 0.4 + (i * 12), w * 0.55, h * 1.8);
        }

        gc.setFont(Font.font("Segoe UI Emoji", FontWeight.NORMAL, Math.min(w, h) * 0.38));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("✨🛍️✨", w * 0.84, h * 0.56);

        WritableImage snap = new WritableImage(w, h);
        canvas.snapshot(null, snap);
        return snap;
    }

   
    public static synchronized void ensureStaticCategoryAssets() {
        try {
            createCategoryCardFile("categories/clothing.png", "Clothing", "CURATED APPAREL", "👗", "#1E2230", "#10141A", "#3B82F6");
            createCategoryCardFile("categories/accessories.png", "Accessories", "ACCESSORIES", "👜", "#2A2219", "#18120B", "#F59E0B");
            createCategoryCardFile("categories/shoes.png", "Shoes", "FOOTWEAR", "👟", "#1F2525", "#101515", "#14B8A6");
            createCategoryCardFile("categories/electronics.png", "Electronics", "TECH & AUDIO", "🎧", "#151D28", "#0D121B", "#06B6D4");
            createCategoryCardFile("categories/book.png", "Book", "LITERARY EDITIONS", "📚", "#2B1D20", "#170F11", "#E11D48");

            // Also ensure .jpg aliases exist for compatibility
            syncCategoryFile("clothing");
            syncCategoryFile("accessories");
            syncCategoryFile("shoes");
            syncCategoryFile("electronics");
            syncCategoryFile("book");
        } catch (Exception e) {
            System.err.println("ImageAssets - Note: ensureStaticCategoryAssets exception (will fallback to canvas): " + e.getMessage());
        }
    }


    public static void createCategoryCardFile(String relPath, String title, String tag, String icon,
                                             String hex1, String hex2, String accentHex) {
        File f1 = new File(BASE_RES_DIR, relPath);
        File f2 = new File(ASSETS_DIR, relPath);
        if (f1.exists() && f2.exists()) {
            return; // Already generated
        }

        int width = 450;
        int height = 300;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        setupAwtHints(g2);

        java.awt.Color c1 = java.awt.Color.decode(hex1);
        java.awt.Color c2 = java.awt.Color.decode(hex2);
        java.awt.Color accent = java.awt.Color.decode(accentHex);

        // Background Gradient
        GradientPaint gp = new GradientPaint(0, 0, c1, width, height, c2);
        g2.setPaint(gp);
        g2.fill(new Rectangle2D.Double(0, 0, width, height));

        // Ambient glow
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point2D.Double(width * 0.5, height * 0.5),
                (float) (width * 0.45),
                new float[]{0.0f, 0.7f, 1.0f},
                new java.awt.Color[]{
                        new java.awt.Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60),
                        new java.awt.Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 15),
                        new java.awt.Color(0, 0, 0, 0)
                });
        g2.setPaint(rgp);
        g2.fill(new Ellipse2D.Double(width * 0.1, height * 0.05, width * 0.8, height * 0.9));

        // Outer Frame
        g2.setColor(new java.awt.Color(255, 255, 255, 25));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Double(14, 14, width - 28, height - 28, 20, 20));

        // Icon
        g2.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 100));
        drawAwtCenteredText(g2, icon, width * 0.5, height * 0.54);

        // Watermark
        g2.setColor(new java.awt.Color(255, 255, 255, 60));
        g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        g2.drawString("C2W STORE", width - 120, 36);

        g2.dispose();
        saveAwtImage(img, relPath);
    }

    /**
     * Pure Java product card asset generator for static assets.
     */
    public static void createProductImageFile(String relPath, String brand, String tag, String icon,
                                             String hex1, String hex2, String accentHex) {
        int width = 500;
        int height = 500;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        setupAwtHints(g2);

        java.awt.Color c1 = java.awt.Color.decode(hex1);
        java.awt.Color c2 = java.awt.Color.decode(hex2);
        java.awt.Color accent = java.awt.Color.decode(accentHex);

        GradientPaint gp = new GradientPaint(0, 0, c1, 0, height, c2);
        g2.setPaint(gp);
        g2.fill(new Rectangle2D.Double(0, 0, width, height));

        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point2D.Double(width * 0.5, height * 0.48),
                (float) (width * 0.45),
                new float[]{0.0f, 0.6f, 1.0f},
                new java.awt.Color[]{
                        new java.awt.Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 70),
                        new java.awt.Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20),
                        new java.awt.Color(0, 0, 0, 0)
                });
        g2.setPaint(rgp);
        g2.fill(new Ellipse2D.Double(width * 0.1, height * 0.08, width * 0.8, height * 0.8));

        g2.setColor(new java.awt.Color(255, 255, 255, 25));
        g2.setStroke(new BasicStroke(1.5f));
        g2.draw(new RoundRectangle2D.Double(20, 20, width - 40, height - 40, 24, 24));
        g2.draw(new Ellipse2D.Double(width * 0.18, height * 0.16, width * 0.64, height * 0.64));

        g2.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 150));
        drawAwtCenteredText(g2, icon, width * 0.5, height * 0.52);

        int pillW = 200;
        int pillH = 32;
        int pillX = (width - pillW) / 2;
        int pillY = height - pillH - 28;

        g2.setColor(new java.awt.Color(15, 15, 20, 210));
        g2.fill(new RoundRectangle2D.Double(pillX, pillY, pillW, pillH, 16, 16));
        g2.setColor(new java.awt.Color(255, 255, 255, 45));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(pillX, pillY, pillW, pillH, 16, 16));

        g2.setColor(accent);
        g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        drawAwtCenteredText(g2, tag, width * 0.5, pillY + 21);

        g2.setColor(new java.awt.Color(255, 255, 255, 60));
        g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        g2.drawString("C2W STORE", width - 110, 42);

        g2.dispose();
        saveAwtImage(img, relPath);
    }

    private static void setupAwtHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private static void drawAwtCenteredText(Graphics2D g2, String text, double x, double y) {
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(text, g2);
        g2.drawString(text, (float) (x - bounds.getWidth() / 2), (float) (y + bounds.getHeight() / 4));
    }

    private static void saveAwtImage(BufferedImage img, String relPath) {
        try {
            File f1 = new File(ASSETS_DIR, relPath);
            f1.getParentFile().mkdirs();
            ImageIO.write(img, "PNG", f1);

            File f2 = new File(BASE_RES_DIR, relPath);
            f2.getParentFile().mkdirs();
            ImageIO.write(img, "PNG", f2);
        } catch (Exception e) {
            System.err.println("ImageAssets - Failed to save image: " + relPath + ": " + e.getMessage());
        }
    }

    private static void syncCategoryFile(String name) {
        File srcPng = new File(BASE_RES_DIR, "categories/" + name + ".png");
        if (srcPng.exists()) {
            copyFile(srcPng, new File(BASE_RES_DIR, "categories/" + name + ".jpg"));
            copyFile(srcPng, new File(ASSETS_DIR, "categories/" + name + ".png"));
            copyFile(srcPng, new File(ASSETS_DIR, "categories/" + name + ".jpg"));
        }
    }

    private static void copyFile(File src, File dest) {
        try {
            if (src.exists()) {
                dest.getParentFile().mkdirs();
                Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Remote asset downloader utility (formerly in Downloader.java).
     */
    public static void downloadRemoteAsset(String path, String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);

            if (conn.getResponseCode() == 200) {
                File dest1 = new File(BASE_RES_DIR, path);
                File dest2 = new File(ASSETS_DIR, path);
                dest1.getParentFile().mkdirs();
                dest2.getParentFile().mkdirs();

                try (InputStream in = conn.getInputStream()) {
                    Files.copy(in, dest1.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                Files.copy(dest1.toPath(), dest2.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.err.println("ImageAssets - Failed to download asset " + path + ": " + e.getMessage());
        }
    }
}
