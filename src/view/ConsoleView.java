package view;

import java.util.Scanner;

public class ConsoleView {
    private static final Scanner scanner = new Scanner(System.in);


    public void afficherMenu(String title, String... options) {

        System.out.println("\n" + "=".repeat(55));
        System.out.println(title);
        System.out.println("=".repeat(55));
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("=".repeat(55));

    }

    public int readMenuChoice( int minChoice, int maxChoice) {
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

    public void afficherHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title.toUpperCase());
        System.out.println("=".repeat(50));
    }

    public void displayMenu(String title, String... options) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title);
        System.out.println("=".repeat(50));
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("=".repeat(50));
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }

    public String demanderSaisie(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    public void afficherErreur(String message) {
        System.out.println("ERREUR: " + message);
    }

    public void afficherSucces(String message) {
        System.out.println("SUCCÈS: " + message);
    }


    public double demanderDecimal(String prompt) {
        while (true) {
            try {
                String saisie = demanderSaisie(prompt);
                return Double.parseDouble(saisie);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez saisir un nombre décimal valide.");
            }
        }
    }

    public int demanderEntier(String prompt) {
        while (true) {
            try {
                String saisie = demanderSaisie(prompt);
                return Integer.parseInt(saisie);
            } catch (NumberFormatException e) {
                afficherErreur("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    public boolean validerIndex(int index, int taille, String typeElement) {
        if (index < 0 || index >= taille) {
            afficherErreur("Numéro de " + typeElement + " invalide.");
            return false;
        }
        return true;
    }

}