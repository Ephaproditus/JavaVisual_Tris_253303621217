package com.unpri.charactermanager.view;

import com.unpri.charactermanager.CharacterManager;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    private final CharacterManager manager;
    private final JPanel listPanel;
    private final JLabel headerCountLabel;
    private final JLabel statusLabel;

    public MainWindow(CharacterManager manager) {
        this.manager = manager;

        setTitle("Character Manager v0.4 - TP10");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(AppTheme.BG_WINDOW);

        // 1. Header Atas
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
        add(headerBar, BorderLayout.NORTH);

        // 2. Daftar Karakter Dinamis (Tengah)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(true);
        listPanel.setBackground(AppTheme.BG_WINDOW);
        listPanel.setBorder(AppTheme.BORDER_LIST_PANEL);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(AppTheme.BORDER_EMPTY);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(AppTheme.BG_WINDOW);
        scrollPane.getViewport().setBackground(AppTheme.BG_WINDOW);
        scrollPane.setPreferredSize(AppTheme.SCROLL_VIEWPORT_SIZE);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Legenda Role (Kanan)
        add(buildLegendPanel(), BorderLayout.EAST);

        // 4. Status Bar Bawah (Total Karakter & Rata-rata Level)
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(AppTheme.STATUS_BAR_BG);
        statusBar.setBorder(AppTheme.BORDER_STATUS_BAR);

        statusLabel = new JLabel("Total: 0 karakter  |  Rata-rata Level: 0.00  |  Siap");
        statusLabel.setFont(AppTheme.FONT_STATUS);
        statusLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel rightTag = new JLabel("v0.4 · TP10", SwingConstants.RIGHT);
        rightTag.setFont(AppTheme.FONT_STATUS);
        rightTag.setForeground(AppTheme.TEXT_MUTED);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(rightTag, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);

        refreshList();
        pack();
        setLocationRelativeTo(null);
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
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            legendPanel.add(itemLabel);
            legendPanel.add(Box.createVerticalStrut(10));
        }

        return legendPanel;
    }

    private void refreshList() {
        listPanel.removeAll();
        List<Character> characters = manager.getAllCharacters();

        for (Character c : characters) {
            boolean isElite = c.getLevel() >= AppTheme.HIGH_LEVEL_THRESHOLD;

            JPanel card = new JPanel(new BorderLayout());
            card.setOpaque(true);
            card.setBackground(AppTheme.colorFor(c.getType()));
            // Properti Tambahan 2: Border berbeda sesuai role karakter
            card.setBorder(AppTheme.cardBorderFor(c.getType()));
            card.setMaximumSize(AppTheme.CARD_MAX_SIZE);

            // Properti Tambahan 1: Tooltip detail statistik lengkap (termasuk Mana & Status)
            String tooltipText = String.format(
                    "%s (%s) — Lv.%d | HP: %d | ATK: %d | Mana: %d | Status: %s",
                    c.getName(), c.getType(), c.getLevel(), c.getHitPoints(),
                    c.getAttackPower(), c.getMana(), c.isAlive() ? "Aktif" : "Gugur"
            );
            card.setToolTipText(tooltipText);

            String text = String.format(
                    "%s | %s | Lv.%d | HP:%d | ATK:%d",
                    c.getName(), c.getType(), c.getLevel(), c.getHitPoints(), c.getAttackPower()
            );

            JLabel infoLabel = new JLabel(text);
            // Mini-Challenge: Level >= HIGH_LEVEL_THRESHOLD memakai teks emas dan tebal
            infoLabel.setFont(isElite ? AppTheme.FONT_CARD_ELITE : AppTheme.FONT_CARD);
            infoLabel.setForeground(isElite ? AppTheme.GOLD_HIGHLIGHT : AppTheme.TEXT_PRIMARY);
            infoLabel.setToolTipText(tooltipText);

            card.add(infoLabel, BorderLayout.WEST);
            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(10));
        }

        int total = manager.getTotalCharacters();
        double avg = manager.getAverageLevel();

        headerCountLabel.setText(total + " karakter");
        statusLabel.setText(String.format("Total: %d karakter  |  Rata-rata Level: %.2f  |  Siap", total, avg));

        listPanel.revalidate();
        listPanel.repaint();
    }
}