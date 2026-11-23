package com.unpri.charactermanager.view;

import com.unpri.charactermanager.CharacterManager;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MainWindow extends JFrame {
    // Konstanta warna & font langsung di MainWindow (tanpa AppTheme.java)
    private static final Color BG_WINDOW = new Color(18, 18, 22);
    private static final Color TEXT_PRIMARY = Color.WHITE;
    private static final Color TEXT_MUTED = new Color(210, 215, 220);
    private static final Color GOLD_HIGHLIGHT = new Color(255, 215, 100);
    private static final Color STATUS_BAR_BG = new Color(14, 14, 17);

    private static final Color ROLE_WARRIOR_BG = new Color(220, 45, 45);
    private static final Color ROLE_MAGE_BG = new Color(20, 145, 235);
    private static final Color ROLE_ARCHER_BG = new Color(20, 180, 140);
    private static final Color ROLE_HEALER_BG = new Color(115, 105, 235);
    private static final Color ROLE_DEFAULT_BG = new Color(80, 85, 95);

    private final CharacterManager manager;
    private final JPanel listPanel;
    private final JLabel headerCountLabel;
    private final JLabel statusLabel;

    public MainWindow(CharacterManager manager) {
        this.manager = manager;

        setTitle("Character Manager - TP8");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_WINDOW);

        // 1. Header Atas
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(true);
        headerBar.setBackground(BG_WINDOW);
        headerBar.setBorder(BorderFactory.createEmptyBorder(16, 18, 12, 18));

        JLabel titleLabel = new JLabel("Character Manager");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);

        headerCountLabel = new JLabel("0 karakter", SwingConstants.RIGHT);
        headerCountLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        headerCountLabel.setForeground(GOLD_HIGHLIGHT);

        headerBar.add(titleLabel, BorderLayout.WEST);
        headerBar.add(headerCountLabel, BorderLayout.EAST);
        add(headerBar, BorderLayout.NORTH);

        // 2. Daftar Karakter Dinamis (Tengah)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(true);
        listPanel.setBackground(BG_WINDOW);
        listPanel.setBorder(BorderFactory.createEmptyBorder(6, 18, 12, 10));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(true);
        scrollPane.setBackground(BG_WINDOW);
        scrollPane.getViewport().setBackground(BG_WINDOW);
        scrollPane.setPreferredSize(new Dimension(540, 340));
        add(scrollPane, BorderLayout.CENTER);

        // 3. Legenda Role (Kanan)
        add(buildLegendPanel(), BorderLayout.EAST);

        // 4. Status Bar Bawah (Total Karakter & Rata-rata Level)
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setOpaque(true);
        statusBar.setBackground(STATUS_BAR_BG);
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        statusLabel = new JLabel("Total: 0 karakter  |  Rata-rata Level: 0.00  |  Siap");
        statusLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        statusLabel.setForeground(TEXT_PRIMARY);

        JLabel rightTag = new JLabel("TP8", SwingConstants.RIGHT);
        rightTag.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        rightTag.setForeground(TEXT_MUTED);

        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(rightTag, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);

        refreshList();
        pack();
        setLocationRelativeTo(null);
    }

    private Color colorFor(CharacterType type) {
        if (type == null) return ROLE_DEFAULT_BG;
        if (type == CharacterType.WARRIOR) return ROLE_WARRIOR_BG;
        if (type == CharacterType.MAGE) return ROLE_MAGE_BG;
        if (type == CharacterType.ARCHER) return ROLE_ARCHER_BG;
        if (type == CharacterType.HEALER) return ROLE_HEALER_BG;
        return ROLE_DEFAULT_BG;
    }

    private JPanel buildLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setOpaque(true);
        legendPanel.setBackground(BG_WINDOW);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 18));

        JLabel legendTitle = new JLabel("Legenda Role");
        legendTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        legendTitle.setForeground(TEXT_PRIMARY);
        legendTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        legendPanel.add(legendTitle);
        legendPanel.add(Box.createVerticalStrut(12));

        for (CharacterType type : CharacterType.values()) {
            JLabel itemLabel = new JLabel(type.name());
            itemLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
            itemLabel.setForeground(TEXT_PRIMARY);
            itemLabel.setOpaque(true);
            itemLabel.setBackground(colorFor(type));
            itemLabel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            legendPanel.add(itemLabel);
            legendPanel.add(Box.createVerticalStrut(10));
        }

        return legendPanel;
    }

    private void refreshList() {
        listPanel.removeAll();
        List<Character> characters = manager.getAllCharacters();

        int sumLevel = 0;
        for (Character c : characters) {
            sumLevel += c.getLevel();

            JPanel card = new JPanel(new BorderLayout());
            card.setOpaque(true);
            card.setBackground(colorFor(c.getType()));
            card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            String text = String.format(
                    "%s | %s | Lv.%d | HP:%d | ATK:%d",
                    c.getName(), c.getType(), c.getLevel(), c.getHitPoints(), c.getAttackPower()
            );

            JLabel infoLabel = new JLabel(text);
            infoLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            infoLabel.setForeground(TEXT_PRIMARY);

            card.add(infoLabel, BorderLayout.WEST);
            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(10));
        }

        int total = characters.size();
        double avg = total > 0 ? (double) sumLevel / total : 0.0;

        headerCountLabel.setText(total + " karakter");
        statusLabel.setText(String.format("Total: %d karakter  |  Rata-rata Level: %.2f  |  Siap", total, avg));

        listPanel.revalidate();
        listPanel.repaint();
    }
}