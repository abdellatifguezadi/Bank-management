package model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class Gestionnaire extends Personne {

    private String idGestionnaire;
    private String departement;
    private List<Client> listeClients;
    private List<Compte> listeComptes;

    public Gestionnaire() {
        this.listeClients = new ArrayList<>();
        this.listeComptes = new ArrayList<>();
    }

    public Gestionnaire(String idGestionnaire, String departement, String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.idGestionnaire = idGestionnaire;
        this.departement = departement;
        this.listeClients = new ArrayList<>();
        this.listeComptes = new ArrayList<>();
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public List<Client> getListeClients() {
        return listeClients;
    }


    @Override
    public String toString() {
        return super.toString() + ", ID Gestionnaire: " + idGestionnaire + ", Département: " + departement + ", Nombre de Clients: " + listeClients.size();
    }



}
