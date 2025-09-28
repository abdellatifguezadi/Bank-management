package view;

import controller.GestionnaireController;
import model.*;
import services.ClientServices;
import services.TransactionServices;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GestionnaireView {

    private ConsoleView consoleView;
    private GestionnaireController gestionnaireController;
    private TransactionServices transactionService;
    private Gestionnaire gestionnaireConnecte;
    private ClientServices clientService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final double SEUIL_TRANSACTION_SUSPECTE = 10000.0;


    public GestionnaireView(ConsoleView consoleView, GestionnaireController gestionnaireController, TransactionServices transactionService, ClientServices clientService) {
        this.consoleView = consoleView;
        this.gestionnaireController = gestionnaireController;
        this.transactionService = transactionService;
        this.clientService = clientService;
    }


    public void afficherMenuPrincipal() {
        while (gestionnaireConnecte == null) {
            consoleView.afficherMessage("\n=== AUTHENTIFICATION GESTIONNAIRE ===");
            consoleView.afficherMessage("(Tapez '0' comme email ou mot de passe pour quitter)");
            String email = consoleView.demanderSaisie("Email:");
            String motDePasse = consoleView.demanderSaisie("Mot de passe:");
            if ("0".equalsIgnoreCase(email) || "0".equalsIgnoreCase(motDePasse)) {
                consoleView.afficherMessage("Merci d'avoir utilisé notre système de gestion bancaire.\nÀ bientôt!");
                return;
            }
            try {
                Gestionnaire gestionnaire = gestionnaireController.authentifierGestionnaire(email, motDePasse);
                if (gestionnaire != null) {
                    gestionnaireConnecte = gestionnaire;
                    consoleView.afficherSucces("Connexion réussie. Bienvenue " + gestionnaire.getPrenom() + " " + gestionnaire.getNom() + "!");
                } else {
                    consoleView.afficherErreur("Échec de l'authentification. Email ou mot de passe incorrect.");
                }
            } catch (IllegalArgumentException e) {
                consoleView.afficherErreur("Échec de l'authentification. " + e.getMessage());
            }
        }
        boolean running = true;
        while (running) {
            consoleView.displayMenu("MENU GESTIONNAIRE - " + gestionnaireConnecte.getPrenom() + " " + gestionnaireConnecte.getNom(),
                    "Consulter mes informations personnelles",
                    "Gérer les clients",
                    "Gérer les comptes",
                    "Consulter les transactions",
                    "Se déconnecter"
            );
            int choix = consoleView.readMenuChoice(1, 5);
            switch (choix) {
                case 1:
                    afficherInformationsPersonnelles();
                    break;
                case 2:
                     afficherGestionClients();
                    break;
                case 3:
                     afficherGestionComptes();
                    break;
                case 4:
                    afficherTransactions();
                    break;
                case 5:
                    gestionnaireConnecte = null;
                    consoleView.afficherMessage("Vous avez été déconnecté.");
                    running = false;
                    break;
            }
        }
    }



    private void afficherInformationsPersonnelles() {
        consoleView.afficherMessage("\n=== MES INFORMATIONS PERSONNELLES ===");
        consoleView.afficherMessage("ID Gestionnaire: " + gestionnaireConnecte.getIdGestionnaire());
        consoleView.afficherMessage("Nom: " + gestionnaireConnecte.getNom());
        consoleView.afficherMessage("Prénom: " + gestionnaireConnecte.getPrenom());
        consoleView.afficherMessage("Email: " + gestionnaireConnecte.getEmail());
        consoleView.afficherMessage("Département: " + gestionnaireConnecte.getDepartement());
        consoleView.afficherMessage("Nombre de clients gérés: " + gestionnaireConnecte.getListeClients().size());
    }

    private void afficherGestionClients() {
        consoleView.displayMenu("GESTION DES CLIENTS",
                "Consulter la liste des clients",
                "Créer un nouveau client",
                "Modifier un client",
                "Supprimer un client",
                "Rechercher un client",
                "Retour"
        );
        int choix = consoleView.readMenuChoice(1, 6);
        switch (choix) {
            case 1:
                afficherListeClients();
                break;
            case 2:
                creerClient();
                break;
            case 3:
                modifierClient();
                break;
            case 4:
                supprimerClient();
                break;
            case 5:
                rechercherClient();
                break;
            case 6:
                // Retour
                break;
        }
    }


    private void afficherListeClients() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== LISTE DES CLIENTS ===");
        for (Client client : clients) {
            consoleView.afficherMessage("\nID: " + client.getIdClient());
            consoleView.afficherMessage("Nom: " + client.getNom());
            consoleView.afficherMessage("Prénom: " + client.getPrenom());
            consoleView.afficherMessage("Email: " + client.getEmail());
            consoleView.afficherMessage("Nombre de comptes: " + client.getComptes().size());
            consoleView.afficherMessage("Solde total: " + client.calculerSoldeTotal() + " MAD");
            consoleView.afficherMessage("----------------------------------------");
        }
    }


    private void creerClient() {
        consoleView.afficherMessage("\n=== CRÉATION D'UN NOUVEAU CLIENT ===");
        String nom = consoleView.demanderSaisie("Nom:");
        String prenom = consoleView.demanderSaisie("Prénom:");
        String email = consoleView.demanderSaisie("Email:");
        String motDePasse = consoleView.demanderSaisie("Mot de passe:");

        try {
            Client client = gestionnaireController.creerClient( nom, prenom, email, motDePasse);
            consoleView.afficherSucces("Client créé avec succès. ID: " + client.getIdClient());

            if (consoleView.demanderConfirmation("Voulez-vous créer un compte pour ce client maintenant?")) {
                creerCompte(client);
            }
        } catch (IllegalStateException e) {
            consoleView.afficherErreur("Impossible de créer le client: " + e.getMessage());
        } catch (Exception e) {
            consoleView.afficherErreur("Erreur lors de la création du client: " + e.getMessage());
        }
    }

    private void modifierClient() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== MODIFICATION D'UN CLIENT ===");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            consoleView.afficherMessage((i + 1) + ". " + client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")");
        }

        int indexClient = consoleView.demanderEntier("\nSélectionnez un client à modifier:") - 1;
        if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
            return;
        }

        Client client = clients.get(indexClient);
        consoleView.afficherMessage("\nModification du client: " + client.getPrenom() + " " + client.getNom());
        consoleView.afficherMessage("Laissez vide pour conserver la valeur actuelle.");

        String nom = consoleView.demanderSaisie("Nom (" + client.getNom() + "):");
        nom = nom.isEmpty() ? client.getNom() : nom;

        String prenom = consoleView.demanderSaisie("Prénom (" + client.getPrenom() + "):");
        prenom = prenom.isEmpty() ? client.getPrenom() : prenom;

        String email = consoleView.demanderSaisie("Email (" + client.getEmail() + "):");
        email = email.isEmpty() ? client.getEmail() : email;

        String motDePasse = consoleView.demanderSaisie("Nouveau mot de passe (laissez vide pour ne pas changer):");
        motDePasse = motDePasse.isEmpty() ? client.getMotDePasse() : motDePasse;

        try {
             gestionnaireController.modifierClient( client.getIdClient(), nom, prenom, email, motDePasse);
            consoleView.afficherSucces("Client mis à jour avec succès.");
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Client introuvable: " + e.getMessage());
        } catch (IllegalStateException e) {
            consoleView.afficherErreur("Impossible de mettre à jour le client: " + e.getMessage());
        }
    }

    private void supprimerClient() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== SUPPRESSION D'UN CLIENT ===");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            consoleView.afficherMessage((i + 1) + ". " + client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")");
        }

        int indexClient = consoleView.demanderEntier("\nSélectionnez un client à supprimer:") - 1;
        if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
            return;
        }

        Client client = clients.get(indexClient);

        if (!client.getComptes().isEmpty()) {
            consoleView.afficherErreur("Ce client possède encore des comptes actifs. Veuillez d'abord supprimer tous ses comptes.");
            return;
        }

        if (consoleView.demanderConfirmation("Êtes-vous sûr de vouloir supprimer le client " + client.getPrenom() + " " + client.getNom() + "?")) {
            try {
                boolean result = gestionnaireController.supprimerClient( client.getIdClient());
                if (result) {
                    consoleView.afficherSucces("Client supprimé avec succès.");
                } else {
                    consoleView.afficherErreur("Impossible de supprimer le client.");
                }
            } catch (NoSuchElementException e) {
                consoleView.afficherErreur("Client introuvable: " + e.getMessage());
            }
        }
    }

    private void rechercherClient() {
        consoleView.displayMenu("RECHERCHE D'UN CLIENT", "Rechercher par Email", "Retour");
        int choix = consoleView.readMenuChoice(1, 2);
        if (choix == 2) {
            return;
        }
        System.out.print("Veuillez entrer l'email du client: ");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine().trim();
        Client client = clientService.trouverClientParEmail(email);
        if (client != null) {
            consoleView.afficherMessage("Client trouvé:");
            consoleView.afficherMessage("ID: " + client.getIdClient());
            consoleView.afficherMessage("Nom: " + client.getNom());
            consoleView.afficherMessage("Prénom: " + client.getPrenom());
            consoleView.afficherMessage("Email: " + client.getEmail());
        } else {
            consoleView.afficherErreur("Aucun client trouvé avec cet email.");
        }
    }


    private void afficherGestionComptes() {
        consoleView.displayMenu("GESTION DES COMPTES",
                "Consulter les comptes d'un client",
                "Créer un nouveau compte",
                "Supprimer un compte",
                "Retour"
        );
        int choix = consoleView.readMenuChoice(1, 4);
        switch (choix) {
            case 1:
                consulterComptesClient();
                break;
            case 2:
                creerCompte(null);
                break;
            case 3:
                supprimerCompte();
                break;
            case 4:
                // Retour
                break;
        }
    }


    private void consulterComptesClient() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== CONSULTATION DES COMPTES D'UN CLIENT ===");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            consoleView.afficherMessage((i + 1) + ". " + client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")");
        }

        int indexClient = consoleView.demanderEntier("\nSélectionnez un client:") - 1;
        if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
            return;
        }

        Client client = clients.get(indexClient);
        List<Compte> comptes = client.getComptes();

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nCe client n'a aucun compte bancaire.");
            return;
        }

        consoleView.afficherMessage("\n=== COMPTES DE " + client.getPrenom() + " " + client.getNom() + " ===");
        for (Compte compte : comptes) {
            consoleView.afficherMessage("\nID Compte: " + compte.getIdCompte());
            consoleView.afficherMessage("Type: " + compte.getTypeCompte());
            consoleView.afficherMessage("Solde: " + compte.getSolde() + " MAD");
            consoleView.afficherMessage("Nombre de transactions: " + compte.getTransactions().size());
            consoleView.afficherMessage("----------------------------------------");
        }

        consoleView.afficherMessage("\nSolde total: " + client.calculerSoldeTotal() + " MAD");
    }

    private void creerCompte(Client clientPreselectionne) {
        Client client = clientPreselectionne;
        if (client == null) {
            List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);
            if (clients.isEmpty()) {
                consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
                return;
            }
            consoleView.afficherMessage("\n=== CRÉATION D'UN NOUVEAU COMPTE ===");
            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                consoleView.afficherMessage((i + 1) + ". " + c.getPrenom() + " " + c.getNom() + " (" + c.getEmail() + ")");
            }
            int indexClient = consoleView.demanderEntier("\nSélectionnez un client:") - 1;
            if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
                return;
            }
            client = clients.get(indexClient);
        }
        consoleView.afficherMessage("\nCréation d'un compte pour " + client.getPrenom() + " " + client.getNom());
        consoleView.displayMenu("TYPE DE COMPTE",
                "COURANT",
                "EPARGNE",
                "DEPOTATERME",
                "Retour"
        );
        int indexTypeCompte = consoleView.readMenuChoice(1, 4);
        if (indexTypeCompte == 4) {
            return;
        }
        TypeCompte typeCompte = TypeCompte.valueOf(
                indexTypeCompte == 1 ? "COURANT" : indexTypeCompte == 2 ? "EPARGNE" : "DEPOTATERME"
        );
        double soldeInitial = consoleView.demanderDecimal("Solde initial (MAD):");

        try {
            Compte compte = gestionnaireController.creerCompte(client.getIdClient(), typeCompte, soldeInitial);
            consoleView.afficherSucces("Compte créé avec succès. ID: " + compte.getIdCompte());
        } catch (IllegalArgumentException e) {
            consoleView.afficherErreur("Solde initial invalide: " + e.getMessage());
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Client introuvable: " + e.getMessage());
        }
    }

    private void supprimerCompte() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== SUPPRESSION D'UN COMPTE ===");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            consoleView.afficherMessage((i + 1) + ". " + client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")");
        }

        int indexClient = consoleView.demanderEntier("\nSélectionnez un client:") - 1;
        if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
            return;
        }

        Client client = clients.get(indexClient);
        List<Compte> comptes = client.getComptes();

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nCe client n'a aucun compte bancaire.");
            return;
        }

        consoleView.afficherMessage("\n=== COMPTES DE " + client.getPrenom() + " " + client.getNom() + " ===");
        for (int i = 0; i < comptes.size(); i++) {
            Compte compte = comptes.get(i);
            consoleView.afficherMessage((i + 1) + ". " + compte.getIdCompte() + " - " + compte.getTypeCompte() + " - Solde: " + compte.getSolde() + " MAD");
        }

        int indexCompte = consoleView.demanderEntier("\nSélectionnez un compte à supprimer:") - 1;
        if (!consoleView.validerIndex(indexCompte, comptes.size(), "compte")) {
            return;
        }

        Compte compte = comptes.get(indexCompte);

        if (compte.getSolde() > 0) {
            consoleView.afficherErreur("Ce compte a encore un solde positif. Veuillez d'abord transférer le solde.");
            return;
        }

        if (compte.getTransactions().size() > 0) {
            if (!consoleView.demanderConfirmation("Ce compte a des transactions. Êtes-vous sûr de vouloir le supprimer?")) {
                return;
            }
        }

        try {
            boolean resultat = gestionnaireController.supprimerCompte(gestionnaireConnecte, client.getIdClient(), compte.getIdCompte());
            if (resultat) {
                consoleView.afficherSucces("Compte supprimé avec succès.");
            } else {
                consoleView.afficherErreur("Impossible de supprimer le compte.");
            }
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Client ou compte introuvable: " + e.getMessage());
        } catch (Exception e) {
            consoleView.afficherErreur("Erreur lors de la suppression du compte: " + e.getMessage());
        }
    }


    private void afficherTransactions() {
        List<Client> clients = gestionnaireController.getClientsGeres(gestionnaireConnecte);

        if (clients.isEmpty()) {
            consoleView.afficherMessage("\nVous ne gérez aucun client pour le moment.");
            return;
        }

        consoleView.afficherMessage("\n=== CONSULTATION DES TRANSACTIONS ===");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            consoleView.afficherMessage((i + 1) + ". " + client.getPrenom() + " " + client.getNom() + " (" + client.getEmail() + ")");
        }

        int indexClient = consoleView.demanderEntier("\nSélectionnez un client:") - 1;
        if (!consoleView.validerIndex(indexClient, clients.size(), "client")) {
            return;
        }

        Client client = clients.get(indexClient);
        List<Compte> comptes = client.getComptes();

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nCe client n'a aucun compte bancaire.");
            return;
        }

        consoleView.afficherMessage("\n=== COMPTES DE " + client.getPrenom() + " " + client.getNom() + " ===");
        for (int i = 0; i < comptes.size(); i++) {
            Compte compte = comptes.get(i);
            consoleView.afficherMessage((i + 1) + ". " + compte.getIdCompte() + " - " + compte.getTypeCompte() + " - Solde: " + compte.getSolde() + " MAD");
        }

        int indexCompte = consoleView.demanderEntier("\nSélectionnez un compte pour voir les transactions:") - 1;
        if (!consoleView.validerIndex(indexCompte, comptes.size(), "compte")) {
            return;
        }

        Compte compte = comptes.get(indexCompte);
        List<Transaction> transactions = compte.getTransactions();

        if (transactions.isEmpty()) {
            consoleView.afficherMessage("\nAucune transaction pour ce compte.");
            return;
        }

        consoleView.afficherMessage("\n=== TRANSACTIONS DU COMPTE " + compte.getIdCompte() + " ===");
        for (Transaction transaction : transactions) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nID: ").append(transaction.getIdTransaction());
            sb.append("\nType: ").append(transaction.getTypeTransaction());
            sb.append("\nMontant: ").append(transaction.getMontant()).append(" MAD");
            sb.append("\nDate: ").append(transaction.getDate().format(DATE_FORMATTER));
            sb.append("\nMotif: ").append(transaction.getMotif());

            if (transaction.getTypeTransaction() == model.TypeTransaction.VIREMENT) {
                if (transaction.getCompteSource().getIdCompte().equals(compte.getIdCompte())) {
                    sb.append("\nVers: ").append(transaction.getCompteDestination().getIdCompte());
                } else {
                    sb.append("\nDe: ").append(transaction.getCompteSource().getIdCompte());
                }
            }

            consoleView.afficherMessage(sb.toString());
            consoleView.afficherMessage("----------------------------------------");
        }
    }




}
