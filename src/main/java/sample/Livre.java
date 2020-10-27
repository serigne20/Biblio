package sample;

public class Livre {
    private String titre;
    private String auteur;
    private String nom;
    private String prenom;
    private String resume;
    private String parution;
    private int colonne;
    private int rangee;

    public Livre (String titre, String prenom, String nom, String parution, int colonne, int rangee, String resume){
        this.titre = titre;
        this.nom = nom;
        this.prenom = prenom;
        this.auteur = prenom + " " + nom;
        this.resume = resume;
        this.parution = parution;
        this.colonne = colonne;
        this.rangee = rangee;
    }
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getParution() {
        return parution;
    }

    public void setParution(String parution) {
        this.parution = parution;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public int getRangee() {
        return rangee;
    }

    public void setRangee(int rangee) {
        this.rangee = rangee;
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
}
