package com.unpri.charactermanager.view;

import com.unpri.charactermanager.CharacterManager;
import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.ParseException;

public class CharacterFormPanel extends JPanel {
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 20;

    private final CharacterManager manager;
    private final Runnable onCharacterAdded;

    private final JTextField nameField;
    private final JComboBox<CharacterType> roleComboBox;
    private final JSpinner levelSpinner;
    private final JButton addButton;
    private final JLabel feedbackLabel;

    private final Border originalNameBorder;
    private final Border originalSpinnerBorder;

    public CharacterFormPanel(CharacterManager manager, Runnable onCharacterAdded) {
        this.manager = manager;
        this.onCharacterAdded = onCharacterAdded;

        setLayout(new BorderLayout(0, 8));
        setOpaque(true);
        setBackground(AppTheme.BG_FORM_PANEL);
        setBorder(AppTheme.BORDER_FORM_PANEL);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        inputRow.setOpaque(true);
        inputRow.setBackground(AppTheme.BG_FORM_PANEL);

        JLabel nameLabel = new JLabel("Nama:");
        nameLabel.setFont(AppTheme.FONT_FORM_LABEL);
        nameLabel.setForeground(AppTheme.TEXT_PRIMARY);

        nameField = new JTextField(14);
        nameField.setFont(AppTheme.FONT_FORM_INPUT);
        nameField.setBackground(AppTheme.BG_INPUT);
        nameField.setForeground(AppTheme.TEXT_PRIMARY);
        nameField.setCaretColor(AppTheme.TEXT_PRIMARY);
        nameField.setBorder(AppTheme.BORDER_INPUT_NORMAL);
        originalNameBorder = nameField.getBorder();

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(AppTheme.FONT_FORM_LABEL);
        roleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        roleComboBox = new JComboBox<>(CharacterType.values());
        roleComboBox.setFont(AppTheme.FONT_FORM_INPUT);
        roleComboBox.setBackground(AppTheme.BG_INPUT);
        roleComboBox.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel levelLabel = new JLabel("Level:");
        levelLabel.setFont(AppTheme.FONT_FORM_LABEL);
        levelLabel.setForeground(AppTheme.TEXT_PRIMARY);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
                Character.MIN_LEVEL,
                Character.MIN_LEVEL,
                Character.MAX_LEVEL,
                1
        );
        levelSpinner = new JSpinner(spinnerModel);
        levelSpinner.setFont(AppTheme.FONT_FORM_INPUT);
        JComponent editor = levelSpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setColumns(3);
            tf.setBackground(AppTheme.BG_INPUT);
            tf.setForeground(AppTheme.TEXT_PRIMARY);
            tf.setCaretColor(AppTheme.TEXT_PRIMARY);
        }
        originalSpinnerBorder = levelSpinner.getBorder();

        addButton = new JButton("Tambah Karakter");
        addButton.setFont(AppTheme.FONT_FORM_LABEL);
        addButton.setBackground(AppTheme.BTN_PRIMARY_BG);
        addButton.setForeground(AppTheme.TEXT_PRIMARY);
        addButton.setFocusPainted(false);

        inputRow.add(nameLabel);
        inputRow.add(nameField);
        inputRow.add(roleLabel);
        inputRow.add(roleComboBox);
        inputRow.add(levelLabel);
        inputRow.add(levelSpinner);
        inputRow.add(addButton);

        feedbackLabel = new JLabel("Siap menambahkan karakter baru (Level 1–99; MAGE maks. Lv.60).");
        feedbackLabel.setFont(AppTheme.FONT_FEEDBACK);
        feedbackLabel.setForeground(AppTheme.TEXT_MUTED);

        add(inputRow, BorderLayout.CENTER);
        add(feedbackLabel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleSubmit());
        nameField.addActionListener(e -> handleSubmit());
    }

    /**
     * Memeriksa nama secara berurutan: kelengkapan -> panjang -> format -> keunikan.
     * Hanya mengembalikan pesan error (atau null jika valid) tanpa menyentuh komponen.
     */
    public String validateName(String rawName) {
        String trimmed = rawName == null ? "" : rawName.trim();

        // 1. Kelengkapan
        if (trimmed.isEmpty()) {
            return "Nama karakter wajib diisi (tidak boleh kosong).";
        }

        // 2. Panjang
        if (trimmed.length() < MIN_NAME_LENGTH || trimmed.length() > MAX_NAME_LENGTH) {
            return "Panjang nama karakter harus antara " + MIN_NAME_LENGTH + " hingga " + MAX_NAME_LENGTH + " karakter.";
        }

        // 3. Format (hanya huruf, spasi, tanda hubung; dilarang spasi ganda di View)
        if (trimmed.contains("  ")) {
            return "Nama karakter tidak boleh mengandung spasi ganda beruntun.";
        }
        if (!trimmed.matches("[A-Za-zÀ-ÿ\\s'-]+")) {
            return "Format nama tidak valid: gunakan huruf saja (tanpa angka atau simbol khusus).";
        }

        // 4. Keunikan
        if (manager.isNameTaken(trimmed)) {
            return "Nama karakter '" + trimmed + "' sudah terdaftar. Masukkan nama unik.";
        }

        return null;
    }

    private void resetFieldStyles() {
        nameField.setBorder(originalNameBorder);
        levelSpinner.setBorder(originalSpinnerBorder);
    }

    private void showInlineError(String message, JComponent targetComponent) {
        resetFieldStyles();
        if (targetComponent == nameField) {
            nameField.setBorder(AppTheme.BORDER_INPUT_ERROR);
        } else if (targetComponent == levelSpinner) {
            levelSpinner.setBorder(AppTheme.BORDER_INPUT_ERROR);
        }
        feedbackLabel.setForeground(AppTheme.TEXT_ERROR);
        feedbackLabel.setText(message);
        if (targetComponent != null) {
            targetComponent.requestFocusInWindow();
        }
    }

    private void handleSubmit() {
        String cleanedName = nameField.getText().trim();
        String nameError = validateName(cleanedName);
        if (nameError != null) {
            showInlineError(nameError, nameField);
            return;
        }

        try {
            levelSpinner.commitEdit();
        } catch (ParseException ex) {
            showInlineError("Nilai level tidak valid; masukkan angka bulat antara 1 hingga 99.", levelSpinner);
            return;
        }

        CharacterType selectedRole = (CharacterType) roleComboBox.getSelectedItem();
        int selectedLevel = (Integer) levelSpinner.getValue();

        try {
            int computedHp = 100 + (selectedLevel * 20);
            int computedAtk = 15 + (selectedLevel * 5);
            int computedMana = (selectedRole == CharacterType.MAGE || selectedRole == CharacterType.HEALER)
                    ? 120 + (selectedLevel * 10)
                    : 50 + (selectedLevel * 5);

            Character newCharacter = new Character(
                    cleanedName,
                    selectedRole,
                    selectedLevel,
                    computedHp,
                    computedAtk,
                    computedMana
            );

            manager.addCharacter(newCharacter);
            resetFieldStyles();
            nameField.setText("");
            levelSpinner.setValue(Character.MIN_LEVEL);

            feedbackLabel.setForeground(AppTheme.TEXT_SUCCESS);
            feedbackLabel.setText("Berhasil menambahkan '" + cleanedName + "' (" + selectedRole + ", Lv." + selectedLevel + ").");

            if (onCharacterAdded != null) {
                onCharacterAdded.run();
            }
            nameField.requestFocusInWindow();
        } catch (IllegalArgumentException ex) {
            JComponent focusTarget = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("level")
                    ? levelSpinner
                    : nameField;
            showInlineError(ex.getMessage(), focusTarget);
        }
    }
}