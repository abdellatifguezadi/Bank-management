package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Client extends Personne {

    private String idClient;
    private List<Compte> comptes;

    public Client() {
    }

    public Client(String idClient, String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idClient = idClient;
        this.comptes = new ArrayList<>();
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public List<Compte> getComptes() {
        return comptes;
    }
    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }

    public void ajouterCompte(Compte compte) {
        this.comptes.add(compte);
        compte.setClient(this);
    }

    public boolean supprimerCompte(Compte compte) {
        return this.comptes.remove(compte);
    }

    public Compte trouverCompteParId(String idCompte) {
        return this.comptes.stream()
                .filter(compte -> compte.getIdCompte().equals(idCompte))
                .findFirst()
                .orElse(null);
    }

    public List<Compte> trouverCompteParType(TypeCompte typeCompte) {
        return this.comptes.stream()
                .filter(compte -> compte.getTypeCompte() == typeCompte)
                .collect(Collectors.toList());
    }

    public double calculerSoldeTotal() {
        return this.comptes.stream()
                .mapToDouble(Compte::getSolde)
                .sum();
    }


}
