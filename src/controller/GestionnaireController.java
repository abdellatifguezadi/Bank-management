package controller;

import model.Client;
import model.Gestionnaire;
import services.ClientServices;
import services.GestionnaireServices;

public class GestionnaireController {

    private GestionnaireServices gestionnaireService;
    private ClientServices clientService;


    public GestionnaireController (GestionnaireServices gestionnaireServices , ClientServices clientServices){
        this.clientService = clientServices;
        this.gestionnaireService = gestionnaireServices;
    }


    public Gestionnaire authentifierGestionnaire(String email , String name){
        return  gestionnaireService.ahthenticateGestionnaire(email, name);
    }

    public Client creerClient( String nom, String prenom, String email, String motDePasse) {
        return clientService.creerClient(nom, prenom, email, motDePasse);
    }

    public Client modifierClient( String idClient, String nom, String prenom, String email, String motDePasse) {
        Client client = clientService.findClientById(idClient);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        return clientService.modifierClient(client, nom, prenom, email, motDePasse);
    }

    public boolean supprimerClient( String idClient) {
        Client client = clientService.findClientById(idClient);
        if (client == null) {
            throw new IllegalArgumentException("Client introuvable");
        }
        return clientService.supprimerClient(idClient);
    }

    public Gestionnaire getConvertirClientEnGestionnaire(Client client, String departement) {
        return gestionnaireService.convertirClientEnGestionnaire(client, departement);
    }


}
