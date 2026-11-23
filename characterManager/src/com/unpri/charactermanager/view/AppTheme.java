package com.unpri.charactermanager.view;

import com.unpri.charactermanager.model.CharacterType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AppTheme {
    // Konstanta Ambang Level Mini-Challenge TP10
    public static final int HIGH_LEVEL_THRESHOLD = 5;

    // 1. Konstanta Warna Tema Gelap (Dark Theme)
    public static final Color BG_WINDOW = new Color(18, 18, 22);
    public static final Color BG_PANEL_SUBTLE = new Color(26, 27, 32);
    public static final Color STATUS_BAR_BG = new Color(14, 14, 17);
    public static final Color BORDER_SUBTLE_LINE = new Color(50, 53, 62);
    public static final Color TEXT_PRIMARY = Color.WHITE;
    public static final Color TEXT_MUTED = new Color(210, 215, 220);
    public static final Color GOLD_HIGHLIGHT = new Color(255, 215, 100);

    // Warna Solid Berdasarkan Role Karakter
    public static final Color ROLE_WARRIOR_BG = new Color(220, 45, 45);
    public static final Color ROLE_MAGE_BG = new Color(20, 145, 235);
    public static final Color ROLE_ARCHER_BG = new Color(20, 180, 140);
    public static final Color ROLE_HEALER_BG = new Color(115, 105, 235);
    public static final Color ROLE_DEFAULT_BG = new Color(80, 85, 95);

    // Warna Aksen Border Kartu Berdasarkan Role
    private static final Color BORDER_WARRIOR_ACCENT = new Color(255, 140, 140);
    private static final Color BORDER_MAGE_ACCENT = new Color(135, 210, 255);
    private static final Color BORDER_ARCHER_ACCENT = new Color(135, 245, 215);
    private static final Color BORDER_HEALER_ACCENT = new Color(200, 195, 255);
    private static final Color BORDER_DEFAULT_ACCENT = new Color(160, 165, 175);

    // 2. Konstanta Font (Logical Fonts + deriveFont)
    public static final Font FONT_TITLE = new Font(Font.SANS_SERIF, Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    public static final Font FONT_CARD = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font FONT_CARD_ELITE = FONT_CARD.deriveFont(Font.BOLD, 14f);
    public static final Font FONT_LEGEND = new Font(Font.MONOSPACED, Font.BOLD, 13);
    public static final Font FONT_STATS = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_STATS_VALUE = new Font(Font.MONOSPACED, Font.BOLD, 13);
    public static final Font FONT_STATUS = new Font(Font.MONOSPACED, Font.PLAIN, 13);

    // 3. Konstanta Border & Dimensi
    public static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder();
    public static final Border BORDER_HEADER = BorderFactory.createEmptyBorder(14, 18, 10, 18);
    public static final Border BORDER_STATS_GRID = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 18, 12, 18),
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_SUBTLE_LINE, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            )
    );
    public static final Border BORDER_SCROLL = BorderFactory.createEmptyBorder(4, 18, 8, 10);
    public static final Border BORDER_LEGEND_PANEL = BorderFactory.createEmptyBorder(12, 10, 12, 18);
    public static final Border BORDER_LEGEND_ITEM = BorderFactory.createEmptyBorder(10, 14, 10, 14);
    public static final Border BORDER_STATUS_BAR = BorderFactory.createEmptyBorder(10, 18, 10, 18);
    private static final Border BORDER_CARD_PADDING = BorderFactory.createEmptyBorder(10, 14, 10, 14);

    public static final Dimension SCROLL_VIEWPORT_SIZE = new Dimension(540, 320);
    public static final Dimension LEGEND_ITEM_MAX_SIZE = new Dimension(130, 40);

    private AppTheme() {
    }

    public static Color colorFor(CharacterType type) {
        if (type == null) return ROLE_DEFAULT_BG;
        switch (type) {
            case WARRIOR: return ROLE_WARRIOR_BG;
            case MAGE:    return ROLE_MAGE_BG;
            case ARCHER:  return ROLE_ARCHER_BG;
            case HEALER:  return ROLE_HEALER_BG;
            default:      return ROLE_DEFAULT_BG;
        }
    }

    public static Border cardBorderFor(CharacterType type) {
        Color accent = BORDER_DEFAULT_ACCENT;
        if (type != null) {
            switch (type) {
                case WARRIOR: accent = BORDER_WARRIOR_ACCENT; break;
                case MAGE:    accent = BORDER_MAGE_ACCENT; break;
                case ARCHER:  accent = BORDER_ARCHER_ACCENT; break;
                case HEALER:  accent = BORDER_HEALER_ACCENT; break;
            }
        }
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 2),
                BORDER_CARD_PADDING
        );
    }
}