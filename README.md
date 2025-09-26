
=======
# GestionBank

## Description

GestionBank est un projet Java pour la gestion complète d'une banque. Cette application permet de gérer les clients, leurs comptes bancaires, les transactions financières et les gestionnaires de la banque à travers une interface console interactive.

## Fonctionnalités

### Gestion des Clients
- ✅ Ajouter de nouveaux clients
- ✅ Supprimer des clients existants
- ✅ Consulter les informations des clients

### Gestion des Comptes
- ✅ Créer des comptes bancaires
- ✅ Associer des comptes aux clients
- ✅ Gérer différents types de comptes

### Gestion des Transactions
- ✅ Effectuer des dépôts
- ✅ Effectuer des retraits
- ✅ Réaliser des virements entre comptes
- ✅ Historique des transactions

### Interface
- ✅ Interface console intuitive et interactive
- ✅ Menus navigables pour toutes les opérations

## Structure du Projet

```
src/
├── Main.java                    
├── controller/                  
│   ├── ClientController.java
│   └── GestionnaireController.java
├── model/                       
│   ├── Client.java
│   ├── Compte.java
│   ├── Gestionnaire.java
│   ├── Personne.java
│   ├── Transaction.java
│   ├── TypeCompte.java
│   └── TypeTransaction.java
├── services/                   
│   ├── ClientServices.java
│   ├── GestionnaireServices.java
│   └── TransactionServices.java
└── view/                        
    ├── BankView.java
    ├── ClientView.java
    ├── ConsoleView.java
    └── GestionnaireView.java
```

## Architecture

Le projet suit le pattern **MVC (Model-View-Controller)** :

- **Model** : Classes représentant les entités métier (Client, Compte, Transaction, etc.)
- **View** : Classes gérant l'affichage et les interactions avec l'utilisateur
- **Controller** : Classes orchestrant les interactions entre les modèles et les vues
- **Services** : Classes contenant la logique métier et les règles de gestion

## Prérequis

- **Java Development Kit (JDK)** version 8 ou supérieure
- Un terminal ou une console pour l'exécution

## Utilisation

1. Lancez l'application
2. Suivez les menus interactifs pour :
   - Gérer les clients (ajout, suppression, consultation)
   - Créer et gérer les comptes bancaires
   - Effectuer des transactions financières
   - Consulter l'historique des opérations

## Types de Comptes Supportés

- Compte Courant
- Compte Épargne
- Compte Depotaterme

## Types de Transactions

- Dépôt
- Retrait
- Virement
- Consultation de solde

## Auteur

**Abdellatif Guezadi**  
Date de création : 26 septembre 2025

