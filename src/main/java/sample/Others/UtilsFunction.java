package sample.Others;

import JaxbTests.Bibliotheque;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UtilsFunction {
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
                System.out.println(resp.getString("titre"));
            }
        }
        catch (Exception e) {
            System.out.println("raté");
        };
    }

    /**
     * Affichage d'une fenêtre d'erreur
     */
    public void erreur() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Erreur.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Erreur");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){System.out.println("raté6");}
    }

    /**
     * Vérifie l'unicité des livres via le Titre, l'Auteur et la date de Parution
     * @param livresData liste des livres
     * @param livre livre a vérifier
     * @return true pour une vérification sans problème et false dans le cas inverse
     */
    public boolean verifyUnicity(ObservableList<Bibliotheque.Livre> livresData, Bibliotheque.Livre livre){
        for(int i=0;i<livresData.size();i++){
            if (livresData.get(i).getTitre().equals(livre.getTitre()) &&
                    livresData.get(i).getAuteur().getNom().equals(livre.getAuteur().getNom()) &&
                    livresData.get(i).getAuteur().getPrenom().equals(livre.getAuteur().getPrenom()) &&
                    livresData.get(i).getParution()==livre.getParution()){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/unicity.fxml"));
                    Parent root1 = (Parent)fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.setTitle("Problème d'unicité");
                    stage.show();

                } catch (Exception e) {
                    System.out.println("raté");
                }
                return false;
            }
        }
        return true;
    }
    public boolean verifyUnicity(ObservableList<Bibliotheque.Livre> livresData, Bibliotheque.Livre livre, int index){
        for(int i=0;i<livresData.size();i++){
            if(i!=index) {
                if (livresData.get(i).getTitre().equals(livre.getTitre()) &&
                        livresData.get(i).getAuteur().getNom().equals(livre.getAuteur().getNom()) &&
                        livresData.get(i).getAuteur().getPrenom().equals(livre.getAuteur().getPrenom()) &&
                        livresData.get(i).getParution() == livre.getParution()) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/unicity.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.setTitle("Problème d'unicité");
                        stage.show();

                    } catch (Exception e) {
                        System.out.println("raté");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Vérifie l'unicité d'un livre dans la base de données
     * @param sqlCo connexion à la base de données
     * @param livresData liste des livres
     * @return true pour une vérification sans problème et false dans le cas inverse
     */
    public boolean verifyUnicityDB(Connection sqlCo, ObservableList<Bibliotheque.Livre> livresData){
        try {
            String query = "SELECT * FROM livre";
            PreparedStatement pst = sqlCo.prepareStatement(query);
            ResultSet resp = pst.executeQuery();
            while (resp.next()) {
                for (int i=0;i<livresData.size();i++){
                    if(resp.getString("titre").equals(livresData.get(i).getTitre()) &&
                    resp.getString("nomaut").equals(livresData.get(i).getAuteur().getNom()) &&
                    resp.getString("prenomaut").equals(livresData.get(i).getAuteur().getPrenom()) &&
                    resp.getInt("parution")==livresData.get(i).getParution()){
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/unicitySync.fxml"));
                            Parent root1 = (Parent)fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.setTitle("Problème d'unicité");
                            stage.show();

                        } catch (Exception e) {
                            System.out.println("raté");
                        }
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("raté");
        };
        return true;
    }
}
