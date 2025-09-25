package model;

import java.time.LocalDateTime;

public class Transaction {
    private String idTransaction;
    private TypeTransaction TypeTransaction;
    private double montant;
    private LocalDateTime date;
    private String motif;
    private Compte compteSource;
    private Compte compteDestination;



    public Transaction() {
    }

    public Transaction(String idTransaction, TypeTransaction typeTransaction, double montant, LocalDateTime date, String motif, Compte compteSource) {
        this.idTransaction = idTransaction;
        this.TypeTransaction = typeTransaction;
        this.montant = montant;
        this.date = date;
        this.motif = motif;
        this.compteSource = compteSource;
    }

    public Transaction(String idTransaction, TypeTransaction typeTransaction, double montant, LocalDateTime date, String motif, Compte compteSource , Compte compteDestination) {
        this.idTransaction = idTransaction;
        this.TypeTransaction = typeTransaction;
        this.montant = montant;
        this.date = date;
        this.motif = motif;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }
    public  TypeTransaction getTypeTransaction() {
        return TypeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.TypeTransaction = typeTransaction;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Compte getCompteSource() {
        return compteSource;
    }

    public void setCompteSource(Compte compteSource) {
        this.compteSource = compteSource;
    }

    public Compte getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(Compte compteDestination) {
        this.compteDestination = compteDestination;
    }


}



