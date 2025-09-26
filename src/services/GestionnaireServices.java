package services;

import model.Client;
import model.Gestionnaire;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestionnaireServices {
    private List<Gestionnaire> gestionnaires;
    private  ClientServices clientServices ;


    public GestionnaireServices(ClientServices clientServices){
        this.clientServices = clientServices ;
        this.gestionnaires = new ArrayList<>();
    }

    public Gestionnaire creerGestionnaire(String departement, String nom, String prenom, String email, String motDePasse) {

        if(InValideNom(nom)) {
            throw new IllegalArgumentException("Nom invalide");
        }
        if(InValidePrenom(prenom)) {
            throw new IllegalArgumentException("Prenom invalide");
        }
        if(InValideEmail(email)) {
            throw new IllegalArgumentException("Email invalide");
        }
        if(trouveGestionnaireParEmail(email) != null || trouveGestionnaireParEmail(email) != null) {
            throw new IllegalArgumentException("Email deja utilise");
        }
        if(InValideMotDePasse(motDePasse)) {
            throw new IllegalArgumentException("Mot de passe invalide");
        }

        if(departement == null || departement.trim().isEmpty()) {
            throw new IllegalArgumentException("Departement invalide");
        }

        String idGestionnaire  = generateIdGestionnaire();
        Gestionnaire gestionnaire = new Gestionnaire(idGestionnaire, departement, nom, prenom, email, motDePasse);
        gestionnaires.add(gestionnaire);
        return gestionnaire;
    }


    public boolean InValideNom(String nom) {
        String nomRegex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        return !nom.matches(nomRegex);
    }

    public boolean InValidePrenom(String prenom) {
        String prenomRegex = "^[a-zA-Z]+([ '-][a-zA-Z]+)*$";
        return !prenom.matches(prenomRegex);
    }

    public boolean InValideEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(emailRegex);
    }

    public boolean InValideMotDePasse(String motDePasse) {
        String motDePasseRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return !motDePasse.matches(motDePasseRegex);
    }

    public void affecterClientAGestionnaire(Gestionnaire gestionnaire, Client client){
        gestionnaire.ajouterClient(client);
    }

    public Gestionnaire trouveGestionnaireParEmail(String email){
        return gestionnaires.stream()
                .filter(g -> g.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    public Gestionnaire ahthenticateGestionnaire(String email, String mdp){
        Gestionnaire gestionnaire = trouveGestionnaireParEmail(email);
        if(gestionnaire != null && gestionnaire.authentifier(email,mdp)){
            return gestionnaire;
        } else {
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }
    }


    public String generateIdGestionnaire() {
        return "GES" + UUID.randomUUID().toString().substring(0, 8);
    }

    public Gestionnaire convertirClientEnGestionnaire(Client client, String departement) {
        String idGestionnaire = "GES" + UUID.randomUUID().toString().substring(0, 8);
        Gestionnaire gestionnaire = new Gestionnaire(idGestionnaire, departement, client.getNom(), client.getPrenom(), client.getEmail(), client.getMotDePasse());
        gestionnaires.add(gestionnaire);
        return gestionnaire;
    }

}
