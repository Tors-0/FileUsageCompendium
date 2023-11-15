package aiden;
/*
"The Dark Room"
Horror-ish? RPG/Choose-your-own-adventure style game.
Created the foundation of it with Character class and a saving system that saves all data even when closing program
Didn't have time to finish the actual game part.

Aiden Rouhani
Unit 3 Project
10-30-23
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {

    /*
    Creation of essential objects/files/scannner
     */
    static Scanner input = new Scanner(System.in);
    static File database = new File("./database.db");
    static File characterDatabase = new File("./characters.db");

    /*
    Game variables/values
     */
    static boolean firstRun = false;
    static String difficulty = "Normal";
    static int currencyBalance = 0;
    static int finishedPlaythroughs = 0;
    static int lastLevel = 0;
    static String lastMenu = "";
    static int numberOfCharacters = 0;

    /*
    Character storage
     */
    static ArrayList<Character> characterArray = new ArrayList<Character>();
    static int mainCharacter = 0;

    /*
    Main method, which starts the gameInitialization() method. Throws exception as result of children methods
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
            gameInitialization();
    }

    /*
    Initializes game, characters, and data from save file
     */
    public static void gameInitialization() throws IOException, ClassNotFoundException {
        System.out.println("Initializing game...");

        /*
        Checks if database file is a file and if the character database is a file, then proceeds to load the data into variables. Uses custom methods in Character class
         */
        if (database.isFile() && database.length() > 0 && characterDatabase.isFile()) {
            System.out.println("Detected save file present. Loading data... ");

            FileReader fileRead = new FileReader(database);
            BufferedReader buffRead = new BufferedReader(fileRead);

            difficulty = buffRead.readLine().replace("Difficulty: ", "");
            currencyBalance = Integer.parseInt(buffRead.readLine().replace("Currency Balance: ", ""));
            finishedPlaythroughs = Integer.parseInt(buffRead.readLine().replace("Finished Playthroughs: ", ""));
            lastLevel = Integer.parseInt(buffRead.readLine().replace("Last Level: ", ""));
            numberOfCharacters = Integer.parseInt(buffRead.readLine().replace("Number of Characters: ", ""));

            characterArray = Character.characterLoad(characterDatabase, numberOfCharacters);

            buffRead.close();
            fileRead.close();
        } else {
            /*
            Creates new files if no save file present
             */
            System.out.println("No save file present. Creating first-run data...");
            firstRun = true;

            if (characterDatabase.isFile()) {
                characterDatabase.delete();
            }

            /*
            Uses getWriter method to save file to normal (non-character) DB
             */
            FileWriter fileWrite = new FileWriter(database);
            try {
                BufferedWriter buffWrite = getWriter(fileWrite);
                buffWrite.flush();
                buffWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileWrite.close();
        }
        System.out.println("Initialization complete.");
        gameMenu(false);
    }

    /*
    getWriter() method, saves data to non-character DB stored at ./data.db using in-game variables
     */
    public static BufferedWriter getWriter(FileWriter fileWrite) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(fileWrite);

        buffWrite.append("Difficulty: ").append(difficulty);
        buffWrite.newLine();
        buffWrite.append("Currency Balance: ").append(String.valueOf(currencyBalance));
        buffWrite.newLine();
        buffWrite.append("Finished Playthroughs: ").append(String.valueOf(finishedPlaythroughs));
        buffWrite.newLine();
        buffWrite.append("Last Level: ").append(String.valueOf(lastLevel));
        buffWrite.newLine();
        buffWrite.append("Number of Characters: ").append(String.valueOf(numberOfCharacters));
        return buffWrite;
    }

    /*
    Game's Main Menu which goes into other categories.
    */
    public static void gameMenu(boolean previouslyIncorrect) throws IOException {

        System.out.println("___________.__             ________                __     __________                       \n" +
                "\\__    ___/|  |__   ____   \\______ \\ _____ _______|  | __ \\______   \\ ____   ____   _____  \n" +
                "  |    |   |  |  \\_/ __ \\   |    |  \\\\__  \\\\_  __ \\  |/ /  |       _//  _ \\ /  _ \\ /     \\ \n" +
                "  |    |   |   Y  \\  ___/   |    `   \\/ __ \\|  | \\/    <   |    |   (  <_> |  <_> )  Y Y  \\\n" +
                "  |____|   |___|  /\\___  > /_______  (____  /__|  |__|_ \\  |____|_  /\\____/ \\____/|__|_|  /\n" +
                "                \\/     \\/          \\/     \\/           \\/         \\/                    \\/ ");
        System.out.println("                                      MAIN MENU                                          \n");
        System.out.println("(1) Start your journey into the dark room...");
        System.out.println("(2) Change your settings...");
        System.out.println("(3) Give up and quit...");
        System.out.println("\n                             © 1988 Reddark Productions");

        // Displays message if the method has an indication that the previous attempt was incorrect.
        System.out.println(previouslyIncorrect ? "!! Incorrect response. Please try again. !!" : "");

        /*
        Switch statement handles different menu options.
         */
        switch (input.nextInt()) {
            case 1:
                System.out.println("Game is still in progress. Try again later.");
                gameMenu(false);
                break;
            case 2:
                settingsMenu(false);
                break;
            case 3:
                quitGame(false);
            default:
                gameMenu(true);
                break;
        }
    }

    /*
    settingsMenu(), takes boolean to determine whether last input was invalid. Displays character stats and allows settings to be changed and saved across game instances
     */
    public static void settingsMenu(boolean invalidResponse) throws IOException {
        lastMenu = "Settings";
        System.out.println("////  S E T T I N G S    M E N U\n");
        System.out.println("(1) Change Difficulty [CURRENT: " + difficulty + "]");
        System.out.println("(2) Change Character [CURRENT: " + ((characterArray.isEmpty()) ? "N/A" : characterArray.get(mainCharacter)) +"]");
        System.out.println("(3) Delete all existing data [REQUIRES GAME RESTART]");

        System.out.println("\n//   S T A T S");
        System.out.println("Currency Balance: " + currencyBalance);
        System.out.println("Amount of Finished Playthroughs: " + finishedPlaythroughs);
        System.out.println("Max Health: " + ((characterArray.isEmpty()) ? "N/A" : characterArray.get(mainCharacter).getMaxHealth()));
        System.out.println("Max Ability: " + ((characterArray.isEmpty()) ? "N/A" : characterArray.get(mainCharacter).getMaxAbility()));
        System.out.println("Current Health: " + ((characterArray.isEmpty()) ? "N/A" : characterArray.get(mainCharacter).getHealth()));
        System.out.println("Current Ability: " + ((characterArray.isEmpty()) ? "N/A" : characterArray.get(mainCharacter).getAbility()));
        System.out.println("Last Level: " + lastLevel);

        System.out.println("\n(0) Go back to Main Menu");
        System.out.println((invalidResponse) ? "Incorrect response. Please try again." : "");

        /*
        Switch statement to handle different outcomes for inputs
         */
        switch (input.nextInt()) {
            case 1:
                difficulty = (difficulty.equals("Easy")) ? "Normal" : (difficulty.equals("Normal")) ? "Hard" : "Easy";
                System.out.println("Difficulty changed to:" + difficulty);
                settingsMenu(false);
                break;
            case 2:
                characterMenu(false);
                break;
            case 3:
                System.out.println("Deleting all data...");
                quitGame(true);
            case 0:
                gameMenu(false);
                break;
            default:
                settingsMenu(true);
        }
    }

    /*
    Character creation, creates character with user-inputted name but random-generated maxHealth and maxAbility, from 75 to 150, inclusive
     */
    public static void characterCreation() {
        System.out.println("What would you like to call your character? ");
        input.nextLine();
        String characterName = input.nextLine();
        int maxHealth = (int)(Math.random()*76)+75;
        int maxAbility = (int)(Math.random()*76)+75;

        /*
        Adds characters to in-game character array, and increases number of characters.
         */
        characterArray.add(new Character(characterName, maxHealth, maxAbility));
        numberOfCharacters++;
        System.out.println("Created character " + characterName + " with " + maxHealth + " max health and " + maxAbility + " max ability.\n");
    }

    /*
    Character menu, which displays the number of characters (if any), and allows user to delete, create a new, or select a new main character
     */
    public static void characterMenu(boolean invalidResponse) throws IOException {
        System.out.println("////  C H A R A C T E R  M E N U");
        System.out.println(numberOfCharacters + "/3 Character Slots used\n");

        /*
        Determines if no characters exist, then outputs message.
         */
        if (numberOfCharacters == 0) {
            System.out.println("WARNING: No characters yet. Press 9 to create a new character.");
        }

        /*
        For each character in numberOfCharacters, displays their stats, number to select, and selected status
         */
        for (int i = 0; i < numberOfCharacters; i++) {
            Character currentCharacter = characterArray.get(i);
            System.out.println("(" + (i+1) + ") " + currentCharacter + ((i == mainCharacter) ? " [CURRENT]" : ""));
            System.out.println("    - Max Health: " + currentCharacter.getMaxHealth() + " (Current: " + currentCharacter.getHealth() + ")");
            System.out.println("    - Max Ability: " + currentCharacter.getMaxAbility() + " (Current: " + currentCharacter.getAbility() + ")\n");
        }

        System.out.println("\n(8) Delete a character");
        System.out.println("(9) Create a new character");
        System.out.println((lastMenu.equals("Settings")) ? "(0) Go back to Settings Menu" : "(0) Go back to the Main Menu");
        System.out.println((invalidResponse) ? "Incorrect response. Please try again." : "");

        /*
        Switch statement to handle various user-input choices
         */
        switch (input.nextInt()) {
            case 1:
                mainCharacter = 0;
                characterMenu(false);
                break;
            case 2:
                mainCharacter = 1;
                characterMenu(false);
                break;
            case 3:
                mainCharacter = 2;
                characterMenu(false);
                break;
            case 8:
                /*
                Deletion case, lets character delete a character, only if it exists
                 */
                if (numberOfCharacters > 0) {
                    input.nextLine();
                    System.out.print("What character would you like to delete? ");

                    int deletedChar = input.nextInt()-1;

                    /*
                    Checks to see if requested delete character is valid character
                     */
                    if (deletedChar <= numberOfCharacters) {
                        System.out.println("Character" + characterArray.get(deletedChar) + "deleted.");
                        characterArray.remove(deletedChar);
                        numberOfCharacters--;
                    } else {
                        System.out.println("Character doesn't exist. Please try again.");
                    }

                    characterMenu(false);
                } else {
                    System.out.println("You have no characters available to delete.");
                }
                break;
            case 9:
                if (numberOfCharacters != 3) {
                    characterCreation();
                } else {
                    System.out.println("You are at the max amount of characters. Delete one and try again. (3/3)");
                }
                characterMenu(false);
                break;
            case 0:
                if (lastMenu.equals("Settings")) {
                    settingsMenu(false);
                } else {
                    gameMenu(false);
                }
                break;
            default:
                characterMenu(true);
        }
    }

    /*
    Quits game and saves data, if user didn't select data delete option.
     */
    public static void quitGame(boolean dataDelete) throws IOException {
        FileWriter write = new FileWriter(database);

        /*
        Determines if data is requested to be deleted, based on boolean, or if it is a normal shutdown, of which it saves
         */
        if (dataDelete) {
            database.delete();
            characterDatabase.delete();
        } else {
            Character.characterSave(characterArray, characterDatabase);
            BufferedWriter buffWrite = getWriter(write);
            buffWrite.flush();
            buffWrite.close();
        }

        System.out.println("Bye-bye... for now...");
        System.exit(666);
    }

    /*
    Unused, planned to be part of game but didn't have time to finish internal gameplay components
     */
    public static boolean endGame() {
        return true;
    }
}