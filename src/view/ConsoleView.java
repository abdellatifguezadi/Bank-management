package view;

import java.util.Scanner;

public class ConsoleView {
    private static final Scanner scanner = new Scanner(System.in);


    public static void afficherMenu(String title, String... options) {

        System.out.println("\n" + "=".repeat(55));
        System.out.println(title);
        System.out.println("=".repeat(55));
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("=".repeat(55));

    }

    public static int readMenuChoice( int minChoice, int maxChoice) {
        int choice;
        do {
            System.out.println("Votre choix: ");
            try {
                String input = scanner.nextLine().trim();
                choice = Integer.parseInt(input);
                if (choice < minChoice || choice > maxChoice) {
                    System.out.printf("Veuillez choisir entre %d et %d.%n", minChoice, maxChoice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
                choice = -1;
            }
        } while (choice < minChoice || choice > maxChoice);
        return choice;
    }

}