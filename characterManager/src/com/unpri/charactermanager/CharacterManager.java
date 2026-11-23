package com.unpri.charactermanager;

import com.unpri.charactermanager.model.Character;
import com.unpri.charactermanager.model.CharacterType;
import java.util.ArrayList;
import java.util.List;

public class CharacterManager {
    public static final int MAX_CHARACTERS = 20;

    private final List<Character> characterList;

    public CharacterManager() {
        this.characterList = new ArrayList<>();
    }

    public boolean isNameTaken(String name) {
        if (name == null) return false;
        String cleaned = name.trim();
        if (cleaned.isEmpty()) return false;
        for (Character existing : characterList) {
            if (existing.getName().equalsIgnoreCase(cleaned)) {
                return true;
            }
        }
        return false;
    }

    public boolean addCharacter(Character c) {
        if (c == null) {
            throw new IllegalArgumentException("Objek karakter tidak boleh null.");
        }
        if (characterList.size() >= MAX_CHARACTERS) {
            throw new IllegalArgumentException(
                    "Kapasitas penuh: maksimal " + MAX_CHARACTERS + " karakter dalam daftar."
            );
        }
        Character.validateCharacterAttributes(c.getName(), c.getType(), c.getLevel(), c.getMana());
        if (isNameTaken(c.getName())) {
            throw new IllegalArgumentException(
                    "Nama karakter '" + c.getName().trim() + "' sudah digunakan. Gunakan nama lain."
            );
        }
        characterList.add(c);
        return true;
    }

    public Character findByName(String name) {
        if (name == null) return null;
        for (Character c : characterList) {
            if (c.getName().equalsIgnoreCase(name.trim())) {
                return c;
            }
        }
        return null;
    }

    public boolean removeCharacter(String name) {
        Character target = findByName(name);
        if (target != null) {
            return characterList.remove(target);
        }
        return false;
    }

    public int getTotalCharacters() {
        return characterList.size();
    }

    public List<Character> getAllCharacters() {
        return new ArrayList<>(characterList);
    }

    public double getAverageLevel() {
        if (characterList.isEmpty()) {
            return 0.0;
        }
        double totalLevel = 0;
        for (Character c : characterList) {
            totalLevel += c.getLevel();
        }
        return totalLevel / characterList.size();
    }

    public ArrayList<Character> findByType(CharacterType type) {
        ArrayList<Character> result = new ArrayList<>();
        for (Character c : characterList) {
            if (c.getType() == type) {
                result.add(c);
            }
        }
        return result;
    }

    public boolean renameCharacter(String oldName, String newName) {
        if (oldName == null || newName == null || newName.trim().isEmpty()) {
            return false;
        }
        Character target = findByName(oldName);
        if (target == null) {
            System.out.println("[ERROR] Karakter '" + oldName + "' tidak ditemukan.");
            return false;
        }
        if (!oldName.equalsIgnoreCase(newName.trim()) && isNameTaken(newName)) {
            System.out.println("[ERROR] Nama baru '" + newName + "' sudah digunakan oleh karakter lain!");
            return false;
        }
        target.setName(newName);
        return true;
    }
}