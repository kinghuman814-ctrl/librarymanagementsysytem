package library;

import java.util.Scanner;

/**
 * Validator.java
 * Centralises all console input validation so that no invalid
 * data (empty text, non-numeric input, negative numbers, etc.)
 * ever reaches the rest of the program.
 */
public class Validator {

    private Scanner scanner;

    public Validator(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Reads a non-empty line of text from the console, re-prompting until valid. */
    public String readNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /** Reads an integer from the console, re-prompting until a valid integer is entered. */
    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter digits only.");
            }
        }
    }

    /** Reads an integer that must be zero or greater. */
    public int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Value cannot be negative. Please try again.");
        }
    }

    /** Reads an integer that must be strictly greater than zero. */
    public int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero. Please try again.");
        }
    }

    /** Reads a menu choice within the given inclusive range. */
    public int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            int choice = readInt(prompt);
            if (choice >= min && choice <= max) {
                return choice;
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    /** Reads a yes/no confirmation. Returns true for 'y' or 'Y'. */
    public boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            }
            System.out.println("Please enter Y or N.");
        }
    }
}
