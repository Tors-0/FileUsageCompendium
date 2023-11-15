package aiden;/*
Character object class for The Dark Room, in order to save characters more efficiently and effectively
 */

import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;

public class Character implements Serializable {
    /*
    Variables for character object
     */
    private String name;
    private int health;
    private int maxHealth;
    private int maxAbility;
    private int ability;

    /*
    Constructor for Character object
     */
    public Character(String name, int maxHealth, int maxAbility) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.maxAbility = maxAbility;
        ability = maxAbility;
        health = maxHealth;
    }

    /*
    Methods to get and change Character variables
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxAbility() {
        return maxAbility;
    }
    public int getAbility() {
        return ability;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void changeName(String newName) {
        name = newName;
    }

    public void changeHealth(int newHealth) {
        health = newHealth;
    }

    public void changeAbility(int newAbility) {
        ability = newAbility;
    }

    public void setCharacter(Character character) {
        name = character.getName();
        maxAbility = character.getMaxAbility();
        maxHealth = character.getMaxHealth();
        health = character.getHealth();
        ability = character.getAbility();

    }

    /*
    Saves character file, uses Character array and converts into file format and serializes
     */
    public static void characterSave(ArrayList<Character> characters, File file) throws IOException {
        file.delete();
        FileOutputStream saveFile = new FileOutputStream(file, true);
        ObjectOutputStream saveObj = new ObjectOutputStream(saveFile);

        for (Character character : characters) {
            saveObj.writeObject(character);
        }

        saveObj.close();
        saveFile.close();
    }

    /*
    Loads from existing character file, relies on number of characters existing in file, then imports
     */
    public static ArrayList<Character> characterLoad(File file, int numOfCharacters) throws IOException, ClassNotFoundException {
        FileInputStream importFile = new FileInputStream(file);
        ObjectInputStream importObj = new ObjectInputStream(importFile);
        ArrayList<Character> characterArray = new ArrayList<>();

        for (int i = 0; i < numOfCharacters; i++) {
            characterArray.add((Character) importObj.readObject());
        }

        importObj.close();
        importFile.close();

        return characterArray;
    }

}
