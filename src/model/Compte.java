package model;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Compte {

    private String idCompte;
    private TypeCompte typeCompte;
    private double solde;
    private List<Transaction> transactions;
    private Client client;

    public Compte() {
        this.transactions = new ArrayList<>();
    }

    public Compte(String idCompte, TypeCompte typeCompte, double solde) {
        this.idCompte = idCompte;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.transactions = new ArrayList<>();
    }

    public Compte( String idCompte, TypeCompte typeCompte, double soldeInitial, Client client) {
        this.idCompte = idCompte;
        this.typeCompte = typeCompte;
        this.solde = soldeInitial;
        this.transactions = new ArrayList<>();
        this.client = client;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }



    public Transaction ajouterTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        switch (transaction.getTypeTransaction()) {
            case DEPOT:
                this.solde += transaction.getMontant();
                break;
            case RETRAIT:
                this.solde -= transaction.getMontant();
                break;
            case VIREMENT:
                if(transaction.getCompteSource() == this) {
                    this.solde -= transaction.getMontant();
                } else if(transaction.getCompteDestination() == this) {
                    this.solde += transaction.getMontant();
                }
                break;
            default:
                throw new IllegalArgumentException("Type de transaction inconnu");
        }
        return transaction;
    }

    public List<Transaction> filtrerTransactionsParType(TypeTransaction type) {
        return this.transactions.stream()
                .filter(t -> t.getTypeTransaction() == type)
                .collect(Collectors.toList());
    }
}
