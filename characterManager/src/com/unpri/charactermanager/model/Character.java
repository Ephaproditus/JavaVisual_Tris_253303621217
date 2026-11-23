package com.unpri.charactermanager.model;

public class Character {
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 99;
    public static final int MAX_MAGE_LEVEL = 60;

    private static int totalCharacterCreated;

    private String name;
    private CharacterType type;
    private int level;
    private int hitPoints;
    private int attackPower;
    private int mana;

    public Character(String name, CharacterType type) {
        this(name, type, MIN_LEVEL, 120, 20, 50);
    }

    public Character(String name, CharacterType type, int level, int hitPoints, int attackPower, int mana) {
        validateCharacterAttributes(name, type, level, mana);
        this.name = name.trim();
        this.type = type;
        this.level = level;
        this.hitPoints = hitPoints;
        this.attackPower = attackPower;
        this.mana = mana;
        totalCharacterCreated++;
    }

    public static void validateCharacterAttributes(String name, CharacterType type, int level, int mana) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama karakter wajib diisi (tidak boleh kosong).");
        }
        if (type == null) {
            throw new IllegalArgumentException("Role karakter wajib dipilih.");
        }
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new IllegalArgumentException(
                    "Level karakter harus berada di antara " + MIN_LEVEL + " hingga " + MAX_LEVEL + "."
            );
        }
        if (type == CharacterType.MAGE && level > MAX_MAGE_LEVEL) {
            throw new IllegalArgumentException(
                    "Role MAGE tidak boleh memiliki level di atas " + MAX_MAGE_LEVEL + " (level saat ini: " + level + ")."
            );
        }
        if (mana < 0) {
            throw new IllegalArgumentException("Mana tidak boleh bernilai negatif.");
        }
    }

    public static int getTotalCharacterCreated() {
        return totalCharacterCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama karakter wajib diisi (tidak boleh kosong).");
        }
        this.name = name.trim();
    }

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        if (type == null) {
            throw new IllegalArgumentException("Role karakter wajib dipilih.");
        }
        Character.validateCharacterAttributes(this.name, type, this.level, this.mana);
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        Character.validateCharacterAttributes(this.name, this.type, level, this.mana);
        this.level = level;
        this.hitPoints = 100 + (level * 20);
        this.attackPower = 15 + (level * 5);
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        if (mana < 0) {
            throw new IllegalArgumentException("Mana tidak boleh bernilai negatif.");
        }
        this.mana = mana;
    }

    public boolean isAlive() {
        return this.hitPoints > 0;
    }

    public void levelUp() {
        setLevel(this.level + 1);
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", hitPoints=" + hitPoints +
                ", attackPower=" + attackPower +
                ", mana=" + mana +
                '}';
    }
}