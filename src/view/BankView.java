package view;

import controller.ClientController;
import controller.GestionnaireController;
import model.Gestionnaire;
import services.ClientServices;
import services.GestionnaireServices;
import services.TransactionServices;

public class BankView {
    public void start() {

        ClientServices clientService= new ClientServices();
        TransactionServices transactionService = new TransactionServices();
        GestionnaireServices gestionnaireService = new GestionnaireServices(clientService);

        ClientController clientController = new ClientController(clientService, transactionService);
        GestionnaireController gestionnaireController = new GestionnaireController(gestionnaireService, clientService);

        Gestionnaire gestionnaire = gestionnaireService.creerGestionnaire("Gestion", "admin", "abdellatif" , "admin@bank.ma", "Admin@123");

        System.out.println("=======================================================");
        System.out.println("============= Systeme de Gestion Bancaire =============");
        System.out.println("=======================================================");


        ConsoleView.afficherMenu("Menu Principal","Se connecter en tant que Gestionnaire",
                "Se connecter en tant que Client",
                "Quitter");
        int choice  = ConsoleView.readMenuChoice(1,3);

        switch (choice){
            case 1 :
                System.out.println("Fonctionnalite 1 en cours de developpement...");
                break;
            case 2 :
                System.out.println("Fonctionnalite 2 en cours de developpement...");
                break;
            case 3 : System.out.println("Merci d'avoir utilise notre application bancaire. Au revoir!");
                     System.exit(0);
        }

        System.out.println("Welcome to the Bank Application!");

    }
}
