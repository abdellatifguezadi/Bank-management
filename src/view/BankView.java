package view;

import controller.ClientController;
import controller.GestionnaireController;
import model.Client;
import model.Gestionnaire;
import services.ClientServices;
import services.GestionnaireServices;
import services.TransactionServices;

public class BankView {
    public void start() {
        ConsoleView consoleView = new ConsoleView();

        ClientServices clientService= new ClientServices();
        TransactionServices transactionService = new TransactionServices();
        GestionnaireServices gestionnaireService = new GestionnaireServices(clientService);

        Gestionnaire gestionnaireConnecte = null;

        ClientController clientController = new ClientController(clientService, transactionService);
        GestionnaireController gestionnaireController = new GestionnaireController(gestionnaireService, clientService);

        ClientView clientView = new ClientView(consoleView, clientController);

        Gestionnaire gestionnaire = gestionnaireService.creerGestionnaire("Gestion", "admin", "abdellatif" , "admin@bank.ma", "Admin@123");
        Client client = clientService.creerClient( "Doe", "John", "client@bank.ma", "Client@123");
        clientService.creerCompte(client, model.TypeCompte.COURANT, 1000);
        clientService.creerCompte(client, model.TypeCompte.EPARGNE, 5000);
        clientService.creerCompte(client, model.TypeCompte.COURANT, 3000);



        consoleView.afficherHeader(" SYSTÈME DE GESTION BANCAIRE" );

        consoleView.displayMenu(" MENU PRINCIPAL",
                "Se connecter en tant que Client",
                "Se connecter en tant que Gestionnaire",
                "Quitter"
        );
        int choix = consoleView.readMenuChoice(1, 3);
        switch (choix) {
            case 1:
                clientView.afficherMenuPrincipal();
                break;
            case 2:
                System.out.println("--- Connexion Gestionnaire ---");
                break;
            case 3:

                break;
        }

        consoleView.afficherMessage("\nMerci d'avoir utilisé notre système de gestion bancaire.");
        consoleView.afficherMessage("À bientôt!");
    }


    
    
}
