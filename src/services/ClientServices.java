package services;

import model.Client;
import model.Compte;
import model.TypeCompte;

import java.util.List;
import java.util.NoSuchElementException;

public class ClientServices {

    private List<Client> clients;

    public ClientServices(List<Client> clients) {
        this.clients = clients;
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
        String id = "CLT";
        int randomNum = (int) (Math.random() * 9000) + 1000; // Génère un nombre aléatoire entre 1000 et 9999
        id += randomNum;
        return id;
    }

    public Client findClientByEmail(String email) {
        for (Client client : clients) {
            if (client.getEmail().equals(email)) {
                return client;
            }
        }
        return null;
    }


    public Client findClientById(String idClient) {
        for (Client client : clients) {
            if (client.getIdClient().equals(idClient)) {
                return client;
            }
        }
        return null;
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

    public Client mmodifierClient(String idClient, String nom, String prenom, String email) {
        Client client = findClientById(idClient);
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
        if(clientWithEmail != null && !clientWithEmail.getIdClient().equals(idClient)) {
            throw new IllegalArgumentException("Un compte avec cet email existe déjà");
        }
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
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

    public Compte trouverCompteById(Client client ,String id){
        return client.getComptes().stream()
                .filter(compte -> compte.getIdCompte().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Compte introuvable avec l'ID: " + id) );

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


}
