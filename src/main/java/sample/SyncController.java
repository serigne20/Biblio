package sample;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SyncController {
    @FXML private Button syncButton;
    @FXML private Button overwriteButton;
    private ObservableList<Bibliotheque.Livre> livres;
    private Connection sqlCo = null;
    private PreparedStatement pst = null;
    public void getData(Connection sql, ObservableList<Bibliotheque.Livre> listLivre){
        livres = listLivre;
        sqlCo = sql;
    }
    public void selectQuery(){
        try {
            String query = "SELECT * FROM livre";
            pst = sqlCo.prepareStatement(query);
            ResultSet resp = pst.executeQuery();
            livres.clear();
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
                livres.add(respLivre);
                System.out.println(resp.getString("titre"));
            }
        }
        catch (Exception e) {
            System.out.println("raté");
        };
    }
    public void DBXMLSync(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) syncButton.getScene().getWindow();
            stage.close();
            for(int i=0;i<livres.size();i++){
                String query = "INSERT INTO livre (titre, nomaut, prenomaut, parution, colonne, rangee, res," +
                        "dispo, edition, format, url) VALUES('" +
                        livres.get(i).getTitre() +"', '"+
                        livres.get(i).getAuteur().getNom() +"', '"+
                        livres.get(i).getAuteur().getPrenom() +"', "+
                        livres.get(i).getParution() +", "+
                        livres.get(i).getColonne() +", "+
                        livres.get(i).getRangee() +", '"+
                        livres.get(i).getPresentation() +"', '"+
                        livres.get(i).getEtat() +"', '"+
                        livres.get(i).getEditeur() +"', '"+
                        livres.get(i).getFormat() +"', '"+
                        livres.get(i).getURL() +"')";
                pst = sqlCo.prepareStatement(query);
                int resp = pst.executeUpdate();
                if(resp == 1){
                    System.out.println("query worked");
                }else{
                    System.out.println("query did not work");
                }
            }
            selectQuery();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
    public void overwriteXML(){
        try {
            selectQuery();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) overwriteButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
