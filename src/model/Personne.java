package model;

public abstract class Personne {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    public Personne() {
    }

    public Personne(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean authentifier(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
    }

    @Override
    public String toString() {
        return "Nom: " + nom + ", Prénom: " + prenom + ", Email: " + email;
    }


}
