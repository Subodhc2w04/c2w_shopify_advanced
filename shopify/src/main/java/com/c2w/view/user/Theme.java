package com.c2w.view.user;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public final class Theme {
    private Theme() {
    }

    // ─── Core Brand Accent ───────────────────────────────────────────────────
    public static final String COLOR_PRIMARY = "#FF3E6C"; // Signature Fashion Coral-Pink
    public static final String COLOR_PRIMARY_HOVER = "#FF5A82"; // Lighter hover state
    public static final String COLOR_PRIMARY_PRESSED = "#D02952";
    public static final String COLOR_PRIMARY_LIGHT = "#3D1520"; // Muted dark-rose tint for dark bg

    // ─── Dark Background Layers ───────────────────────────────────────────────
    public static final String COLOR_BG_PAGE = "#0F0F12"; // Near-black base canvas
    public static final String COLOR_BG_SECTION = "#17171C"; // Slightly lighter section bg
    public static final String COLOR_BG_CARD = "#1E1E26"; // Card surface
    public static final String COLOR_BG_ELEVATED = "#252530"; // Elevated panels / modals
    public static final String COLOR_BG_INPUT = "#1A1A22"; // Text field background

    // ─── Text Colors ─────────────────────────────────────────────────────────
    public static final String COLOR_DARK = "#F0F0F5"; // Primary text (near-white)
    public static final String COLOR_DARK_HOVER = "#FFFFFF";
    public static final String COLOR_SECONDARY_TEXT = "#A8A8B8"; // Secondary text
    public static final String COLOR_MUTED_TEXT = "#68687A"; // Placeholder / muted
    public static final String COLOR_BORDER = "#2E2E3A"; // Subtle border
    public static final String COLOR_BORDER_FOCUS = "#FF3E6C"; // Focus ring

    // ─── Semantic Colors ──────────────────────────────────────────────────────
    public static final String COLOR_SUCCESS = "#00D68F"; // Emerald green
    public static final String COLOR_SUCCESS_LIGHT = "#0D2B22"; // Dark tint background
    public static final String COLOR_WARNING = "#FF905A"; // Warm amber
    public static final String COLOR_GOLD = "#F0C040"; // VIP Gold
    public static final String COLOR_DISCOUNT = "#FF3E6C";

    // ─── JavaFX Paint Colors ─────────────────────────────────────────────────
    public static final Color PAINT_PRIMARY = Color.web(COLOR_PRIMARY);
    public static final Color PAINT_DARK = Color.web(COLOR_DARK);
    public static final Color PAINT_MUTED = Color.web(COLOR_MUTED_TEXT);
    public static final Color PAINT_SUCCESS = Color.web(COLOR_SUCCESS);
    public static final Color PAINT_BG_PAGE = Color.web(COLOR_BG_PAGE);

    // ─── Font Helpers ─────────────────────────────────────────────────────────
    public static Font font(double size, FontWeight weight) {
        return Font.font("Segoe UI", weight, size);
    }

    public static Font font(double size) {
        return Font.font("Segoe UI", FontWeight.NORMAL, size);
    }

    // ─── Shadow Effects ───────────────────────────────────────────────────────
    public static DropShadow cardShadow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.45), 14, 0, 0, 4);
    }

    public static DropShadow cardHoverShadow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(255, 62, 108, 0.35), 22, 0, 0, 6);
    }

    public static DropShadow floatingShadow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.6), 28, 0, 0, 10);
    }

    public static DropShadow subtleShadow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.3), 10, 0, 0, 2);
    }

    public static DropShadow navbarShadow() {
        return new DropShadow(BlurType.THREE_PASS_BOX, Color.rgb(0, 0, 0, 0.5), 16, 0, 0, 3);
    }
}
