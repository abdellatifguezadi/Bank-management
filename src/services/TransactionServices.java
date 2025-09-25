package services;

import model.Client;
import model.Compte;
import model.Transaction;
import model.TypeTransaction;

import java.time.LocalDateTime;

public class TransactionServices {

    public String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis();
    }

    public Compte getCompteById(Client client ,String Id){
        Compte compte = client.getComptes().stream()
                .filter(c -> c.getIdCompte().equals(Id))
                .findFirst().orElse(null);
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable avec l'ID: " + Id);
        }
        return compte;
    }

    public Transaction effectuerDepot(Client client, String idCompte, double montant, String motif) {
        Compte compte = getCompteById(client, idCompte);
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }
        String idTransaction = generateTransactionId();
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(idTransaction, TypeTransaction.DEPOT, montant, date, motif, compte);
        compte.ajouterTransaction(transaction);
        return transaction;
    }

    public Transaction effectuerRetrait(Client client, String idCompte, double montant, String motif) {
        Compte compte = getCompteById(client, idCompte);
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }
        if (compte.getSolde() < montant) {
            throw new ArithmeticException("Solde insuffisant pour effectuer ce retrait");
        }
        String idTransaction = generateTransactionId();
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(idTransaction, TypeTransaction.RETRAIT, montant, date, motif, compte);
        compte.ajouterTransaction(transaction);
        return transaction;
    }

    public Transaction effectuerVirement(Client client, String idCompteSource, String idCompteDestination, double montant, String motif) {
        Compte compteSource = getCompteById(client, idCompteSource);
        Compte compteDestination = getCompteById(client, idCompteDestination);
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du virement doit être positif");
        }
        if (compteSource.getSolde() < montant) {
            throw new ArithmeticException("Solde insuffisant pour effectuer ce virement");
        }
        String idTransaction = generateTransactionId();
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(idTransaction, TypeTransaction.VIREMENT, montant, date, motif, compteSource, compteDestination);
        compteSource.ajouterTransaction(transaction);
        compteDestination.ajouterTransaction(transaction);
        return transaction;
    }


    public Transaction effectuerVirement(Client client, String idCompteSource, Compte compteDestination, double montant, String motif) {
        Compte compteSource = getCompteById(client, idCompteSource);
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant du virement doit être positif");
        }
        if (compteSource.getSolde() < montant) {
            throw new ArithmeticException("Solde insuffisant pour effectuer ce virement");
        }
        String idTransaction = generateTransactionId();
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(idTransaction, TypeTransaction.VIREMENT, montant, date, motif, compteSource, compteDestination);
        compteSource.ajouterTransaction(transaction);
        compteDestination.ajouterTransaction(transaction);
        return transaction;
    }
}
