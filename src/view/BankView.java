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

        GestionnaireView gestionnaireView = new GestionnaireView(consoleView, gestionnaireController, transactionService, clientService);

        ClientView clientView = new ClientView(consoleView, clientController);



        Gestionnaire gestionnaire = gestionnaireService.creerGestionnaire("Gestion", "admin", "abdellatif" , "admin@bank.ma", "Admin@123");
        Client client = clientService.creerClient( "guezadi", "abdellatif", "client@bank.ma", "Client@123");
        Client client2 = clientService.creerClient( "guezadi", "idriss", "client1@bank.ma", "Client@123");

        clientService.creerCompte(client, model.TypeCompte.COURANT, 1000);
        clientService.creerCompte(client, model.TypeCompte.EPARGNE, 5000);
        clientService.creerCompte(client2, model.TypeCompte.COURANT, 3000);

        while (true) {
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
                    gestionnaireView.afficherMenuPrincipal();
                    break;
                case 3:
                    consoleView.afficherMessage("\nMerci d'avoir utilisé notre système de gestion bancaire.");
                    consoleView.afficherMessage("À bientôt!");
                    return;
            }
        }
    }

}
