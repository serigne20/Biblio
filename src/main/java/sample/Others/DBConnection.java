package sample.Others;
import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnection {
    /**
     * Connecte à la base de données SQL Server
     * @return connexion à la base de données SQL Server
     */
    public static Connection SQLConnection(){
        Connection sql = null;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String url = "jdbc:sqlserver://localhost;databaseName=Bibliotheque;user=LAPTOP-KA8PJG45\\erwan;password=;integratedSecurity=true";
            sql = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sql;
    }
    /**
     * Affiche les livres de la base de données
     * @param sqlCo connexion à la base de données
     * @param livresData liste des livres
     */
    public void selectQuery(Connection sqlCo, ObservableList<Bibliotheque.Livre> livresData){
        try {
            String query = "SELECT * FROM livre";
            PreparedStatement pst = sqlCo.prepareStatement(query);
            ResultSet resp = pst.executeQuery();
            livresData.clear();
            while (resp.next()) {
                Bibliotheque.Livre respLivre = new Bibliotheque.Livre();
                Bibliotheque.Livre.Auteur respAut = new Bibliotheque.Livre.Auteur();
                respLivre.setTitre(resp.getString("titre"));
                respAut.setNom(resp.getString("nomaut"));
                respAut.setPrenom(resp.getString("prenomaut"));
                respLivre.setAuteur(respAut);
                respLivre.setParution((short) resp.getInt("parution"));
                respLivre.setColonne((short) resp.getInt("colonne"));
                respLivre.setRangee((short) resp.getInt("rangee"));
                respLivre.setPresentation(resp.getString("res"));
                respLivre.setEtat(resp.getString("dispo"));
                respLivre.setURL(resp.getString("url"));
                respLivre.setEditeur(resp.getString("edition"));
                respLivre.setFormat(resp.getString("format"));
                livresData.add(respLivre);
            }
        }
        catch (Exception e) {
            System.out.println("raté");
        };
    }

    /**
     * Supprime la ligne selectionnée dans la base de données
     * @param sqlCo connexion SQL Server
     * @param livres liste des livres
     * @param Livreindex index du livre
     */
    public void deleteQuery(Connection sqlCo, ObservableList<Bibliotheque.Livre> livres, int Livreindex){
        try{
            String query = "DELETE FROM livre WHERE titre = '"+livres.get(Livreindex).getTitre() +
                    "' AND nomaut = '"+ livres.get(Livreindex).getAuteur().getNom() +
                    "' AND prenomaut = '"+ livres.get(Livreindex).getAuteur().getPrenom() +
                    "' AND parution ="+livres.get(Livreindex).getParution();
            PreparedStatement pst = null;
            pst = sqlCo.prepareStatement(query);
            int resp = pst.executeUpdate();
            if (resp == 1) {
                System.out.println("query worked");
            } else {
                System.out.println("query did not work");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Met à jour la ligne selectionnée dans la base de données
     * @param sqlCo connexion SQL Server
     * @param livre livre selectionné
     * @param oldTitre ancien titre du livre
     * @param oldNom ancien nom d'auteur du livre
     * @param oldPrenom ancien prenom d'auteur du livre
     * @param oldParu ancienne date de parution du livre
     */
    public void updateQuery(Connection sqlCo, Bibliotheque.Livre livre, String oldTitre, String oldNom, String oldPrenom, int oldParu){
        try{
            String query = "UPDATE livre SET " +
                    "titre = '" + livre.getTitre() + "', " +
                    "nomaut = '" + livre.getAuteur().getNom() + "', " +
                    "prenomaut ='" + livre.getAuteur().getPrenom() + "', " +
                    "parution =" + livre.getParution() + ", " +
                    "colonne =" + livre.getColonne() + ", " +
                    "rangee =" + livre.getRangee() + ", " +
                    "res ='" + livre.getPresentation() + "', " +
                    "dispo ='" + livre.getEtat() + "', " +
                    "edition ='" + livre.getEditeur() + "'," +
                    "format ='" + livre.getFormat() + "', " +
                    "url = '" + livre.getURL() + "'" +
                    "WHERE titre = '" + oldTitre +
                    "' AND nomaut = '" + oldNom +
                    "' AND prenomaut = '" + oldPrenom +
                    "' AND parution =" + oldParu;
            PreparedStatement pst = null;
            pst = sqlCo.prepareStatement(query);
            int resp = pst.executeUpdate();
            if (resp == 1) {
                System.out.println("query worked");
            } else {
                System.out.println("query did not work");
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Ajoute un livre dans la base de données
     * @param sqlCo connexion SQL Server
     * @param livre livre à ajouté
     */
    public void insertQuery(Connection sqlCo, Bibliotheque.Livre livre){
        try{
            String query = "INSERT INTO livre (titre, nomaut, prenomaut, parution, colonne, rangee, res," +
                    "dispo, edition, format, url) VALUES('" +
                    livre.getTitre() + "', '" +
                    livre.getAuteur().getNom() + "', '" +
                    livre.getAuteur().getPrenom() + "', " +
                    livre.getParution() + ", " +
                    livre.getColonne() + ", " +
                    livre.getRangee() + ", '" +
                    livre.getPresentation() + "', '" +
                    livre.getEtat() + "', '" +
                    livre.getEditeur() + "', '" +
                    livre.getFormat() + "', '" +
                    livre.getURL() + "')";

            PreparedStatement pst = sqlCo.prepareStatement(query);
            int resp = pst.executeUpdate();
            if (resp == 1) {
                System.out.println("query worked");
            } else {
                System.out.println("query did not work");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
