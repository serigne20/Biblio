package sample.ViewControllers;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Others.UtilsFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SyncController {
    @FXML private Button syncButton;
    @FXML private Button overwriteButton;
    private ObservableList<Bibliotheque.Livre> livres;
    private Connection sqlCo = null;
    private PreparedStatement pst = null;
    private UtilsFunction utils = new UtilsFunction();

    /**
     * Récupération des données envoyées par le Controller principal
     * @param sql valeur de connection
     * @param listLivre liste des livres du tableau
     */
    public void getData(Connection sql, ObservableList<Bibliotheque.Livre> listLivre){
        livres = listLivre;
        sqlCo = sql;
    }

    /**
     * Ajoute les livres d'un fichier XML à la base de données
     * {'UtilsFuntcion': {@link UtilsFunction#verifyUnicityDB(Connection, ObservableList)}} Vérification de l'unicité
     */
    public void DBXMLSync(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) syncButton.getScene().getWindow();
            stage.close();
            if(utils.verifyUnicityDB(sqlCo,livres)){
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
            }
            utils.selectQuery(sqlCo,livres);
        } catch (Exception e) {
            System.out.println("raté");
        };
    }

    /**
     * Remplace les données du tableau par celles de la base de données
     */
    public void overwriteXML(){
        try {
            utils.selectQuery(sqlCo,livres);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sync.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) overwriteButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println("raté");
        };
    }
}
