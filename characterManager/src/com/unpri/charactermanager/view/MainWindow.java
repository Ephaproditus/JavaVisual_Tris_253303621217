package com.unpri.charactermanager.view;

import com.unpri.charactermanager.CharacterManager;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    private final CharacterManager manager;
    private final JPanel cardsGridPanel;
    private final JLabel headerCountLabel;
    private final JLabel statusLeftLabel;

    // Label pada Panel Statistik Tambahan (GridLayout 2x2)
    private final JLabel statTotalValLabel;
    private final JLabel statAvgLevelValLabel;
    private final JLabel statHighestLevelValLabel;
    private final JLabel statDominantRoleValLabel;

    public MainWindow(CharacterManager manager) {
        this.manager = manager;

        setTitle("Character Manager v0.5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(AppTheme.BG_WINDOW);

        // 1. Wilayah NORTH: Header Judul + Panel Statistik GridLayout(2, 2)
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.setOpaque(true);
        northContainer.setBackground(AppTheme.BG_WINDOW);

        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(true);
        headerBar.setBackground(AppTheme.BG_WINDOW);
        headerBar.setBorder(AppTheme.BORDER_HEADER);

        JLabel titleLabel = new JLabel("Character Manager");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        headerCountLabel = new JLabel("0 karakter", SwingConstants.RIGHT);
        headerCountLabel.setFont(AppTheme.FONT_STATS);
        headerCountLabel.setForeground(AppTheme.GOLD_HIGHLIGHT);

        headerBar.add(titleLabel, BorderLayout.WEST);
        headerBar.add(headerCountLabel, BorderLayout.EAST);

        // Panel Baru Tambahan: Ringkasan Statistik 2x2 (GridLayout)
        JPanel statsGridPanel = new JPanel(new GridLayout(2, 2, 16, 6));
        statsGridPanel.setOpaque(true);
        statsGridPanel.setBackground(AppTheme.BG_PANEL_SUBTLE);
        statsGridPanel.setBorder(AppTheme.BORDER_STATS_GRID);

        statTotalValLabel = createStatLabel("Total Karakter: 0");
        statAvgLevelValLabel = createStatLabel("Rata-rata Level: 0.00");
        statHighestLevelValLabel = createStatLabel("Level Tertinggi: -");
        statDominantRoleValLabel = createStatLabel("Jumlah Elite (Lv≥5): 0");

        statsGridPanel.add(statTotalValLabel);
        statsGridPanel.add(statAvgLevelValLabel);
        statsGridPanel.add(statHighestLevelValLabel);
        statsGridPanel.add(statDominantRoleValLabel);

        northContainer.add(headerBar, BorderLayout.NORTH);
        northContainer.add(statsGridPanel, BorderLayout.CENTER);
        add(northContainer, BorderLayout.NORTH);

        // 2. Wilayah CENTER: Panel Pembungkus (NORTH) + Grid Kartu + JScrollPane
        JPanel scrollWrapperPanel = new JPanel(new BorderLayout());
        scrollWrapperPanel.setOpaque(true);
        scrollWrapperPanel.setBackground(AppTheme.BG_WINDOW);

        cardsGridPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        cardsGridPanel.setOpaque(true);
        cardsGridPanel.setBackground(AppTheme.BG_WINDOW);
        scrollWrapperPanel.add(cardsGridPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollWrapperPanel);
        scrollPane.setBorder(AppTheme.BORDER_SCROLL);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(AppTheme.BG_WINDOW);
        scrollPane.getViewport().setBackground(AppTheme.BG_WINDOW);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(AppTheme.SCROLL_VIEWPORT_SIZE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Wilayah EAST: Legenda Role dengan BoxLayout Y_AXIS
        add(buildLegendPanel(), BorderLayout.EAST);

        // 4. Wilayah SOUTH: Status Bar (Teks di Kiri & "v0.5" di Kanan)
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(AppTheme.STATUS_BAR_BG);
        statusBar.setBorder(AppTheme.BORDER_STATUS_BAR);

        statusLeftLabel = new JLabel("Total: 0 karakter  |  Siap");
        statusLeftLabel.setFont(AppTheme.FONT_STATUS);
        statusLeftLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel versionRightLabel = new JLabel("v0.5", SwingConstants.RIGHT);
        versionRightLabel.setFont(AppTheme.FONT_STATUS);
        versionRightLabel.setForeground(AppTheme.TEXT_MUTED);

        statusBar.add(statusLeftLabel, BorderLayout.WEST);
        statusBar.add(versionRightLabel, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);

        refreshList();
        pack();
        setLocationRelativeTo(null);
    }

    private JLabel createStatLabel(String initialText) {
        JLabel label = new JLabel(initialText);
        label.setFont(AppTheme.FONT_STATS_VALUE);
        label.setForeground(AppTheme.TEXT_MUTED);
        return label;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(true);
        legendPanel.setBackground(AppTheme.BG_WINDOW);
        legendPanel.setBorder(AppTheme.BORDER_LEGEND_PANEL);

        JLabel legendTitle = new JLabel("Legenda Role");
        legendTitle.setFont(AppTheme.FONT_SUBTITLE);
        legendTitle.setForeground(AppTheme.TEXT_PRIMARY);
        legendTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        legendPanel.add(legendTitle);
        legendPanel.add(Box.createVerticalStrut(12));

        for (CharacterType type : CharacterType.values()) {
            JLabel itemLabel = new JLabel(type.name());
            itemLabel.setFont(AppTheme.FONT_LEGEND);
            itemLabel.setForeground(AppTheme.TEXT_PRIMARY);
            itemLabel.setOpaque(true);
            itemLabel.setBackground(AppTheme.colorFor(type));
            itemLabel.setBorder(AppTheme.BORDER_LEGEND_ITEM);
            itemLabel.setMaximumSize(AppTheme.LEGEND_ITEM_MAX_SIZE);
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            legendPanel.add(itemLabel);
            legendPanel.add(Box.createVerticalStrut(10));
        }

        legendPanel.add(Box.createVerticalGlue());
        return legendPanel;
    }

    private void refreshList() {
        cardsGridPanel.removeAll();
        List<Character> characters = manager.getAllCharacters();

        int maxLevel = 0;
        String topCharName = "-";
        int eliteCount = 0;

        for (Character c : characters) {
            boolean isElite = c.getLevel() >= AppTheme.HIGH_LEVEL_THRESHOLD;
            if (isElite) {
                eliteCount++;
            }
            if (c.getLevel() > maxLevel) {
                maxLevel = c.getLevel();
                topCharName = c.getName() + " (Lv." + c.getLevel() + ")";
            }

            JPanel card = new JPanel(new BorderLayout());
            card.setOpaque(true);
            card.setBackground(AppTheme.colorFor(c.getType()));
            card.setBorder(AppTheme.cardBorderFor(c.getType()));

            String tooltipText = String.format(
                    "%s (%s) — Lv.%d | HP: %d | ATK: %d | Mana: %d",
                    c.getName(), c.getType(), c.getLevel(),
                    c.getHitPoints(), c.getAttackPower(), c.getMana()
            );
            card.setToolTipText(tooltipText);

            String text = String.format(
                    "%s | %s | Lv.%d | HP:%d | ATK:%d",
                    c.getName(), c.getType(), c.getLevel(), c.getHitPoints(), c.getAttackPower()
            );

            JLabel infoLabel = new JLabel(text);
            infoLabel.setFont(isElite ? AppTheme.FONT_CARD_ELITE : AppTheme.FONT_CARD);
            infoLabel.setForeground(isElite ? AppTheme.GOLD_HIGHLIGHT : AppTheme.TEXT_PRIMARY);
            infoLabel.setToolTipText(tooltipText);

            card.add(infoLabel, BorderLayout.WEST);
            cardsGridPanel.add(card);
        }

        int total = manager.getTotalCharacters();
        double avg = manager.getAverageLevel();

        headerCountLabel.setText(total + " karakter");
        statusLeftLabel.setText("Total: " + total + " karakter  |  Siap");

        statTotalValLabel.setText("Total Karakter: " + total);
        statAvgLevelValLabel.setText(String.format("Rata-rata Level: %.2f", avg));
        statHighestLevelValLabel.setText("Level Tertinggi: " + topCharName);
        statDominantRoleValLabel.setText("Karakter Elite (Lv≥" + AppTheme.HIGH_LEVEL_THRESHOLD + "): " + eliteCount);

        cardsGridPanel.revalidate();
        cardsGridPanel.repaint();
    }
}