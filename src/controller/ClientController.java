package controller;


import model.Client;
import model.Compte;
import services.ClientServices;
import services.TransactionServices;

import java.util.List;

public class ClientController {

    private ClientServices clientServices;
    private TransactionServices transactionService;

    public ClientController(ClientServices clientService, TransactionServices transactionService) {
        this.clientServices = clientService;
        this.transactionService = transactionService;
    }

    public Client authentifierClient(String email , String mdp){
        return clientServices.authentifierClient(email,mdp);
    }

    public List<Compte> getComptes(Client client){
        return client.getComptes();
    }

    public void effectuerDepot(Client client, String idCompte, double montant, String motif){
        transactionService.effectuerDepot(client,idCompte,montant,motif);
    }

    public void effuctuerRetrait(Client client , String idCompte , double montant , String motif){
        transactionService.effectuerRetrait(client , idCompte , montant , motif);
    }
    public void effectuerVirementInterne(Client client , String idCompteSource , String idCompteDestination , double montant , String motif){
        transactionService.effectuerVirement(client,idCompteSource,idCompteDestination,montant,motif);
    }

    public void effectuerVirementExterne(Client client , String idCompteSource , Compte compteDestination , double montant , String motif){
        transactionService.effectuerVirement(client,idCompteSource,compteDestination,montant,motif);
    }

    public Compte trouveCompteParIdGlobal(String id){
        return  clientServices.trouverCompteParIdGlobal(id);
    }




}
