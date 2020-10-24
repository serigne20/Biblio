package sample;

public class livre {
    private int colone,range;
    private String titre, auteur, resume, parution;

    public livre (String tit, String nom, String prenom, String res, int c, int rang, String par ){
        this.titre=tit;
        this.auteur=nom+" "+prenom;
        this.resume=res;
        this.parution=par;
        this.colone=c;
        this.range=rang;
    }

    public int getColone() {
        return colone;
    }

    public int getRange() {
        return range;
    }

    public String getResume() {
        return resume;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getTitre() {
        return titre;
    }

    public String getParution() {
        return parution;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setColone(int colone) {
        this.colone = colone;
    }

    public void setParution(String parution) {
        this.parution = parution;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "livre{" +
                "titre :'" + titre + '\'' +
                "\n colone=" + colone +
                "\n range=" + range +
                "\n auteur='" + auteur + '\'' +
                "\n resume='" + resume + '\'' +
                "\n parution='" + parution + '\'' +
                '}';
    }
}

