package services;

import model.Client;
import model.Compte;
import model.TypeCompte;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class ClientServices {

    private List<Client> clients;

    public ClientServices() {
        this.clients = new ArrayList<>();
    }
    public boolean InValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(emailRegex);
    }

    public boolean InValideNom(String nom) {
        String nomRegex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        return !nom.matches(nomRegex);
    }

    public boolean InValidePrenom(String prenom) {
        String prenomRegex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        return !prenom.matches(prenomRegex);
    }

    public boolean InValideMotDePasse(String motDePasse){
        return motDePasse.trim().length() < 8;
    }

    public String generateIdClient() {
        return "CLT" + UUID.randomUUID().toString().substring(0, 8);
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    public Client findClientByEmail(String email) {
        String emailRecherche = email.trim().toLowerCase();
        for (Client client : clients) {
            if (client.getEmail().trim().toLowerCase().equals(emailRecherche)) {
                return client;
            }
        }
        return null;
    }


    public Client findClientById(String idClient) {
        return clients.stream()
                .filter(c -> c.getIdClient().equals(idClient))
                .findFirst()
                .orElse(null);
    }

    public Client creerClient(String nom, String prenom, String email, String motDePasse) {


        if(InValideNom(nom)){
            throw new IllegalArgumentException("Nom invalide");
        }

        if(InValidePrenom(prenom)){
            throw new IllegalArgumentException("Prénom invalide");
        }

        if(InValidEmail(email)){
            throw new IllegalArgumentException("Email invalide");
        }

        if(findClientByEmail(email) != null){
            throw new IllegalArgumentException("Un compte avec cet email existe déjà");
        }

        if(InValideMotDePasse(motDePasse)){
            throw new IllegalArgumentException("Mot de passe invalide (au moins 8 caractères)");
        }

        String idClient = generateIdClient();
        Client client = new Client(idClient, nom, prenom, email, motDePasse);
        clients.add(client);
        return client;
    }

    public Client modifierClient(Client client, String nom, String prenom, String email, String motDePasse) {

        if(client == null){
            throw new NoSuchElementException("Client introuvable");
        }
        if(InValideNom(nom)){
            throw new IllegalArgumentException("Nom invalide");
        }
        if(InValidePrenom(prenom)){
            throw new IllegalArgumentException("Prénom invalide");
        }
        if(InValidEmail(email)){
            throw new IllegalArgumentException("Email invalide");
        }
        Client clientWithEmail = findClientByEmail(email);
        if(clientWithEmail != null && !clientWithEmail.getEmail().equals(email)) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà");
        }
        if(InValideMotDePasse(motDePasse)){
            throw new IllegalArgumentException("Mot de passe invalide (au moins 8 caractères)");
        }
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setMotDePasse(motDePasse);
        return client;
    }

    public boolean supprimerClient(String idClient) {
        Client client = findClientById(idClient);
        if (client == null) {
            throw new NoSuchElementException("Client introuvable");
        }
        return clients.remove(client);
    }

    public Compte creerCompte(Client client, TypeCompte typeCompte , double soldeInitial ) {
        if(client == null){
            throw new IllegalArgumentException("Client invalide");
        }
        if(soldeInitial < 0){
            throw new IllegalArgumentException("Solde initial invalide");
        }

        if(typeCompte == null){
            throw new IllegalArgumentException("Type de compte invalide");
        }
        String idCompte = "CPT" + ((int)(Math.random() * 90000) + 10000);
        Compte compte = new Compte(idCompte, typeCompte, soldeInitial, client);
        client.ajouterCompte(compte);
        return compte;
    }

    public Client authentifierClient(String email, String motDePasse) {
        Client client = findClientByEmail(email);
        if(client != null && client.authentifier(email, motDePasse)){
            return client;
        } else {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
    }


    public Compte trouverCompteParIdGlobal(String id){
        for(Client client : clients){
          Compte compte = client.trouverCompteParId(id);
            if(compte != null){
                return compte;
            }
        }
        return null;
    }

    public Client trouverClientParEmail(String email) {
        String emailRecherche = email.trim().toLowerCase();
        return clients.stream()
                .filter(c -> c.getEmail().trim().toLowerCase().equals(emailRecherche))
                .findFirst()
                .orElse(null);
    }


    public boolean supprimerCompte(Client client, String idCompte) {
        Compte compte = client.trouverCompteParId(idCompte);
        if (compte == null) {
            throw new NoSuchElementException("Compte introuvable avec l'ID: " + idCompte);
        }
        return client.supprimerCompte(compte);
    }



}
