package view;

import controller.ClientController;
import model.Client;
import model.Compte;
import model.Transaction;
import model.TypeTransaction;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientView {

    private ConsoleView consoleView;
    private ClientController clientController;
    private Client clientConnecte;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ClientView(ConsoleView consoleView, ClientController clientController) {
        this.consoleView = consoleView;
        this.clientController = clientController;
    }

    public void afficherMenuPrincipal() {
        while (clientConnecte == null) {
            consoleView.afficherMessage("\n=== AUTHENTIFICATION CLIENT ===");
            consoleView.afficherMessage("(Tapez '0' comme email ou mot de passe pour quitter)");
            String email = consoleView.demanderSaisie("Email:");
            String motDePasse = consoleView.demanderSaisie("Mot de passe:");
            if ("0".equalsIgnoreCase(email) || "0".equalsIgnoreCase(motDePasse)) {
                consoleView.afficherMessage("Merci d'avoir utilisé notre système de gestion bancaire.\nÀ bientôt!");
                return;
            }
            try {
                Client client = clientController.authentifierClient(email, motDePasse);
                if (client != null) {
                    clientConnecte = client;
                    consoleView.afficherSucces("Connexion réussie. Bienvenue " + client.getPrenom() + " " + client.getNom() + "!");
                } else {
                    consoleView.afficherErreur("Échec de l'authentification. Email ou mot de passe incorrect.");
                }
            } catch (IllegalArgumentException e) {
                consoleView.afficherErreur("Échec de l'authentification. " + e.getMessage());
            }
        }
        boolean running = true;
        while (running) {
            consoleView.displayMenu("MENU CLIENT - " + clientConnecte.getPrenom() + " " + clientConnecte.getNom(),
                    "Consulter mes comptes",
                    "Effectuer une opération (dépôt, retrait, virement)",
                    "Afficher l'historique des transactions",
                    "Se déconnecter"
            );
            int choix = consoleView.readMenuChoice(1, 4);
            switch (choix) {
                case 1:
                    afficherComptes();
                    break;
                case 2:
                    effectuerOperation();
                    break;
                case 3:
                    afficherHistoriqueTransactions();
                    break;
                case 4:
                    clientConnecte = null;
                    consoleView.afficherMessage("Vous avez été déconnecté.");
                    running = false;
                    break;
            }
        }
    }


    private void afficherComptes() {
        List<Compte> comptes = clientController.getComptes(clientConnecte);

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nVous n'avez aucun compte bancaire.");
            return;
        }

        consoleView.afficherMessage("\n=== MES COMPTES BANCAIRES ===");
        for (Compte compte : comptes) {
            consoleView.afficherMessage("\nID Compte: " + compte.getIdCompte());
            consoleView.afficherMessage("Type: " + compte.getTypeCompte());
            consoleView.afficherMessage("Solde: " + compte.getSolde() + " MAD");
            consoleView.afficherMessage("Nombre de transactions: " + compte.getTransactions().size());
            consoleView.afficherMessage("----------------------------------------");
        }

        consoleView.afficherMessage("\nSolde total: " + clientConnecte.calculerSoldeTotal() + " MAD");
    }

    private void effectuerOperation() {
        List<Compte> comptes = clientController.getComptes(clientConnecte);

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nVous n'avez aucun compte bancaire pour effectuer des opérations.");
            return;
        }

        consoleView.displayMenu("OPÉRATIONS BANCAIRES",
                "Dépôt",
                "Retrait",
                "Virement interne",
                "Virement externe",
                "Retour"
        );
        int choixOperation = consoleView.readMenuChoice(1, 5);

        if (choixOperation == 5) {
            return;
        }

        consoleView.afficherMessage("\n=== MES COMPTES ===");
        for (int i = 0; i < comptes.size(); i++) {
            Compte compte = comptes.get(i);
            consoleView.afficherMessage((i + 1) + ". " + compte.getIdCompte() + " - " + compte.getTypeCompte() + " - Solde: " + compte.getSolde() + " MAD");
        }

        try {
            int indexCompteSource = consoleView.demanderEntier("\nSélectionnez un compte:") - 1;
            if (!consoleView.validerIndex(indexCompteSource, comptes.size(), "compte")) {
                return;
            }

            Compte compteSource = comptes.get(indexCompteSource);
            String idCompteSource = compteSource.getIdCompte();

            switch (choixOperation) {
                case 1:
                    effectuerDepot(idCompteSource);
                    break;
                case 2:
                    effectuerRetrait(idCompteSource);
                    break;
                case 3:
                    effectuerVirementInterne(idCompteSource, comptes);
                    break;
                case 4:
                    effectuerVirementExterne(idCompteSource);
                    break;
            }
        } catch (Exception e) {
            consoleView.afficherErreur("Erreur lors de l'opération: " + e.getMessage());
        }
    }


    private void effectuerRetrait(String idCompte) {
        double montant = consoleView.demanderDecimal("Montant du retrait (MAD):");
        String motif = consoleView.demanderSaisie("Motif du retrait:");

        try {
            clientController.effuctuerRetrait(clientConnecte, idCompte, montant, motif);
            consoleView.afficherSucces("Retrait de " + montant + " MAD effectué avec succès.");
        } catch (IllegalArgumentException e) {
            consoleView.afficherErreur("Montant invalide: " + e.getMessage());
        } catch (ArithmeticException e) {
            consoleView.afficherErreur("Solde insuffisant: " + e.getMessage());
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Compte introuvable: " + e.getMessage());
        }
    }

    private void effectuerDepot(String idCompte) {
        double montant = consoleView.demanderDecimal("Montant du dépôt (MAD):");
        String motif = consoleView.demanderSaisie("Motif du dépôt:");

        try {
            clientController.effectuerDepot(clientConnecte, idCompte, montant, motif);
            consoleView.afficherSucces("Dépôt de " + montant + " MAD effectué avec succès.");
        } catch (IllegalArgumentException e) {
            consoleView.afficherErreur("Montant invalide: " + e.getMessage());
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Compte introuvable: " + e.getMessage());
        }
    }


    private void effectuerVirementInterne(String idCompteSource, List<Compte> comptes) {
        consoleView.afficherMessage("\n=== COMPTES DESTINATION ===");
        int compteur = 1;
        for (Compte compte : comptes) {
            if (!compte.getIdCompte().equals(idCompteSource)) {
                consoleView.afficherMessage(compteur + ". " + compte.getIdCompte() + " - " + compte.getTypeCompte() + " - Solde: " + compte.getSolde() + " MAD");
                compteur++;
            }
        }

        if (compteur == 1) {
            consoleView.afficherMessage("Vous n'avez pas d'autres comptes pour effectuer un virement interne.");
            return;
        }

        int indexCompteDestination = consoleView.demanderEntier("Sélectionnez un compte destination:") - 1;
        if (!consoleView.validerIndex(indexCompteDestination, comptes.size() - 1, "compte destination")) {
            return;
        }

        String idCompteDestination = "";
        compteur = 0;
        for (Compte compte : comptes) {
            if (!compte.getIdCompte().equals(idCompteSource)) {
                if (compteur == indexCompteDestination) {
                    idCompteDestination = compte.getIdCompte();
                    break;
                }
                compteur++;
            }
        }

        double montant = consoleView.demanderDecimal("Montant du virement (MAD):");
        String motif = consoleView.demanderSaisie("Motif du virement:");

        try {
            clientController.effectuerVirementInterne(clientConnecte, idCompteSource, idCompteDestination, montant, motif);
            consoleView.afficherSucces("Virement de " + montant + " MAD effectué avec succès.");
        } catch (IllegalArgumentException e) {
            consoleView.afficherErreur("Montant invalide: " + e.getMessage());
        } catch (ArithmeticException e) {
            consoleView.afficherErreur("Solde insuffisant: " + e.getMessage());
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Compte introuvable: " + e.getMessage());
        }
    }

    private void effectuerVirementExterne(String idCompteSource) {
        double montant = consoleView.demanderDecimal("Montant du virement externe (MAD):");
        String idCompteDestination = consoleView.demanderSaisie("ID du compte destination:");
        String motif = consoleView.demanderSaisie("Motif du virement:");
        try {
            Compte compteDestination = clientController.trouveCompteParIdGlobal(idCompteDestination);
            if (compteDestination == null) {
                consoleView.afficherErreur("Compte destination introuvable.");
                return;
            }
            clientController.effectuerVirementExterne(clientConnecte, idCompteSource, compteDestination, montant, motif);
            consoleView.afficherSucces("Virement externe de " + montant + " MAD effectué avec succès.");
        } catch (IllegalArgumentException e) {
            consoleView.afficherErreur("Montant invalide: " + e.getMessage());
        } catch (ArithmeticException e) {
            consoleView.afficherErreur("Solde insuffisant: " + e.getMessage());
        } catch (NoSuchElementException e) {
            consoleView.afficherErreur("Compte introuvable: " + e.getMessage());
        }
    }



    private void afficherHistoriqueTransactions() {
        List<Compte> comptes = clientController.getComptes(clientConnecte);

        if (comptes.isEmpty()) {
            consoleView.afficherMessage("\nVous n'avez aucun compte bancaire.");
            return;
        }

        consoleView.afficherMessage("\n=== MES COMPTES ===");
        for (int i = 0; i < comptes.size(); i++) {
            Compte compte = comptes.get(i);
            consoleView.afficherMessage((i + 1) + ". " + compte.getIdCompte() + " - " + compte.getTypeCompte() + " - Solde: " + compte.getSolde() + " MAD");
        }

        consoleView.displayMenu("Sélectionnez un compte pour voir les transactions",
                comptes.stream().map(c -> c.getIdCompte() + " - " + c.getTypeCompte() + " - Solde: " + c.getSolde() + " MAD").toArray(String[]::new)
        );
        int indexCompte = consoleView.readMenuChoice(1, comptes.size()) - 1;
        if (!consoleView.validerIndex(indexCompte, comptes.size(), "compte")) {
            return;
        }

        Compte compte = comptes.get(indexCompte);
        List<Transaction> transactions = compte.getTransactions();

        if (transactions.isEmpty()) {
            consoleView.afficherMessage("\nAucune transaction pour ce compte.");
            return;
        }

        consoleView.displayMenu("FILTRER LES TRANSACTIONS",
                "Toutes les transactions",
                "Dépôts uniquement",
                "Retraits uniquement",
                "Virements uniquement",
                "Retour"
        );
        int choixFiltrage = consoleView.readMenuChoice(1, 6);

        List<Transaction> transactionsFiltrees;
        switch (choixFiltrage) {
            case 1:
                transactionsFiltrees = transactions;
                break;
            case 2:
                transactionsFiltrees = compte.filtrerTransactionsParType(TypeTransaction.DEPOT);
                break;
            case 3:
                transactionsFiltrees = compte.filtrerTransactionsParType(TypeTransaction.RETRAIT);
                break;
            case 4:
                transactionsFiltrees = compte.filtrerTransactionsParType(TypeTransaction.VIREMENT);
                break;
            case 5:
                return;
            default:
                transactionsFiltrees = transactions;
        }

        consoleView.afficherMessage("\n=== HISTORIQUE DES TRANSACTIONS ===");
        for (Transaction transaction : transactionsFiltrees) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nID: ").append(transaction.getIdTransaction());
            sb.append("\nType: ").append(transaction.getTypeTransaction());
            sb.append("\nMontant: ").append(transaction.getMontant()).append(" MAD");
            sb.append("\nDate: ").append(transaction.getDate().format(DATE_FORMATTER));
            sb.append("\nMotif: ").append(transaction.getMotif());

            if (transaction.getTypeTransaction() == TypeTransaction.VIREMENT) {
                if (transaction.getCompteSource().getIdCompte().equals(compte.getIdCompte())) {
                    sb.append("\nVers: ").append(transaction.getCompteDestination().getIdCompte());
                } else {
                    sb.append("\nDe: ").append(transaction.getCompteSource().getIdCompte());
                }
            }


            consoleView.afficherMessage(sb.toString());
            consoleView.afficherMessage("----------------------------------------");
        }
        consoleView.afficherMessage("Solde actuel: " + compte.getSolde() + " MAD");
    }



}
